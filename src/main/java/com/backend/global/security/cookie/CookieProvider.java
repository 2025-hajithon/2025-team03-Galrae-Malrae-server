package com.backend.global.security.cookie;

import com.backend.global.properties.JwtProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CookieProvider {

    private final JwtProperties jwtProperties;

    // local test 시에, http라면 secure(false)로 설정, domain 설정은 빼주세요.

    public ResponseCookie generateAccessTokenCookie(String accessToken) {
        return ResponseCookie.from("access", accessToken)
                .maxAge(jwtProperties.accessTokenExpSeconds())
                .domain("galraemalrae.duckdns.org")
                .path("/")
                .secure(true) // prod, test: true, local: false
                .httpOnly(true)
                .sameSite("None") // prod, test: None, local: Lax
                .build();
    }

    public ResponseCookie generateRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refresh", refreshToken)
                .maxAge(jwtProperties.refreshTokenExpSeconds())
                .domain("galraemalrae.duckdns.org")
                .path("/auth/refresh")
                .secure(true) // prod, test: true, local: false
                .httpOnly(true)
                .sameSite("None") // prod, test: None, local: Lax
                .build();
    }

    public ResponseCookie generateExpiredCookie(Cookie cookie) {
        return ResponseCookie.from(cookie.getName(), "")
                .maxAge(0)
                .domain("galraemalrae.duckdns.org")
                .path(cookie.getPath())
                .secure(true) // prod: true, local: false
                .httpOnly(true)
                .sameSite("None") // prod: None, local: Lax
                .build();
    }

    // CookieService로 이동해야함
    public Optional<String> extractValueFromCookie(HttpServletRequest request, String name) {
        return Optional.ofNullable(WebUtils.getCookie(request, name)).map(Cookie::getValue);
    }
}
