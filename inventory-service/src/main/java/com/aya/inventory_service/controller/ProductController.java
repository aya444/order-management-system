package com.aya.inventory_service.controller;

import com.aya.inventory_service.dto.ProductDto;
import com.aya.inventory_service.service.ProductService;
import jakarta.validation.Valid;
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
@RequestMapping("product")
public class ProductController {

    private final ProductService productService;

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

    @GetMapping("/name/{id}")
    public ResponseEntity<String> getProductName(@PathVariable("id") @NotNull Integer id) {
        String productName = productService.getProductName(id);
        return new ResponseEntity<>(productName, HttpStatus.OK);
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

    @PostMapping("/get-products")
    public ResponseEntity<List<ProductDto>> getQuestionsById(@RequestBody List<Integer> productIds) {
        List<ProductDto> questions = productService.getProductsById(productIds);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @GetMapping("/price/{id}")
    public ResponseEntity<Double> getProductPriceById(@PathVariable("id") @NotNull Integer id) {
        Double productPrice = productService.getProductPrice(id);
        return new ResponseEntity<>(productPrice, HttpStatus.OK);
    }

}
