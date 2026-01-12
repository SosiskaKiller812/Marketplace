package com.marketplace.product.entities.request;

import java.math.BigDecimal;
import java.util.Set;

import javax.validation.constraints.*;

public record ProductRequest(
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    String name,

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    String description,

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Price must have up to 8 integer digits and 2 fraction digits")
    BigDecimal price,

    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount cannot be negative")
    Integer amount,

    Set<CategoryRequest> categories,

    @NotNull(message = "Seller ID is required")
    @Positive(message = "Seller ID must be positive")
    Long sellerId
) {}
