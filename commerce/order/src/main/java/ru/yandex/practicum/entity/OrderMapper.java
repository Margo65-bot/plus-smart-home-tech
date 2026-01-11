package ru.yandex.practicum.entity;

import ru.yandex.practicum.dto.order.OrderDto;

public class OrderMapper {
    public static OrderDto mapToDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getShoppingCartId(),
                order.getProducts(),
                order.getPaymentId(),
                order.getDeliveryId(),
                order.getState(),
                order.getDeliveryWeight(),
                order.getDeliveryVolume(),
                order.getFragile(),
                order.getTotalPrice(),
                order.getDeliveryPrice(),
                order.getProductPrice()
        );
    }
}
