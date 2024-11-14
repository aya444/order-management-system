package com.aya.inventory_service.service.impl;

import com.aya.inventory_service.dto.ProductDto;
import com.aya.inventory_service.entity.Category;
import com.aya.inventory_service.entity.Product;
import com.aya.inventory_service.exception.InvalidProductDataException;
import com.aya.inventory_service.exception.ProductNotFoundException;
import com.aya.inventory_service.repository.CategoryRepository;
import com.aya.inventory_service.repository.ProductRepository;
import com.aya.inventory_service.service.ProductService;
import com.aya.inventory_service.util.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private static final String PRODUCT_ID_NOT_FOUND_MESSAGE = "Product with this Id not found!";
    private static final String PRODUCT_ID_CANNOT_BE_NULL = "Product Id cannot be null!";
    private static final String PRODUCT_DATA_CANNOT_BE_NULL_MESSAGE = "Product data cannot be null!";

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void createProduct(ProductDto productDto) {
        if (productDto == null) {
            throw new InvalidProductDataException(PRODUCT_DATA_CANNOT_BE_NULL_MESSAGE);
        }

        Category category = categoryRepository.findByName(productDto.getCategoryName());
        Product productEntity = productMapper.fromDtoToEntity(productDto);
        productEntity.setCategory(category);
        productRepository.save(productEntity);
    }

    // TODO : updateProduct cannot update category name of the product
    @Override
    public void updateProduct(Integer id, ProductDto productDto) {
        if (productDto == null) {
            throw new InvalidProductDataException(PRODUCT_DATA_CANNOT_BE_NULL_MESSAGE);
        }
        if (id == null) {
            throw new InvalidProductDataException(PRODUCT_ID_CANNOT_BE_NULL);
        }

        productRepository.findById(id).ifPresentOrElse(
                product -> {
                    productMapper.updateEntityFromDto(product, productDto);
                    productRepository.save(product);
                },
                () -> {
                    throw new ProductNotFoundException(PRODUCT_ID_NOT_FOUND_MESSAGE);
                }
        );
    }

    @Override
    public ProductDto getProduct(Integer id) {
        if (id == null) {
            throw new InvalidProductDataException(PRODUCT_ID_CANNOT_BE_NULL);
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_ID_NOT_FOUND_MESSAGE));
        return productMapper.fromEntityToDto(product);
    }

    @Override
    public Double getProductPrice(Integer id) {
        if (id == null) {
            throw new InvalidProductDataException(PRODUCT_ID_CANNOT_BE_NULL);
        }

        return getProduct(id).getPrice();
    }

    @Override
    public String getProductName(Integer id) {
        if (id == null) {
            throw new InvalidProductDataException(PRODUCT_ID_CANNOT_BE_NULL);
        }

        return getProduct(id).getName();
    }

    @Override
    public List<ProductDto> getAllProduct() {
        List<Product> productList = productRepository.findAll();
        return productList.stream()
                .map(productMapper::fromEntityToDto)
                .toList();
    }

    @Override
    public void deleteProduct(Integer id) {
        if (id == null) {
            throw new InvalidProductDataException(PRODUCT_ID_CANNOT_BE_NULL);
        }

        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new ProductNotFoundException(PRODUCT_ID_NOT_FOUND_MESSAGE);
        }
    }

    @Override
    public List<ProductDto> getProductsById(List<Integer> productIds) {
        return productIds.stream()
                .map(id -> productRepository.findById(id)
                        .orElseThrow(() -> new ProductNotFoundException("Product cannot be found with ID: " + id)))
                .map(productMapper::fromEntityToDto)
                .toList();
    }
}
