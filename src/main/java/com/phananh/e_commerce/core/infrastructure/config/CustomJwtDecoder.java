package com.phananh.e_commerce.core.infrastructure.config;

import com.phananh.e_commerce.usermanagement.domain.model.User;
import com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository.springdata.SpringDataUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class CustomJwtDecoder implements JwtDecoder {

    private final JwtDecoder delegate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SpringDataUserRepository springDataUserRepository;

    public CustomJwtDecoder(@Value("${application.security.jwt.secret-key}") String secretKey,
                            RedisTemplate<String, Object> redisTemplate,
                            SpringDataUserRepository springDataUserRepository) {
        SecretKey key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        this.delegate = NimbusJwtDecoder.withSecretKey(key).build();
        this.redisTemplate = redisTemplate;
        this.springDataUserRepository = springDataUserRepository;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        if (Boolean.TRUE.equals(redisTemplate.hasKey("jwt:revoked:" + token))) {
            throw new BadJwtException("Token has been revoked");
        }

        Jwt jwt = delegate.decode(token);

        Optional<User> userOpt = springDataUserRepository.findByCredentials_Username(jwt.getSubject());
        if (userOpt.isEmpty() || userOpt.get().getCredentials() == null || !Boolean.TRUE.equals(userOpt.get().getCredentials().isEnabled())) {
            throw new BadJwtException("User account is disabled or not found");
        }

        return jwt;
    }
}
