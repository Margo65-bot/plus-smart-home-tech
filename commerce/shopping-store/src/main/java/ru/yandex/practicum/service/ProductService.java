package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.shopping_store.PageableDto;
import ru.yandex.practicum.dto.shopping_store.ProductCategory;
import ru.yandex.practicum.dto.shopping_store.ProductCollectionDto;
import ru.yandex.practicum.dto.shopping_store.ProductDto;
import ru.yandex.practicum.dto.shopping_store.QuantityState;

public interface ProductService {
    ProductDto create(ProductDto productDto);

    ProductDto update(ProductDto productDto);

    ProductDto getById(String productId);

    ProductCollectionDto getCollection(ProductCategory category, PageableDto pageable);

    String remove(String productId);

    String setQuantityState(String productId, QuantityState quantityState);
}
