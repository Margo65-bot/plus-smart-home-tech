package ru.yandex.practicum.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;

import java.math.BigDecimal;

public interface PaymentApi {
    @PostMapping("/api/v1/payment")
    @ResponseStatus(HttpStatus.OK)
    PaymentDto makePayment(
            @RequestBody @Valid OrderDto orderDto
    );

    @PostMapping("/api/v1/payment/totalCost")
    @ResponseStatus(HttpStatus.OK)
    BigDecimal calculateTotalCost(
            @RequestBody @Valid OrderDto orderDto
    );

    @PostMapping("/api/v1/payment/refund")
    @ResponseStatus(HttpStatus.OK)
    void successfulPayment(
            @RequestBody @NotBlank String orderId
    );

    @PostMapping("/api/v1/payment/productCost")
    @ResponseStatus(HttpStatus.OK)
    BigDecimal calculateProductCost(
            @RequestBody @Valid OrderDto orderDto
    );

    @PostMapping("/api/v1/payment/failed")
    @ResponseStatus(HttpStatus.OK)
    void failedPayment(
            @RequestBody @NotBlank String orderId
    );
}
