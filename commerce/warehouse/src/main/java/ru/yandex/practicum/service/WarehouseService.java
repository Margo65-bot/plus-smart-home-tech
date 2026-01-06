package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.ShippedToDeliveryRequest;

import java.util.Map;

public interface WarehouseService {
    void createProduct(NewProductInWarehouseRequest newProductInWarehouseRequest);

    BookedProductsDto checkShoppingCart(ShoppingCartDto shoppingCartDto);

    void changeQuantity(AddProductToWarehouseRequest addProductToWarehouseRequest);

    AddressDto getAddress();

    void sendToDelivery(ShippedToDeliveryRequest shippedToDeliveryRequest);

    void returnProducts(Map<String, Long> products);

    BookedProductsDto assemblyProductsForOrder(AssemblyProductsForOrderRequest assemblyProductsForOrderRequest);
}
