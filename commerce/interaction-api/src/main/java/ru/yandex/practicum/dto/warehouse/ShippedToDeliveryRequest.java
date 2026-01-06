package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.NotBlank;

public record ShippedToDeliveryRequest(
        @NotBlank(message = "Id заказа не должен быть пустым")
        String orderId,

        @NotBlank(message = "Id доставки не должен быть пустым")
        String deliveryId
) {
}
