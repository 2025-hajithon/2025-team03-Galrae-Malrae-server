package com.backend.global.security.redis;

import com.backend.domain.member.entity.Member;
import com.backend.global.security.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenRedisService {

    private final RedisTemplate<String, String> refreshTokenRedisTemplate;
    private final JwtProperties jwtProperties;

    public void saveRefreshToken(Member member, String refreshToken) {
        refreshTokenRedisTemplate
                .opsForValue()
                .set(
                        String.valueOf(member.getId()),
                        refreshToken,
                        Duration.ofSeconds(jwtProperties.refreshTokenExpSeconds()));
    }

    public Optional<String> getRefreshToken(Member member) {
        return Optional.ofNullable(refreshTokenRedisTemplate.opsForValue().get(String.valueOf(member.getId())));
    }

    public void deleteRefreshToken(Member member) {
        refreshTokenRedisTemplate.delete(String.valueOf(member.getId()));
    }
}
