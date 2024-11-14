package com.aya.order_service.feign;


import com.aya.order_service.dto.ProductDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("INVENTORY-SERVICE")
public interface InventoryClient {
    @PutMapping("inventory/deduce/{id}")
    ResponseEntity<String> deductProductFromStock(@PathVariable("id") Integer id, @RequestParam Integer quantity);

    @GetMapping("product/{id}")
    ResponseEntity<ProductDto> getProductById(@PathVariable("id") Integer id);

    @PostMapping("product/get-products")
    public ResponseEntity<List<ProductDto>> getQuestionsById(@RequestBody List<Integer> productIds);

    @GetMapping("product/price/{id}")
    public ResponseEntity<Double> getProductPriceById(@PathVariable("id") @NotNull Integer id);
}

