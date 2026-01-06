package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.api.DeliveryApi;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.service.DeliveryService;

import java.math.BigDecimal;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class DeliveryController implements DeliveryApi {
    private final DeliveryService deliveryService;

    @Override
    public DeliveryDto create(DeliveryDto deliveryDto) {
        log.info("Delivery: Метод create() вызван для заказа '{}'", deliveryDto.orderId());
        return deliveryService.create(deliveryDto);
    }

    @Override
    public void successfulDeliveryForOrder(String orderId) {
        orderId = orderId.replaceAll("\"", "");
        log.info("Delivery: Метод successfulDeliveryForOrder() вызван для заказа '{}'", orderId);
        deliveryService.successfulDeliveryForOrder(orderId);
    }

    @Override
    public void failedDeliveryForOrder(String orderId) {
        orderId = orderId.replaceAll("\"", "");
        log.info("Delivery: Метод failedDeliveryForOrder() вызван для заказа '{}'", orderId);
        deliveryService.failedDeliveryForOrder(orderId);
    }

    @Override
    public void pickedProductsToDelivery(String orderId) {
        orderId = orderId.replaceAll("\"", "");
        log.info("Delivery: Метод pickedProductsToDelivery() вызван для заказа '{}'", orderId);
        deliveryService.pickedProductsToDelivery(orderId);
    }

    @Override
    public BigDecimal calculateCost(OrderDto orderDto) {
        log.info("Delivery: Метод calculateCost() вызван для заказа '{}'", orderDto.orderId());
        return deliveryService.calculateCost(orderDto);
    }

}