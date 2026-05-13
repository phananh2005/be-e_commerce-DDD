package com.phananh.e_commerce.core.infrastructure.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    /**
     * Upload a file to Cloudinary with a specific public ID
     *
     * @param file the file to upload
     * @param folder the folder in Cloudinary where the file will be stored
     * @param publicId the public ID for the uploaded file
     * @return the URL of the uploaded file
     * @throws IOException if an error occurs during upload
     */
    String uploadFile(MultipartFile file, String folder, String publicId) throws IOException;
}


