package com.aya.inventory_service.controller;

import com.aya.inventory_service.dto.ProductDto;
import com.aya.inventory_service.service.InventoryService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{catName}")
    public ResponseEntity<List<ProductDto>> getProductByCategoryName(@PathVariable("catName") @NotNull String catName) {
        List<ProductDto> productDto = inventoryService.getProductByCategoryName(catName);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @PatchMapping("deduce/{id}")
    public ResponseEntity<String> deductProductFromStock(@PathVariable("id") @NotNull Integer id, @RequestBody @NotNull Integer quantity) {
        inventoryService.deductStock(id, quantity);
        return new ResponseEntity<>("Product deducted from stock", HttpStatus.OK);
    }

}
