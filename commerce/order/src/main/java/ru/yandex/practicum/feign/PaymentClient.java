package ru.yandex.practicum.feign;

import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;

import java.math.BigDecimal;

@FeignClient(name = "payment")
public interface PaymentClient {
    @PostMapping("/api/v1/payment/productCost")
    BigDecimal calculateProductCost(
            @RequestBody OrderDto orderDto
    ) throws FeignException;

    @PostMapping("/api/v1/payment/totalCost")
    BigDecimal calculateTotalCost(
            @RequestBody OrderDto orderDto
    ) throws FeignException;

    @PostMapping("/api/v1/payment")
    PaymentDto makePayment(
            @RequestBody OrderDto orderDto
    ) throws FeignException;
}
