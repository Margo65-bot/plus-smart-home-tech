package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;

import java.math.BigDecimal;

public interface PaymentService {
    PaymentDto makePayment(OrderDto orderDto);

    BigDecimal calculateTotalCost(OrderDto orderDto);

    void successfulPayment(String orderId);

    BigDecimal calculateProductCost(OrderDto orderDto);

    void failedPayment(String orderId);
}
