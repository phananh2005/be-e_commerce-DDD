package com.phananh.e_commerce.product.application.service.impl;

import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.core.util.PageUtils;
import com.phananh.e_commerce.core.util.StringUtils;
import com.phananh.e_commerce.product.application.dto.response.customer.ProductDetailResponse;
import com.phananh.e_commerce.product.application.dto.response.customer.ProductSummaryResponse;
import com.phananh.e_commerce.product.application.mapper.CustomerProductMapper;
import com.phananh.e_commerce.product.application.service.CustomerProductService;
import com.phananh.e_commerce.product.domain.document.ProductDocument;
import com.phananh.e_commerce.product.domain.model.Product;
import com.phananh.e_commerce.product.domain.repository.ProductRepository;
import com.phananh.e_commerce.product.presentation.dto.request.customer.CustomerProductSearchRequest;
import com.phananh.e_commerce.productcatalog.application.service.BrandService;
import com.phananh.e_commerce.productcatalog.application.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerProductServiceImpl implements CustomerProductService {

    ProductRepository productRepository;
    CustomerProductMapper customerProductMapper;
    BrandService brandService;
    CategoryService categoryService;
    ElasticsearchOperations elasticsearchOperations;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductSummaryResponse> getProductsActiveBySearch(CustomerProductSearchRequest request) {
        int page = PageUtils.getPageNumber(request.getPage());
        int size = PageUtils.getPageSize(request.getSize());
        String sortBy = PageUtils.getSortBy(request.getSortBy());
        String sortType = PageUtils.getSortType(request.getSortType());
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortType), sortBy));

        BoolQuery.Builder boolBuilder = new BoolQuery.Builder();

        if (!StringUtils.isBlank(request.getKeyword())) {
            String kw = request.getKeyword().trim();
            Query multiMatch = Query.of(q -> q.multiMatch(m -> m
                    .query(kw)
                    .fields(java.util.Arrays.asList(
                            "name^6", 
                            "name.unaccent^5", 
                            "name.english^4", 
                            "description^3", 
                            "description.unaccent^2", 
                            "description.english^1"
                    ))
                    .fuzziness("AUTO")
            ));
            boolBuilder.must(multiMatch);
        }

        if (request.getCategoryId() != null) {
            boolBuilder.filter(Query.of(q -> q.term(t -> t
                    .field("categoryId")
                    .value(request.getCategoryId())
            )));
        }
        if (request.getBrandId() != null) {
            boolBuilder.filter(Query.of(q -> q.term(t -> t
                    .field("brandId")
                    .value(request.getBrandId())
            )));
        }
        if (request.getMinPrice() != null || request.getMaxPrice() != null) {
            boolBuilder.filter(Query.of(q -> q.range(r -> r.number(n -> {
                n.field("minPrice");
                if (request.getMinPrice() != null) {
                    n.gte(request.getMinPrice().doubleValue());
                }
                if (request.getMaxPrice() != null) {
                    n.lte(request.getMaxPrice().doubleValue());
                }
                return n;
            }))));
        }

        Query finalQuery = Query.of(q -> q.bool(boolBuilder.build()));

        NativeQuery query = NativeQuery.builder()
                .withQuery(finalQuery)
                .withPageable(pageable)
                .build();

        SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(query, ProductDocument.class);

        List<ProductSummaryResponse> content = searchHits.stream().map(hit -> {
            ProductDocument doc = hit.getContent();
            ProductSummaryResponse response = new ProductSummaryResponse();
            if (doc.getId() != null) {
                try {
                    response.setProductId(Long.valueOf(doc.getId()));
                } catch(NumberFormatException ignored) {}
            }
            if (doc.getUuid() != null) {
                response.setProductUuid(doc.getUuid().toString());
            }
            response.setProductName(doc.getName());
            if (doc.getMinPrice() != null) {
                response.setMinPrice(BigDecimal.valueOf(doc.getMinPrice()));
            }
            response.setAvatarUrl(doc.getAvatarUrl());
            return response;
        }).collect(Collectors.toList());

        return new PageImpl<>(content, pageable, searchHits.getTotalHits());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailResponse getProductById(String uuid) {
        Product product = productRepository.getProductByUuid(uuid).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getVariants() == null || product.getVariants().isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND);
        }

        ProductDetailResponse response = customerProductMapper.toProductDetailResponse(product);

        if (product.getBrandId() != null) {
            response.setBrandName(brandService.getBrandNameById(product.getBrandId()));
        }
        if (product.getCategoryId() != null) {
            response.setCategoryName(categoryService.getCategoryNameById(product.getCategoryId()));
        }

        return response;
    }
}
