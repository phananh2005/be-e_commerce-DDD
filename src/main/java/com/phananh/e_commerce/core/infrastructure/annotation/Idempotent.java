package com.phananh.e_commerce.core.infrastructure.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
    String headerName() default "Idempotency-Key";
    long timeout() default 86400; // 24 hours in seconds
}
