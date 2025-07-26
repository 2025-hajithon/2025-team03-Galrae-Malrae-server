package com.backend.global.security.jwt;


import com.backend.global.security.UserPrincipal;
import com.backend.global.security.cookie.CookieProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CookieProvider cookieProvider;

    // Jwt 필요없는 공개 API 전용.
    // 따로 UrlConstants 클래스에서 WHITE_URLS 정의하도록 수정 예정.
    private static final List<String> EXCLUDED_URLS = List.of("/");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        for (String url : EXCLUDED_URLS) {
            if (request.getRequestURI().startsWith(url)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        String accessToken = cookieProvider
                .extractValueFromCookie(request, "access")
                .orElseThrow(() -> new RuntimeException(
                        "Missing access token in cookie: [" + request.getMethod() + "] " + request.getRequestURI()));

        Authentication authentication = getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private Authentication getAuthentication(String accessToken) {
        // DTO 구현해서 Claims 대신 필요한 정보만 받게 구현 필요
        final Claims claims = jwtService.getClaimsFromToken(accessToken);

        Long memberId = Long.valueOf(claims.getSubject());
        String handle = claims.get("handle", String.class);
        String memberRole = "ROLE_" + claims.get("memberRole", String.class);
        String adminRole = "ROLE_" + claims.get("adminRole", String.class);

        UserDetails userDetails = new UserPrincipal(memberId);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
