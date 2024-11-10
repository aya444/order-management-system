package com.aya.inventory_service.util;

import com.aya.inventory_service.dto.CategoryDto;
import com.aya.inventory_service.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto fromEntityToDto(Category category);

    Category fromDtoToEntity(CategoryDto categoryDto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(@MappingTarget Category category, CategoryDto dto);
}
