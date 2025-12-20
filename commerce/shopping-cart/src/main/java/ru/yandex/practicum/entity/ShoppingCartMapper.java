package ru.yandex.practicum.entity;

import ru.yandex.practicum.dto.shopping_cart.ShoppingCartDto;

public class ShoppingCartMapper {
    public static ShoppingCartDto mapToDto(ShoppingCart cart) {
        return new ShoppingCartDto(
                cart.getId(),
                cart.getProducts()
        );
    }
}
