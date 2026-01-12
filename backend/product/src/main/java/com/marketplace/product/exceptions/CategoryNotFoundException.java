package com.marketplace.product.exceptions;

import com.marketplace.product.entities.request.CategoryRequest;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(CategoryRequest category) {
        super("Category '" + category.getName() + "' not found");
    }

    public CategoryNotFoundException(String categoryName) {
        super("Category '" + categoryName + "' not found");
    }
}
