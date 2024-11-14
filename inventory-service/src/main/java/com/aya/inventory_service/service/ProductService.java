package com.aya.inventory_service.service;

import com.aya.inventory_service.dto.ProductDto;

import java.util.List;

public interface ProductService {
    void createProduct(ProductDto productDto);

    void updateProduct(Integer id, ProductDto productDto);

    ProductDto getProduct(Integer id);

    Double getProductPrice(Integer id);

    String getProductName(Integer id);

    List<ProductDto> getAllProduct();

    List<ProductDto> getProductsById(List<Integer> productIds);

    void deleteProduct(Integer id);
}
