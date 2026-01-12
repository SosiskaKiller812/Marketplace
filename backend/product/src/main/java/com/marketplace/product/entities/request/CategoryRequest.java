package com.marketplace.product.entities.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;



@AllArgsConstructor
@Data
public class CategoryRequest{
    @Size(min = 2, max = 40, message = "Size must be between 2 and 40")
    @NotBlank(message = "name is required")
    String name;
}
