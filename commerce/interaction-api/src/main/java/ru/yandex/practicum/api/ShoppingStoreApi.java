package ru.yandex.practicum.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.dto.shopping_store.ProductCategory;
import ru.yandex.practicum.dto.shopping_store.ProductCollectionDto;
import ru.yandex.practicum.dto.shopping_store.ProductDto;
import ru.yandex.practicum.dto.shopping_store.QuantityState;

public interface ShoppingStoreApi {
    @PutMapping("/api/v1/shopping-store")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto create(
            @RequestBody @Valid ProductDto productDto
    );

    @PostMapping("/api/v1/shopping-store")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto update(
            @RequestBody @Valid ProductDto productDto
    );

    @PostMapping("/api/v1/shopping-store/removeProductFromStore")
    @ResponseStatus(HttpStatus.OK)
    public String remove(
            @RequestBody @NotBlank String productId
    );

    @PostMapping("/api/v1/shopping-store/quantityState")
    @ResponseStatus(HttpStatus.OK)
    public String setQuantityState(
            @RequestParam(required = true) String productId,
            @RequestParam(required = true) QuantityState quantityState
    );

    @GetMapping("/api/v1/shopping-store/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getById(
            @PathVariable String productId
    );

    @GetMapping("/api/v1/shopping-store")
    @ResponseStatus(HttpStatus.OK)
    public ProductCollectionDto getCollection(
            @RequestParam(required = true) ProductCategory category,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size,
            @RequestParam(required = false, defaultValue = "productName,ASC") String sort
    );

}
