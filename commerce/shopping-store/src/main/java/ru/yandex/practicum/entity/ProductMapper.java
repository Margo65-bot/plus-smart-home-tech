package ru.yandex.practicum.entity;

import ru.yandex.practicum.dto.shopping_store.ProductDto;

public class ProductMapper {
    public static ProductDto mapToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setProductId(product.getId());
        dto.setProductName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setImageSrc(product.getImageSrc());
        dto.setQuantityState(product.getQuantityState());
        dto.setProductState(product.getState());
        dto.setProductCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        return dto;
    }

    public static Product mapToModel(ProductDto dto) {
        Product product = new Product();
        product.setName(dto.getProductName());
        product.setDescription(dto.getDescription());
        product.setImageSrc(dto.getImageSrc());
        product.setQuantityState(dto.getQuantityState());
        product.setState(dto.getProductState());
        product.setCategory(dto.getProductCategory());
        product.setPrice(dto.getPrice());
        return product;
    }
}
