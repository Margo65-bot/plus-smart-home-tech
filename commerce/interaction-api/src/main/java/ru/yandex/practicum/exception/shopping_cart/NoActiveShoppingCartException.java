package ru.yandex.practicum.exception.shopping_cart;

public class NoActiveShoppingCartException extends RuntimeException {
    public NoActiveShoppingCartException(String message) {
        super(message);
    }
}
