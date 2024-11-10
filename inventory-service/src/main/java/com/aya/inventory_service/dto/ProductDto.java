package com.aya.inventory_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Integer id;
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotBlank(message = "Description cannot be blank")
    private String description;
    @NotNull(message = "Quantity cannot be blank")
    private Integer quantity;
    @NotNull(message = "Price cannot be blank")
    private Double price;
    @NotBlank(message = "CategoryId cannot be blank")
    private String categoryName;
}
