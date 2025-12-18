package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.api.WarehouseApi;
import ru.yandex.practicum.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.service.WarehouseService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class WarehouseController implements WarehouseApi {
    private final WarehouseService warehouseService;

    @Override
    public void createProduct(NewProductInWarehouseRequest newProductInWarehouseRequest) {
        log.info("Warehouse: Запущен метод createProduct() newProductInWarehouseRequest={}",
                newProductInWarehouseRequest);
        warehouseService.createProduct(newProductInWarehouseRequest);
    }

    @Override
    public BookedProductsDto checkShoppingCart(ShoppingCartDto shoppingCartDto) {
        log.info("Warehouse: Запущен метод checkShoppingCart() shoppingCartDto={}",
                shoppingCartDto);
        return warehouseService.checkShoppingCart(shoppingCartDto);
    }

    @Override
    public void changeQuantity(AddProductToWarehouseRequest addProductToWarehouseRequest) {
        log.info("Warehouse: Запущен метод changeQuantity() addProductToWarehouseRequest={}",
                addProductToWarehouseRequest);
        warehouseService.changeQuantity(addProductToWarehouseRequest);
    }

    @Override
    public AddressDto getAddress() {
        log.info("Warehouse: Запущен метод getAddress()");
        return warehouseService.getAddress();
    }
}