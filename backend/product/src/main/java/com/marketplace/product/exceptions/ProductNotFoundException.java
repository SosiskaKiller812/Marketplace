package com.marketplace.product.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(long id) {
        super("Product with id: " + id + " not found");
    }

    public ProductNotFoundException(String name) {
        super("User  with name: " + name + " not found");
    }
}
