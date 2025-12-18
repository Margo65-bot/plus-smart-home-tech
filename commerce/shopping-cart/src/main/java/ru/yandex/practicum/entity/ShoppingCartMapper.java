package ru.yandex.practicum.entity;

import ru.yandex.practicum.dto.shopping_cart.ShoppingCartDto;

public class ShoppingCartMapper {
    public static ShoppingCartDto mapToDto(ShoppingCart cart) {
        ShoppingCartDto dto = new ShoppingCartDto();
        dto.setShoppingCartId(cart.getId());
        dto.setProducts(cart.getProducts());
        return dto;
    }
}
