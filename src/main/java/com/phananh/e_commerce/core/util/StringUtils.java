package com.phananh.e_commerce.core.util;

import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;

public class StringUtils {

    public static String normalizeName(String name) {
        String normalizedName = name == null ? "" : name.trim();
        if (normalizedName.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        return normalizedName;
    }

    public static boolean isNotBlank(String str) {
        return str != null && !str.isBlank();
    }
}
