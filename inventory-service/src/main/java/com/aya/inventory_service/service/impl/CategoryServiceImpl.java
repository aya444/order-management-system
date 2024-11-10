package com.aya.inventory_service.service.impl;

import com.aya.inventory_service.dto.CategoryDto;
import com.aya.inventory_service.entity.Category;
import com.aya.inventory_service.exception.CategoryNotFoundException;
import com.aya.inventory_service.exception.InvalidCategoryDataException;
import com.aya.inventory_service.repository.CategoryRepository;
import com.aya.inventory_service.service.CategoryService;
import com.aya.inventory_service.util.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private static final String CATEGORY_ID_NOT_FOUND_MESSAGE = "Category Id not found!";
    private static final String CATEGORY_DATA_CANNOT_BE_NULL_MESSAGE = "Category data cannot be null!";

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        if (categoryDto == null) {
            throw new InvalidCategoryDataException(CATEGORY_DATA_CANNOT_BE_NULL_MESSAGE);
        }

        Category categoryEntity = categoryMapper.fromDtoToEntity(categoryDto);
        Category savedCategory = categoryRepository.save(categoryEntity);
        return categoryMapper.fromEntityToDto(savedCategory);
    }

    @Override
    public CategoryDto updateCategory(Integer id, CategoryDto categoryDto) {
        if (categoryDto == null) {
            throw new InvalidCategoryDataException(CATEGORY_DATA_CANNOT_BE_NULL_MESSAGE);
        }

        return categoryRepository.findById(id)
                .map(category -> {
                    categoryMapper.updateEntityFromDto(category, categoryDto);
                    Category updatedCategory = categoryRepository.save(category);
                    return categoryMapper.fromEntityToDto(updatedCategory);
                })
                .orElseThrow(() -> new CategoryNotFoundException(CATEGORY_ID_NOT_FOUND_MESSAGE));
    }

    @Override
    public CategoryDto getCategory(Integer id) {
        if (id == null) {
            throw new InvalidCategoryDataException(CATEGORY_DATA_CANNOT_BE_NULL_MESSAGE);
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(CATEGORY_ID_NOT_FOUND_MESSAGE));

        return categoryMapper.fromEntityToDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream()
                .map(categoryMapper::fromEntityToDto)
                .toList();
    }

    @Override
    public CategoryDto getCategoryByName(String categoryName) {
        if (categoryName == null) {
            throw new InvalidCategoryDataException(CATEGORY_DATA_CANNOT_BE_NULL_MESSAGE);
        }
        Category category = categoryRepository.findByName(categoryName);
        return categoryMapper.fromEntityToDto(category);
    }

    @Override
    public void deleteCategory(Integer id) {
        if (id == null) {
            throw new InvalidCategoryDataException(CATEGORY_DATA_CANNOT_BE_NULL_MESSAGE);
        }

        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new CategoryNotFoundException(CATEGORY_ID_NOT_FOUND_MESSAGE);
        }
    }
}
