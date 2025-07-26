package com.backend.global.security.jwt;

import com.backend.domain.member.entity.Member;
import com.backend.global.security.redis.RefreshTokenRedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRedisService refreshTokenRedisService;

    public String createAccessToken(Member member) {
        return jwtProvider.generateAccessToken(member);
    }

    public String createRefreshToken(Member member) {
        String refreshToken = jwtProvider.generateRefreshToken(member);
        refreshTokenRedisService.saveRefreshToken(member, refreshToken);
        return refreshToken;
    }

    // 예외처리 로직 수정 필요. Java 21에서 pattern matching 적용도 가능.
    public Claims getClaimsFromToken(String token) {
        try {
            final Claims claims = jwtProvider.parseToken(token);
            String tokenType = claims.get("type", String.class);
            if (tokenType.equals("access") || tokenType.equals("refresh")) {
                return claims;
            }
            throw new JwtException("Invalid token");
        } catch (RuntimeException e) {
            throw new JwtException("Invalid token", e);
        }
    }

    // 따로 validateToken으로 예외 처리 책임 분리
    public boolean isRefreshTokenValid(Member member, String refreshToken) {
        return refreshTokenRedisService
                .getRefreshToken(member)
                .map(redisRefreshToken -> redisRefreshToken.equals(refreshToken))
                .orElse(false);
    }

    public void deleteRefreshTokenFromRedis(Member member) {
        refreshTokenRedisService.deleteRefreshToken(member);
    }
}
