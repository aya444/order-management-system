package com.aya.inventory_service.util;

import com.aya.inventory_service.dto.ProductDto;
import com.aya.inventory_service.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product fromDtoToEntity(ProductDto productDto);

    @Mapping(target = "categoryName", source = "category.name")
    ProductDto fromEntityToDto(Product product);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(@MappingTarget Product product, ProductDto dto);
}
