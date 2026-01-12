package com.marketplace.product.exceptions;

import com.marketplace.product.entities.request.CategoryRequest;

public class CategoryAlreadyExistsException extends RuntimeException{
    public CategoryAlreadyExistsException(CategoryRequest category){
        super(new String("Category with name '" + category.getName() + "' already exists"));
    }
}
