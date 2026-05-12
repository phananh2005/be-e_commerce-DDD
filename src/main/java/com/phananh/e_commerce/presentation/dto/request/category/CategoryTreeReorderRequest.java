package com.phananh.e_commerce.presentation.dto.request.category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryTreeReorderRequest {

    @Valid
    @NotEmpty(message = "Nodes must not be empty")
    private List<Node> nodes;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Node {

        @NotNull(message = "Category id is required")
        private Long categoryId;

        private Long parentId;

        @NotNull(message = "Display order is required")
        private Integer displayOrder;
    }
}



