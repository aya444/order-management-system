package com.aya.inventory_service.controller;

import com.aya.inventory_service.dto.ProductDto;
import com.aya.inventory_service.service.InventoryService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("/{catName}")
    public ResponseEntity<List<ProductDto>> getProductByCategoryName(@PathVariable("catName") @NotNull String catName) {
        List<ProductDto> productDto = inventoryService.getProductByCategoryName(catName);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @PutMapping("deduce/{id}")
    public ResponseEntity<String> deductProductFromStock(@PathVariable("id") @NotNull Integer id, @RequestParam @NotNull Integer quantity) {
        inventoryService.deductStock(id, quantity);
        return new ResponseEntity<>("Product deducted from stock", HttpStatus.OK);
    }

}
