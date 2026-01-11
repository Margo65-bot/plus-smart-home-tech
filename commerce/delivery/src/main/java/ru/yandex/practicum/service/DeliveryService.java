package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.OrderDto;

import java.math.BigDecimal;

public interface DeliveryService {
    DeliveryDto create(DeliveryDto deliveryDto);

    void successfulDeliveryForOrder(String orderId);

    void failedDeliveryForOrder(String orderId);

    void pickedProductsToDelivery(String orderId);

    BigDecimal calculateCost(OrderDto orderDto);
}
