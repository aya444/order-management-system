package com.aya.notification_service.feign;

import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("INVENTORY-SERVICE")
public interface InventoryClient {
    @GetMapping("product/name/{id}")
    ResponseEntity<String> getProductName(@PathVariable("id") @NotNull Integer id);

    @GetMapping("product/price/{id}")
    ResponseEntity<Double> getProductPriceById(@PathVariable("id") @NotNull Integer id);

    @GetMapping("product/quantity/{id}")
    ResponseEntity<Integer> getProductQuantityById(@PathVariable("id") @NotNull Integer id);
}
