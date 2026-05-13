package com.example.muneoserver.global.security.redis.email;

import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailCodeStore {

    private static final String VERIFY_KEY_PREFIX = "auth:email:verify:";
    private static final String RESET_KEY_PREFIX = "auth:email:reset:";

    private final StringRedisTemplate redisTemplate;

    public void saveVerificationCode(String email, String code, long ttlSeconds) {
        redisTemplate.opsForValue().set(verifyKey(email), code, Duration.ofSeconds(ttlSeconds));
    }

    public Optional<String> findVerificationCode(String email) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(verifyKey(email)));
    }

    public void deleteVerificationCode(String email) {
        redisTemplate.delete(verifyKey(email));
    }

    public void saveResetCode(String email, String code, long ttlSeconds) {
        redisTemplate.opsForValue().set(resetKey(email), code, Duration.ofSeconds(ttlSeconds));
    }

    public Optional<String> findResetCode(String email) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(resetKey(email)));
    }

    public void deleteResetCode(String email) {
        redisTemplate.delete(resetKey(email));
    }

    private String verifyKey(String email) {
        return VERIFY_KEY_PREFIX + email;
    }

    private String resetKey(String email) {
        return RESET_KEY_PREFIX + email;
    }
}
