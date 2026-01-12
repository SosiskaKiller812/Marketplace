package com.marketplace.product.services;

import java.util.List;

import com.marketplace.product.entities.request.CategoryRequest;
import com.marketplace.product.entities.request.ProductRequest;
import com.marketplace.product.entities.response.ProductResponse;
import com.marketplace.product.exceptions.ProductNotFoundException;

public interface ProductService {
    public List<ProductResponse> getAll();

    public List<ProductResponse> getAllWithCategory(CategoryRequest category);

    public List<ProductResponse> getAllByName(String name);

    public ProductResponse getById(long id) throws ProductNotFoundException;

    public ProductResponse createProduct(ProductRequest productRequest);
}
