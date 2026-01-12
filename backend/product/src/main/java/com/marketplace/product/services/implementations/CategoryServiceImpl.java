package com.marketplace.product.services.implementations;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.marketplace.product.entities.Category;
import com.marketplace.product.entities.request.CategoryRequest;
import com.marketplace.product.entities.response.CategoryResponse;
import com.marketplace.product.exceptions.CategoryAlreadyExistsException;
import com.marketplace.product.exceptions.CategoryNotFoundException;
import com.marketplace.product.repositories.CategoryRepository;
import com.marketplace.product.repositories.ProductRepository;
import com.marketplace.product.services.CategoryService;
import com.marketplace.product.utilities.CategoryMapper;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Category addCategory(CategoryRequest categoryRequest) throws CategoryAlreadyExistsException {

        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new CategoryAlreadyExistsException(categoryRequest);
        }

        return categoryRepository.save(categoryMapper.toEntity(categoryRequest));
    }

    @Transactional
    @Override
    public void deleteCategory(@Valid CategoryRequest categoryRequest) throws CategoryNotFoundException {
        Category category = categoryRepository.findByName(categoryRequest.getName())
                .orElseThrow(() -> new CategoryNotFoundException(categoryRequest));

        productRepository.deleteCategoryRelations(category.getId());

        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryResponse> getAll() {
        List<Category> list = categoryRepository.findAll();
        return categoryMapper.toResponse(list).stream().collect(Collectors.toList());
    }

    @Override
    public Category findByName(String name) throws CategoryNotFoundException {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new CategoryNotFoundException(name));
    }

    @Override
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }
    

}
