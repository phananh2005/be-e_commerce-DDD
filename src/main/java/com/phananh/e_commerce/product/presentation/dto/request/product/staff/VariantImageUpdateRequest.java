package com.phananh.e_commerce.product.presentation.dto.request.product.staff;

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
public class VariantImageUpdateRequest {

    @NotNull(message = "Variant avatar is required")
    private List<Long> variantAvatarId;

    @NotNull(message = "Variants are required")
    @Valid
    private List<VariantImageUpdate> variantImages;

    @Data
    public static class VariantImageUpdate {
        @NotNull(message = "Variant id is required")
        private Long variantImageId;

        @NotNull(message = "Images are required")
        private List<MultipartFile> variantImages;
    }
}


