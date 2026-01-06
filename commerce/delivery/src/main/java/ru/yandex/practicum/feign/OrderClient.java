package ru.yandex.practicum.feign;

import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.order.OrderDto;

@FeignClient(name = "order")
public interface OrderClient {
    @PostMapping("/api/v1/order/delivery")
    OrderDto successfulOrderDelivery(
            @RequestBody String orderId
    ) throws FeignException;

    @PostMapping("/api/v1/order/delivery/failed")
    OrderDto failedOrderDelivery(
            @RequestBody String orderId
    ) throws FeignException;

    @PostMapping("/api/v1/order/assembly")
    OrderDto successfulOrderAssembly(
            @RequestBody String orderId
    ) throws FeignException;
}
