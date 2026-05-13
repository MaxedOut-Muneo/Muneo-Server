package com.example.muneoserver.global.security.redis.social;

import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocialSignupTicketStore {

    private static final String KEY_PREFIX = "auth:social-signup:";

    private final StringRedisTemplate redisTemplate;

    public void save(String ticket, Long userId, long ttlSeconds) {
        redisTemplate.opsForValue().set(key(ticket), String.valueOf(userId), Duration.ofSeconds(ttlSeconds));
    }

    public Optional<Long> findUserId(String ticket) {
        String value = redisTemplate.opsForValue().get(key(ticket));
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(Long.valueOf(value));
    }

    public void delete(String ticket) {
        redisTemplate.delete(key(ticket));
    }

    private String key(String ticket) {
        return KEY_PREFIX + ticket;
    }
}
