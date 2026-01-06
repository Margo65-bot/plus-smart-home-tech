package ru.yandex.practicum.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record ProductReturnRequest(
        @NotBlank(message = "Id заказа не должен быть пустым")
        String orderId,

        @NotNull(message = "Список товаров не должен быть null")
        @NotEmpty(message = "Список товаров не должен быть пустым")
        Map<String, Long> products
) {
}
