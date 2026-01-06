package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.order.OrderState;
import ru.yandex.practicum.dto.order.ProductReturnRequest;

import java.util.List;

public interface OrderService {
    List<OrderDto> getAllByUsername(String username);

    OrderDto create(String username, CreateNewOrderRequest createNewOrderRequest);

    OrderDto returnProducts(ProductReturnRequest productReturnRequest);

    OrderDto setState(String orderId, OrderState state);

    OrderDto calculateTotalPrice(String orderId);

    OrderDto calculateDeliveryPrice(String orderId);
}
