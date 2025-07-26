package com.backend.domain.auth.service;

import com.backend.domain.auth.dto.request.SignupRequest;
import com.backend.domain.member.entity.Member;
import com.backend.domain.member.repository.MemberRepository;
import com.backend.global.security.cookie.CookieProvider;
import com.backend.global.security.jwt.JwtProvider;
import com.backend.global.security.jwt.JwtService;
import com.backend.global.util.MemberUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final CookieProvider cookieProvider;
    private final MemberRepository memberRepository;
    private final MemberUtil memberUtil;

    @Transactional
    public void signup(SignupRequest request) {
        Long memberId = memberUtil.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        member.updateUsername(request.username());
    }

    public void refreshJwtAndRotate(Cookie refreshTokenCookie, HttpServletResponse response) {
        // RT 만료되었는지 판단하기, 만료 또는 변조시 Exception
        String refreshToken = refreshTokenCookie.getValue();
        Long memberId = Long.valueOf(jwtService.getClaimsFromToken(refreshToken).getSubject());

        // 존재하지 않는 Member이면 Exception
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));

        // request cookie의 RT와 Redis의 RT 대조
        if (!jwtService.isRefreshTokenValid(member, refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        // AT 발급
        String newAccessToken = jwtService.createAccessToken(member);

        // RT 발급 및 redis 저장
        String newRefreshToken = jwtService.createRefreshToken(member);

        // AT, RT Cookie 설정
        ResponseCookie newAccessTokenCookie = cookieProvider.generateAccessTokenCookie(newAccessToken);
        response.addHeader(HttpHeaders.SET_COOKIE, newAccessTokenCookie.toString());

        ResponseCookie newRefreshTokenCookie = cookieProvider.generateRefreshTokenCookie(newRefreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, newRefreshTokenCookie.toString());
    }

    public void logout(Cookie accessTokenCookie, Cookie refreshTokenCookie, HttpServletResponse response) {
        // RT 만료되었는지 판단하기, 만료 또는 변조시 Exception
        String refreshToken = refreshTokenCookie.getValue();
        Long memberId = Long.valueOf(jwtService.getClaimsFromToken(refreshToken).getSubject());

        // 존재하지 않는 Member이면 Exception
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));

        // request cookie의 RT와 Redis의 RT 대조
        if (!jwtService.isRefreshTokenValid(member, refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        // AT Cookie 만료시키기
        ResponseCookie expiredAccessTokenCookie = cookieProvider.generateExpiredCookie(accessTokenCookie);
        response.addHeader(HttpHeaders.SET_COOKIE, expiredAccessTokenCookie.toString());

        // RT Cookie 만료시키기
        ResponseCookie expiredRefreshTokenCookie = cookieProvider.generateExpiredCookie(refreshTokenCookie);
        response.addHeader(HttpHeaders.SET_COOKIE, expiredAccessTokenCookie.toString());

        // redis에서 RT 삭제하기
        jwtService.deleteRefreshTokenFromRedis(member);
    }
}
