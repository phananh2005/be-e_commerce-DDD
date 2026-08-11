package com.phananh.e_commerce.core.infrastructure.annotation;

import org.springframework.http.ResponseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdempotencyCacheData {
    private String status;
    private ResponseEntity<?> response;
}
