package ru.yandex.practicum.feign;

import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.order.OrderDto;

@FeignClient(name = "order")
public interface OrderClient {
    @PostMapping("/api/v1/order/payment")
    OrderDto successfulPaymentForOrderId(
            @RequestBody String orderId
    ) throws FeignException;

    @PostMapping("/api/v1/order/payment/failed")
    OrderDto failedPaymentForOrderId(
            @RequestBody String orderId
    ) throws FeignException;
}
