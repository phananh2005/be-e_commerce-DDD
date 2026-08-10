package com.phananh.e_commerce.product.infrastructure.persistence.repository.elasticsearch;

import com.phananh.e_commerce.product.domain.document.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductDocumentRepository extends ElasticsearchRepository<ProductDocument, String> {
}
