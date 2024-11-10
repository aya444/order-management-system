package com.aya.inventory_service.service;

import com.aya.inventory_service.dto.ProductDto;

import java.util.List;

public interface InventoryService {
    List<ProductDto> getProductByCategoryName(String categoryName);

    void deductStock(Integer productId, int quantity);
}