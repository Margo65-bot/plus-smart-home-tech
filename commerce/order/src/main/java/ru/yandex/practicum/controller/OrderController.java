package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.api.OrderApi;
import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.order.OrderState;
import ru.yandex.practicum.dto.order.ProductReturnRequest;
import ru.yandex.practicum.service.OrderService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class OrderController implements OrderApi {
    private final OrderService orderService;

    @Override
    public List<OrderDto> getAllByUsername(String username) {
        log.info("Order: Метод getAllByUsername() вызван для пользователя '{}'", maskUsername(username));
        return orderService.getAllByUsername(username);
    }

    @Override
    public OrderDto create(String username, CreateNewOrderRequest createNewOrderRequest) {
        log.info("Order: Метод create() вызван для пользователя '{}'", maskUsername(username));
        return orderService.create(username, createNewOrderRequest);
    }

    @Override
    public OrderDto returnProducts(ProductReturnRequest productReturnRequest) {
        log.info("Order: Метод returnProducts() вызван для заказа '{}'", productReturnRequest.orderId());
        return orderService.returnProducts(productReturnRequest);
    }

    @Override
    public OrderDto successfulPaymentForOrderId(String orderId) {
        orderId = orderId.replaceAll("\"", "");
        log.info("Order: Метод successfulPaymentForOrderId() вызван для заказа '{}'", orderId);
        return orderService.setState(orderId, OrderState.PAID);
    }

    @Override
    public OrderDto failedPaymentForOrderId(String orderId) {
        orderId = orderId.replaceAll("\"", "");
        log.info("Order: Метод failedPaymentForOrderId() вызван для заказа '{}'", orderId);
        return orderService.setState(orderId, OrderState.PAYMENT_FAILED);
    }

    @Override
    public OrderDto successfulOrderDelivery(String orderId) {
        orderId = orderId.replaceAll("\"", "");
        log.info("Order: Метод successfulOrderDelivery() вызван для заказа '{}'", orderId);
        return orderService.setState(orderId, OrderState.DELIVERED);
    }

    @Override
    public OrderDto failedOrderDelivery(String orderId) {
        orderId = orderId.replaceAll("\"", "");
        log.info("Order: Метод failedOrderDelivery() вызван для заказа '{}'", orderId);
        return orderService.setState(orderId, OrderState.DELIVERY_FAILED);
    }

    @Override
    public OrderDto completed(String orderId) {
        orderId = orderId.replaceAll("\"", "");
        log.info("Order: Метод completed() вызван для заказа '{}'", orderId);
        return orderService.setState(orderId, OrderState.COMPLETED);
    }

    @Override
    public OrderDto calculateTotalPrice(String orderId) {
        orderId = orderId.replaceAll("\"", "");
        log.info("Order: Метод calculateTotalPrice() вызван для заказа '{}'", orderId);
        return orderService.calculateTotalPrice(orderId);
    }

    @Override
    public OrderDto calculateDeliveryPrice(String orderId) {
        orderId = orderId.replaceAll("\"", "");
        log.info("Order: Метод calculateDeliveryPrice() вызван для заказа '{}'", orderId);
        return orderService.calculateDeliveryPrice(orderId);
    }

    @Override
    public OrderDto successfulOrderAssembly(String orderId) {
        orderId = orderId.replaceAll("\"", "");
        log.info("Order: Метод successfulOrderAssembly() вызван для заказа '{}'", orderId);
        return orderService.setState(orderId, OrderState.ASSEMBLED);
    }

    @Override
    public OrderDto failedOrderAssembly(String orderId) {
        orderId = orderId.replaceAll("\"", "");
        log.info("Order: Метод failedOrderAssembly() вызван для заказа '{}'", orderId);
        return orderService.setState(orderId, OrderState.ASSEMBLY_FAILED);
    }

    private String maskUsername(String username) {
        if (username == null || username.length() <= 2) {
            return username;
        }
        return username.charAt(0) + "***" + username.charAt(username.length() - 1);
    }
}