package com.marketplace.product.exception;

import com.marketplace.product.entity.request.CategoryRequest;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(CategoryRequest category) {
        super("Category '" + category.getName() + "' not found");
    }

    public CategoryNotFoundException(String categoryName) {
        super("Category '" + categoryName + "' not found");
    }
}
