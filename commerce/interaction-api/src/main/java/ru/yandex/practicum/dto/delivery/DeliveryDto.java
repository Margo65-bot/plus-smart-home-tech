package ru.yandex.practicum.dto.delivery;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.yandex.practicum.dto.warehouse.AddressDto;

public record DeliveryDto(
        @NotBlank(message = "Id доставки не должен быть пустым")
        String deliveryId,

        @NotNull(message = "Адрес отправки не должен быть null")
        AddressDto fromAddress,

        @NotNull(message = "Адрес доставки не должен быть null")
        AddressDto toAddress,

        @NotBlank(message = "Id заказа не должен быть пустым")
        String orderId,

        DeliveryState deliveryState
) {
}
