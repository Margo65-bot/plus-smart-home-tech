package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.api.ShoppingCartApi;
import ru.yandex.practicum.dto.shopping_cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.service.ShoppingCartService;

import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class ShoppingCartController implements ShoppingCartApi {
    private final ShoppingCartService shoppingCartService;

    @Override
    public ShoppingCartDto addProducts(String username, Map<String, Long> addProductMap) {
        log.info("Shopping Cart: Метод addProducts() вызван для пользователя '{}', добавляется {} товар(ов)",
                maskUsername(username), addProductMap);

        return shoppingCartService.addProducts(username, addProductMap);
    }

    @Override
    public ShoppingCartDto removeProducts(String username, List<String> removeProductList) {
        log.info("Shopping Cart: Метод removeProducts() вызван для пользователя '{}', удаляется {} товар(ов)",
                maskUsername(username), removeProductList);

        return shoppingCartService.removeProducts(username, removeProductList);
    }

    @Override
    public ShoppingCartDto changeProductQuantity(String username,
                                                 ChangeProductQuantityRequest changeProductQuantityRequest) {
        log.info("Shopping Cart: Метод changeProductQuantity() вызван для пользователя '{}', товар: {}",
                maskUsername(username), changeProductQuantityRequest);
        return shoppingCartService.changeProductQuantity(username, changeProductQuantityRequest);
    }

    @Override
    public String deactivate(String username) {
        log.info("Shopping Cart: Метод deactivate() вызван для пользователя '{}'", maskUsername(username));
        return shoppingCartService.deactivate(username);
    }

    @Override
    public ShoppingCartDto getByUsername(String username) {
        log.info("Shopping Cart: Метод getByUsername() вызван для пользователя '{}'", maskUsername(username));
        return shoppingCartService.getByUsername(username);
    }

    private String maskUsername(String username) {
        if (username == null || username.length() <= 2) {
            return username;
        }
        return username.charAt(0) + "***" + username.charAt(username.length() - 1);
    }
}