package com.phananh.e_commerce.core.presentation.controller;

import com.phananh.e_commerce.core.infrastructure.service.CloudinaryService;
import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/cloudinary")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Cloudinary", description = "Signature endpoints for direct uploads")
public class CloudinaryController {

    CloudinaryService cloudinaryService;

    @GetMapping("/signature")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUploadSignature(@RequestParam String folder) {
        Map<String, Object> signature = cloudinaryService.generateUploadSignature(folder);
        ApiResponse<Map<String, Object>> response = ApiResponse.<Map<String, Object>>builder()
                .message("Cloudinary upload signature generated")
                .result(signature)
                .build();
        return ResponseEntity.ok(response);
    }
}

