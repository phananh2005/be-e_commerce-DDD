package com.phananh.e_commerce.core.infrastructure.aspect;

import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.core.infrastructure.annotation.Idempotent;
import com.phananh.e_commerce.core.infrastructure.annotation.IdempotencyCacheData;
import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class IdempotentAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String IDEMPOTENCY_PREFIX = "idempotency:";

    @Around("@annotation(idempotent)")
    public Object processIdempotency(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        HttpServletRequest request = attributes.getRequest();
        String idempotencyKey = request.getHeader(idempotent.headerName());

        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        String redisKey = IDEMPOTENCY_PREFIX + idempotencyKey;

        IdempotencyCacheData processingData = new IdempotencyCacheData("PROCESSING", null);
        Boolean isAbsent = redisTemplate.opsForValue().setIfAbsent(redisKey, processingData, idempotent.timeout(), TimeUnit.SECONDS);

        if (Boolean.FALSE.equals(isAbsent)) {
            Object cachedObj = redisTemplate.opsForValue().get(redisKey);
            if (cachedObj instanceof IdempotencyCacheData cacheData) {
                if ("PROCESSING".equals(cacheData.getStatus())) {
                    throw new AppException(ErrorCode.CONFLICT);
                } else if ("DONE".equals(cacheData.getStatus())) {
                    return cacheData.getResponse();
                }
            }
            throw new AppException(ErrorCode.CONFLICT);
        }

        try {
            Object result = joinPoint.proceed();

            if (result instanceof ResponseEntity<?> responseEntity) {
                IdempotencyCacheData doneData = new IdempotencyCacheData("DONE", responseEntity);
                redisTemplate.opsForValue().set(redisKey, doneData, idempotent.timeout(), TimeUnit.SECONDS);
            } else {
                IdempotencyCacheData doneData = new IdempotencyCacheData("DONE", ResponseEntity.ok(result));
                redisTemplate.opsForValue().set(redisKey, doneData, idempotent.timeout(), TimeUnit.SECONDS);
            }

            return result;
        } catch (Exception e) {
            redisTemplate.delete(redisKey);
            throw e;
        }
    }
}
