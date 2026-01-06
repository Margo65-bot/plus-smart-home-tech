package ru.yandex.practicum.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Map;

public record OrderDto(
        @NotBlank(message = "Id заказа не должен быть пустым")
        String orderId,

        @NotBlank(message = "Id корзины не должен быть пустым")
        String shoppingCartId,

        @NotNull(message = "Список товаров не должен быть null")
        @NotEmpty(message = "Список товаров не должен быть пустым")
        Map<String, Long> products,

        String paymentId,

        String deliveryId,

        OrderState state,

        BigDecimal deliveryWeight,

        BigDecimal deliveryVolume,

        Boolean fragile,

        BigDecimal totalPrice,

        BigDecimal deliveryPrice,

        BigDecimal productPrice
) {
}
