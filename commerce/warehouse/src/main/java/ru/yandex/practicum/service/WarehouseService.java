package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;

public interface WarehouseService {
    void createProduct(NewProductInWarehouseRequest newProductInWarehouseRequest);

    BookedProductsDto checkShoppingCart(ShoppingCartDto shoppingCartDto);

    void changeQuantity(AddProductToWarehouseRequest addProductToWarehouseRequest);

    AddressDto getAddress();
}
