package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.api.PaymentApi;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;
import ru.yandex.practicum.service.PaymentService;

import java.math.BigDecimal;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentApi {
    private final PaymentService paymentService;

    @Override
    public PaymentDto makePayment(OrderDto orderDto) {
        log.info("Payment: Метод makePayment() вызван для заказа '{}'", orderDto.orderId());
        return paymentService.makePayment(orderDto);
    }

    @Override
    public BigDecimal calculateTotalCost(OrderDto orderDto) {
        log.info("Payment: Метод calculateTotalCost() вызван для заказа '{}'", orderDto.orderId());
        return paymentService.calculateTotalCost(orderDto);
    }

    @Override
    public void successfulPayment(String orderId) {
        orderId = orderId.replaceAll("\"", "");
        log.info("Payment: Метод successfulPayment() вызван для заказа '{}'", orderId);
        paymentService.successfulPayment(orderId);
    }

    @Override
    public BigDecimal calculateProductCost(OrderDto orderDto) {
        log.info("Payment: Метод calculateProductCost() вызван для заказа '{}'", orderDto.orderId());
        return paymentService.calculateProductCost(orderDto);
    }

    @Override
    public void failedPayment(String orderId) {
        orderId = orderId.replaceAll("\"", "");
        log.info("Payment: Метод failedPayment() вызван для заказа '{}'", orderId);
        paymentService.failedPayment(orderId);
    }

}