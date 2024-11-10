package com.aya.inventory_service.controller;

import com.aya.inventory_service.dto.ProductDto;
import com.aya.inventory_service.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> addProduct(@RequestBody @Valid ProductDto productDto) {
        productService.createProduct(productDto);
        return new ResponseEntity<>("Product has been created", HttpStatus.OK);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editProduct(@PathVariable @NotNull Integer id, @RequestBody @Valid ProductDto updatedProductDto) {
        productService.updateProduct(id, updatedProductDto);
        return new ResponseEntity<>("Product has been updated", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") @NotNull Integer id) {
        ProductDto productDto = productService.getProduct(id);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> productDtoList = productService.getAllProduct();
        return new ResponseEntity<>(productDtoList, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable @NotNull Integer id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>("Product with id " + id + " was successfully deleted!", HttpStatus.OK);
    }

}
