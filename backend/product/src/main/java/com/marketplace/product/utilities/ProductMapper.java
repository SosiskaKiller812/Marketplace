package com.marketplace.product.utilities;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

import com.marketplace.product.entities.Product;
import com.marketplace.product.entities.request.ProductRequest;
import com.marketplace.product.entities.response.ProductResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class ProductMapper {

    private final CategoryMapper categoryMapper;

    public Product toEntity(ProductRequest productRequest){
        return Product.builder()
        .name(productRequest.name())
        .description(productRequest.description())
        .price(productRequest.price())
        .amount(productRequest.amount())
        .sellerId(productRequest.sellerId())
        .categories(categoryMapper.toEntity(productRequest.categories()))
        .build();
    }

    public ProductResponse toResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .amount(product.getAmount())
                .categories(categoryMapper.toResponse(product.getCategories()))
                .sellerId(product.getSellerId())
                .build();
    }

    public List<Product> toEntity(Collection<ProductRequest> productRequests){
        return productRequests.stream().map(productRequest -> toEntity(productRequest)).toList();
    }

    public List<ProductResponse> toResponse(Collection<Product> products){
        return products.stream().map(product -> toResponse(product)).toList();
    }
}
