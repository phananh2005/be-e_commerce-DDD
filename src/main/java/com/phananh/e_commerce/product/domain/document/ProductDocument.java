package com.phananh.e_commerce.product.domain.document;

import lombok.*;

import java.time.Instant;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Document(indexName = "products")
@Setting(settingPath = "elasticsearch/settings/product_settings.json")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private UUID uuid;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "vi_analyzer"),
            otherFields = {
                    @InnerField(suffix = "unaccent", type = FieldType.Text, analyzer = "vi_smart_unaccent"),
                    @InnerField(suffix = "english", type = FieldType.Text, analyzer = "english")
            }
    )
    private String name;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "vi_analyzer"),
            otherFields = {
                    @InnerField(suffix = "unaccent", type = FieldType.Text, analyzer = "vi_smart_unaccent"),
                    @InnerField(suffix = "english", type = FieldType.Text, analyzer = "english")
            }
    )
    private String description;

    @Field(type = FieldType.Double)
    private Double minPrice;

    @Field(type = FieldType.Double)
    private Double maxPrice;

    @Field(type = FieldType.Keyword, index = false)
    private String avatarUrl;

    @Field(type = FieldType.Long)
    private Long categoryId;

    @Field(type = FieldType.Long)
    private Long brandId;

    @Field(type = FieldType.Date)
    private Instant createdAt;

    @Field(type = FieldType.Long)
    private Long version;
}
