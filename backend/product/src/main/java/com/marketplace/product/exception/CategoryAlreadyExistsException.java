package com.marketplace.product.exception;

import com.marketplace.product.entity.request.CategoryRequest;

public class CategoryAlreadyExistsException extends RuntimeException{
    public CategoryAlreadyExistsException(CategoryRequest category){
        super(new String("Category with name '" + category.getName() + "' already exists"));
    }
}
