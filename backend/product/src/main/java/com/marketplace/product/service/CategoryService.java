package com.marketplace.product.service;

import java.util.List;

import com.marketplace.product.entity.Category;
import com.marketplace.product.entity.request.CategoryRequest;
import com.marketplace.product.entity.response.CategoryResponse;
import com.marketplace.product.exception.CategoryAlreadyExistsException;
import com.marketplace.product.exception.CategoryNotFoundException;

public interface CategoryService {
    public Category addCategory(CategoryRequest categoryRequest) throws CategoryAlreadyExistsException;

    public void deleteCategory(CategoryRequest categoryRequest) throws CategoryNotFoundException;

    public Category findByName(String name) throws CategoryNotFoundException;

    public List<CategoryResponse> getAll();

    public boolean existsByName(String name);
}
