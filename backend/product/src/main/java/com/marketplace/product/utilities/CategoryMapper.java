package com.marketplace.product.utilities;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.marketplace.product.entities.Category;
import com.marketplace.product.entities.request.CategoryRequest;
import com.marketplace.product.entities.response.CategoryResponse;
import com.marketplace.product.exceptions.CategoryNotFoundException;
import com.marketplace.product.repositories.CategoryRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class CategoryMapper {

    private final CategoryRepository categoryRepository;

    public Category toEntity(CategoryRequest categoryRequest) {
        return categoryRepository.findByName(categoryRequest.getName())
                .orElse(new Category(categoryRequest.getName()));
    }

    public Set<Category> toEntity(Set<CategoryRequest> categories) {
        Set<CategoryRequest> safeCopy = new HashSet<>(categories);

        Set<String> categoryNames = safeCopy.stream()
                .map(CategoryRequest::getName)
                .collect(Collectors.toSet());

        List<Category> foundCategories = new LinkedList<>();
        if (!categoryNames.isEmpty()) {
            foundCategories = categoryRepository.findAllByName(categoryNames);
        }

        if (foundCategories.size() != safeCopy.size()) {
            Set<String> foundNames = foundCategories.stream()
                    .map(Category::getName)
                    .collect(Collectors.toSet());

            Set<String> missingNames = categoryNames.stream()
                    .filter(name -> !foundNames.contains(name))
                    .collect(Collectors.toSet());

            throw new CategoryNotFoundException(missingNames.toString());
        }

        return new HashSet<>(foundCategories);
    }

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getName());
    }

    public Set<CategoryResponse> toResponse(Collection<Category> categories) {
        return categories.stream()
                .map(category -> new CategoryResponse(category.getId(), category.getName()))
                .collect(Collectors.toSet());
    }
}
