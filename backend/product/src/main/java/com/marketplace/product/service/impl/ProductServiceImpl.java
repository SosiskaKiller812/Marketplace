package com.marketplace.product.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.marketplace.product.entity.Category;
import com.marketplace.product.entity.Product;
import com.marketplace.product.entity.request.CategoryRequest;
import com.marketplace.product.entity.request.ProductRequest;
import com.marketplace.product.entity.response.ProductResponse;
import com.marketplace.product.exception.CategoryNotFoundException;
import com.marketplace.product.exception.ProductNotFoundException;
import com.marketplace.product.mapper.ProductMapper;
import com.marketplace.product.repository.CategoryRepository;
import com.marketplace.product.repository.ProductRepository;
import com.marketplace.product.service.ProductService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductResponse> getAll() {
        return productMapper.toResponse(productRepository.findAll());
    }

    @Override
    public List<ProductResponse> getAllWithCategory(CategoryRequest categoryRequest) {
        Category category = categoryRepository.findByName(categoryRequest.getName())
                .orElseThrow(() -> new CategoryNotFoundException(categoryRequest));
        return productMapper.toResponse(productRepository.findByCategoriesContainingOrderByNameAsc(category));
    }

    @Override
    public List<ProductResponse> getAllByName(String name) {
        return productMapper.toResponse(productRepository.findByNameContaining(name));
    }

    @Override
    public ProductResponse getById(long id) throws ProductNotFoundException {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        
        Set<CategoryRequest> newSet = productRequest.categories().stream()
                .filter(c -> categoryRepository.existsByName(c.getName()))
                .collect(Collectors.toSet());

                
        ProductRequest newProductRequest = new ProductRequest(productRequest.name(), productRequest.description(),
                productRequest.price(), productRequest.amount(), newSet, productRequest.sellerId());
        
        Product newProduct = productMapper.toEntity(newProductRequest);

        return productMapper.toResponse(productRepository.save(newProduct));
    }

}
