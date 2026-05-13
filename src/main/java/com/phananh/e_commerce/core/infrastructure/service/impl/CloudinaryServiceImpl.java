package com.phananh.e_commerce.core.infrastructure.service.impl;

import com.cloudinary.Cloudinary;
import com.phananh.e_commerce.core.infrastructure.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file, String folder, String publicId) throws IOException {
        try {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("File is empty");
            }

            if (publicId != null && !publicId.isBlank()) {
                throw new IllegalArgumentException("publicId must not be blank");
            }

            Map<String, Object> uploadParams = new HashMap<>();
            uploadParams.put("folder", folder);
            uploadParams.put("resource_type", "auto");
            uploadParams.put("unique_filename", false);
            uploadParams.put("public_id", publicId);
            uploadParams.put("overwrite", true);
            uploadParams.put("invalidate", true);

            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            String url = (String) uploadResult.get("secure_url");

            log.info("File uploaded successfully to Cloudinary: {}", url);
            return url;
        } catch (IOException e) {
            log.error("Error uploading file to Cloudinary", e);
            throw e;
        }
    }
}


