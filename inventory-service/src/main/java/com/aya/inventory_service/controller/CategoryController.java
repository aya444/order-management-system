package com.aya.inventory_service.controller;

import com.aya.inventory_service.dto.CategoryDto;
import com.aya.inventory_service.service.CategoryService;
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
@RequestMapping("category")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody @NotNull CategoryDto categoryDto) {
        CategoryDto newCategory = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(newCategory, HttpStatus.OK);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<CategoryDto> editCategory(@PathVariable @NotNull Integer id, @RequestBody @NotNull CategoryDto updatedCategoryDto) {
        CategoryDto updatedCategory = categoryService.updateCategory(id, updatedCategoryDto);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("id") @NotNull Integer id) {
        CategoryDto categoryDto = categoryService.getCategory(id);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @GetMapping("/name/{categoryName}")
    public ResponseEntity<CategoryDto> getCategoryByName(@PathVariable("categoryName") @NotNull String categoryName) {
        CategoryDto categoryDto = categoryService.getCategoryByName(categoryName);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categoryDtoList = categoryService.getAllCategories();
        return new ResponseEntity<>(categoryDtoList, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable @NotNull Integer id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>("Category with id " + id + " was successfully deleted!", HttpStatus.OK);
    }
}
