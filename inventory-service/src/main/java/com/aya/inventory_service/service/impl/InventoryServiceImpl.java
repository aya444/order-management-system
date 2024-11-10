package com.aya.inventory_service.service.impl;

import com.aya.inventory_service.dto.ProductDto;
import com.aya.inventory_service.entity.Product;
import com.aya.inventory_service.exception.InvalidCategoryDataException;
import com.aya.inventory_service.exception.InvalidProductDataException;
import com.aya.inventory_service.exception.ProductNotFoundException;
import com.aya.inventory_service.repository.InventoryRepository;
import com.aya.inventory_service.repository.ProductRepository;
import com.aya.inventory_service.service.InventoryService;
import com.aya.inventory_service.util.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository, ProductRepository productRepository, ProductMapper productMapper) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public List<ProductDto> getProductByCategoryName(String categoryName) {
        if (categoryName == null) {
            throw new InvalidCategoryDataException("Category name cannot be null!");
        }

        List<Product> products = inventoryRepository.findProductsByCategory(categoryName);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("Product not found!");
        }

        return products.stream().map(productMapper::fromEntityToDto).toList();
    }

    @Override
    public void deductStock(Integer productId, int quantity) {
        if (productId == null) {
            throw new InvalidProductDataException("Product ID cannot be null!");
        }
        if (quantity <= 0) {
            throw new InvalidProductDataException("Quantity must be greater than zero!");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product cannot be found with ID: " + productId));

        int updatedQuantity = product.getQuantity() - quantity;
        if (updatedQuantity < 0) {
            throw new InvalidProductDataException("Insufficient stock for product ID: " + productId);
        }

        product.setQuantity(updatedQuantity);
        productRepository.save(product);
    }
}
