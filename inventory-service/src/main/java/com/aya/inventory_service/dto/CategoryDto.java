package com.aya.inventory_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDto {

    private Integer id;
    @NotBlank(message = "Name cannot be blank")
    private String name;
}
