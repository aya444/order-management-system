package com.aya.inventory_service.service;

import com.aya.inventory_service.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(Integer id, CategoryDto categoryDto);

    CategoryDto getCategory(Integer id);

    List<CategoryDto> getAllCategories();

    CategoryDto getCategoryByName(String categoryName);

    void deleteCategory(Integer id);
}
