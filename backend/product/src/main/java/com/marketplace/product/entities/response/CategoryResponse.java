package com.marketplace.product.entities.response;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    private int id;

    @Size(min = 2, max = 40, message = "Size must be between 2 and 40")
    @NotBlank(message = "name is required")
    private String name;

    public CategoryResponse(String name){
        this.name = name;
    }
}
