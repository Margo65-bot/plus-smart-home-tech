package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.api.ShoppingStoreApi;
import ru.yandex.practicum.dto.shopping_store.PageableDto;
import ru.yandex.practicum.dto.shopping_store.ProductCategory;
import ru.yandex.practicum.dto.shopping_store.ProductCollectionDto;
import ru.yandex.practicum.dto.shopping_store.ProductDto;
import ru.yandex.practicum.dto.shopping_store.QuantityState;
import ru.yandex.practicum.service.ProductService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class ShoppingStoreController implements ShoppingStoreApi {
    private final ProductService productService;

    @Override
    public ProductDto create(ProductDto productDto) {
        log.info("Shopping Store: Запущен метод create() productDto={}", productDto);
        return productService.create(productDto);
    }

    @Override
    public ProductDto update(ProductDto productDto) {
        log.info("Shopping Store: Запущен метод update() productDto={}", productDto);
        return productService.update(productDto);
    }

    @Override
    public String remove(String productId) {
        log.info("Shopping Store: Запущен метод remove() productId={}", productId);
        return productService.remove(productId);
    }

    @Override
    public String setQuantityState(String productId, QuantityState quantityState) {
        log.info("Shopping Store: Запущен метод setQuantityState() productId={}, quantityState={}",
                productId, quantityState);
        return productService.setQuantityState(productId, quantityState);
    }

    @Override
    public ProductDto getById(String productId) {
        log.info("Shopping Store: Запущен метод getById() productId={}", productId);
        return productService.getById(productId);
    }

    @Override
    public ProductCollectionDto getCollection(ProductCategory category, Integer page, Integer size, String sort) {
        log.info("Shopping Store: Запущен метод getCollection() category={}, page={}, size={}, sort={}",
                category, page, size, sort);
        return productService.getCollection(category, new PageableDto(page, size, sort));
    }
}