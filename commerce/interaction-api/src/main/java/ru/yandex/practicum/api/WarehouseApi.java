package ru.yandex.practicum.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;

public interface WarehouseApi {
    @PutMapping("/api/v1/warehouse")
    @ResponseStatus(HttpStatus.OK)
    void createProduct(
            @RequestBody @Valid NewProductInWarehouseRequest newProductInWarehouseRequest
    );

    @PostMapping("/api/v1/warehouse/check")
    @ResponseStatus(HttpStatus.OK)
    BookedProductsDto checkShoppingCart(
            @RequestBody @Valid ShoppingCartDto shoppingCartDto
    );

    @PostMapping("/api/v1/warehouse/add")
    @ResponseStatus(HttpStatus.OK)
    void changeQuantity(
            @RequestBody @Valid AddProductToWarehouseRequest addProductToWarehouseRequest
    );

    @GetMapping("/api/v1/warehouse/address")
    @ResponseStatus(HttpStatus.OK)
    AddressDto getAddress();
}
