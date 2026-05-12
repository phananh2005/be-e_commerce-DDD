package com.phananh.e_commerce.presentation.dto.request.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VariantImageCreateRequest {

    @NotNull(message = "Variants are required")
    @Valid
    private List<VariantImageCreate> variantImages;

    @Data
    public static class VariantImageCreate {
        @NotNull(message = "Variant id is required")
        private Long variantId;

        @NotNull(message = "Images are required")
        private List<MultipartFile> variantImages;

        @NotNull(message = "Variant avatar is required")
        private MultipartFile variantAvatar;
    }
}

