package com.backend.global.security.oauth2;

import com.backend.global.security.cookie.CookieProvider;
import com.backend.global.security.jwt.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final CookieProvider cookieProvider;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        // AT, RT 생성
        String accessToken = jwtService.createAccessToken(oAuth2User.getMember());
        String refreshToken = jwtService.createRefreshToken(oAuth2User.getMember());
        System.out.println("===============================================");
        System.out.println("AT, RT 생성 완료");
        System.out.println("===============================================");

        // AT, RT 쿠키 생성
        ResponseCookie accessTokenCookie = cookieProvider.generateAccessTokenCookie(accessToken);
        ResponseCookie refreshTokenCookie = cookieProvider.generateRefreshTokenCookie(refreshToken);
        System.out.println("===============================================");
        System.out.println("AT, RT 쿠키 생성 완료");
        System.out.println("===============================================");

        // AT, RT set-cookie 설정
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        System.out.println("===============================================");
        System.out.println("set-cookie 완료");
        System.out.println("===============================================");

        // Redirect URI 설정
        String targetUrl = determineTargetUrl(request, response, authentication);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
        System.out.println("===============================================");
        System.out.println("redirect uri 설정 완료");
        System.out.println("===============================================");

        // 인증 속성 제거
        clearAuthenticationAttributes(request);
    }

    // prod에서 localhost 변경 필요. (인증 성공 or 실패, 회원가입 필요 or 필요없음에 따라 redirect-uri 변경)
    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        boolean needSignup = oAuth2User.getMember().getUsername() == null;

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("https://2025-team03-galrae-malrae-web.vercel.app/oauth/callback")
                .queryParam("needSignup", needSignup);

        return builder.build().toUriString();
    }
}
