package com.backend.global.security.jwt;

import com.backend.domain.member.entity.Member;
import com.backend.global.properties.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

    // Member는 변경되어서는 안됨. dto 고려.
    // Study별 권한은 그 안에서 검증합니다.
    public String generateAccessToken(Member member) {
        final Date now = new Date();
        final Date expiration = new Date(now.getTime() + jwtProperties.accessTokenExpSeconds() * 1000);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("GalraeMalrae")
                .setSubject(member.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .claim("type", "access")
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Member member) {
        final Date now = new Date();
        final Date expiration = new Date(now.getTime() + jwtProperties.refreshTokenExpSeconds() * 1000);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("GalraeMalrae")
                .setSubject(member.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .claim("type", "refresh")
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer ".length() == 7
        }
        return null;
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey()) // 서명 검증 위해 비밀 키 설정
                .build()
                .parseClaimsJws(token) // 서명 검증 포함
                .getBody();
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secretKey()));
    }
}
