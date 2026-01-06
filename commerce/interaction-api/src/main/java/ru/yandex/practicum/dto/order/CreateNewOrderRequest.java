package ru.yandex.practicum.dto.order;

import jakarta.validation.constraints.NotNull;
import ru.yandex.practicum.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddressDto;

public record CreateNewOrderRequest(
        @NotNull(message = "Корзина для заказа не должна быть null")
        ShoppingCartDto shoppingCart,

        @NotNull(message = "Адрес для заказа не должен быть null")
        AddressDto deliveryAddress
) {
}
