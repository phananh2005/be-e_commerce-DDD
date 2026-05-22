package com.phananh.e_commerce.core.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class FileUtils {
    public static boolean isEmpty(MultipartFile file) {
        return file == null || file.isEmpty();
    }

    public static boolean isEmpty(List<MultipartFile> files) {
        return files == null || files.isEmpty();
    }
}
