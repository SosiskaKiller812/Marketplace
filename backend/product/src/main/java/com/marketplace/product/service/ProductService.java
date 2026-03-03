package com.marketplace.product.service;

import java.util.List;

import com.marketplace.product.entity.request.CategoryRequest;
import com.marketplace.product.entity.request.ProductRequest;
import com.marketplace.product.entity.response.ProductResponse;
import com.marketplace.product.exception.ProductNotFoundException;

public interface ProductService {
    public List<ProductResponse> getAll();

    public List<ProductResponse> getAllWithCategory(CategoryRequest category);

    public List<ProductResponse> getAllByName(String name);

    public ProductResponse getById(long id) throws ProductNotFoundException;

    public ProductResponse createProduct(ProductRequest productRequest);
}
