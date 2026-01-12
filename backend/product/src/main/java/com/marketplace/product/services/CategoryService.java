package com.marketplace.product.services;

import java.util.List;

import com.marketplace.product.entities.Category;
import com.marketplace.product.entities.request.CategoryRequest;
import com.marketplace.product.entities.response.CategoryResponse;
import com.marketplace.product.exceptions.CategoryAlreadyExistsException;
import com.marketplace.product.exceptions.CategoryNotFoundException;

public interface CategoryService {
    public Category addCategory(CategoryRequest categoryRequest) throws CategoryAlreadyExistsException;

    public void deleteCategory(CategoryRequest categoryRequest) throws CategoryNotFoundException;

    public Category findByName(String name) throws CategoryNotFoundException;

    public List<CategoryResponse> getAll();

    public boolean existsByName(String name);
}
