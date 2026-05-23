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

    @DeleteMapping("/file/{publicId}")
    public ResponseEntity<Void> deleteFile(@PathVariable String publicId) {
        try {
            cloudinaryService.deleteFile(publicId);
            return ResponseEntity.noContent().build();
        } catch (AppException ex) {
            throw ex; // Let global exception handler translate
        } catch (Exception ex) {
            throw new AppException(ErrorCode.FILE_DELETE_ERROR);
        }
    }

    /**
     * Delete a Cloudinary resource by its public URL.
     * Example: DELETE /cloudinary/file?url=https://res.cloudinary.com/.../upload/v12345/folder/name.jpg
     */
    @DeleteMapping("/file")
    public ResponseEntity<Void> deleteFileByUrl(@RequestParam String url) {
        try {
            cloudinaryService.deleteFileByUrl(url);
            return ResponseEntity.noContent().build();
        } catch (AppException ex) {
            throw ex; // Let global exception handler translate
        } catch (Exception ex) {
            throw new AppException(ErrorCode.FILE_DELETE_ERROR);
        }
    }
}

