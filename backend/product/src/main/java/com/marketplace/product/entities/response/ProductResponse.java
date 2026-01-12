package com.marketplace.product.entities.response;

import java.math.BigDecimal;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProductResponse {

    private long id;

    private String name;

    private String description;

    private BigDecimal price;

    private int amount;

    private Set<CategoryResponse> categories;

    private long sellerId;
}
