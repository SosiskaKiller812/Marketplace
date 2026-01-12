package com.marketplace.product.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.product.entities.request.CategoryRequest;
import com.marketplace.product.entities.request.ProductRequest;
import com.marketplace.product.entities.response.ProductResponse;
import com.marketplace.product.services.implementations.ProductServiceImpl;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Data
@AllArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductAPI {

    private final ProductServiceImpl productService;

    @GetMapping("")
    public List<ProductResponse> getAll() {
        return productService.getAll();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        ProductResponse product = productService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    //FIX
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getByCategory(@PathVariable CategoryRequest category) {
        List<ProductResponse> list = productService.getAllWithCategory(category);
        return (ResponseEntity<?>) list;
    }

    @PostMapping("/new")
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        ProductResponse newProduct = productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }
}
