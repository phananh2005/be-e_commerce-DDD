package com.phananh.e_commerce.product.application.dto.command;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
public class ProductCreateCommand {
    private String name;

    private String description;

    private Long categoryId;

    private Long brandId;
}
