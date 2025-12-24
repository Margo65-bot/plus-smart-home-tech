package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.shopping_cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.shopping_cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;

public interface ShoppingCartService {
    ShoppingCartDto addProducts(String username, Map<String, Long> addProductMap);

    ShoppingCartDto removeProducts(String username, List<String> removeProductList);

    ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest changeProductQuantityRequest);

    String deactivate(String username);

    ShoppingCartDto getByUsername(String username);
}
