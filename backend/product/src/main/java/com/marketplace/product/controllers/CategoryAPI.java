package com.marketplace.product.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.product.entities.request.CategoryRequest;
import com.marketplace.product.entities.response.CategoryResponse;
import com.marketplace.product.entities.response.MessageResponse;
import com.marketplace.product.services.implementations.CategoryServiceImpl;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@Data
@AllArgsConstructor
@RequestMapping("/api/categories")
public class CategoryAPI {

    private final CategoryServiceImpl categoryService;
    
    @PostMapping("/new")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest) {
        categoryService.addCategory(categoryRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Category created successfully"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCategory(@RequestBody CategoryRequest categoryRequest) {
        categoryService.deleteCategory(categoryRequest);

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Deleted successfully"));
    }

    @GetMapping("")
    public List<CategoryResponse> getAll() {
        return categoryService.getAll();
    }

}
