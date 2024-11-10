package com.aya.inventory_service.service;

import com.aya.inventory_service.dto.ProductDto;

import java.util.List;

public interface ProductService {
    void createProduct(ProductDto productDto);

    void updateProduct(Integer id, ProductDto productDto);

    ProductDto getProduct(Integer id);

    List<ProductDto> getAllProduct();

    void deleteProduct(Integer id);
}
