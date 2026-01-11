package ru.yandex.practicum.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.order.ProductReturnRequest;

import java.util.List;

public interface OrderApi {
    @GetMapping("/api/v1/order")
    @ResponseStatus(HttpStatus.OK)
    List<OrderDto> getAllByUsername(
            @RequestParam(required = true) String username
    );

    @PutMapping("/api/v1/order")
    @ResponseStatus(HttpStatus.OK)
    OrderDto create(
            @RequestParam(required = true) String username,
            @RequestBody @Valid CreateNewOrderRequest createNewOrderRequest
    );

    @PostMapping("/api/v1/order/return")
    @ResponseStatus(HttpStatus.OK)
    OrderDto returnProducts(
            @RequestBody @NotNull ProductReturnRequest productReturnRequest
    );

    @PostMapping("/api/v1/order/payment")
    @ResponseStatus(HttpStatus.OK)
    OrderDto successfulPaymentForOrderId(
            @RequestBody @NotBlank String orderId
    );

    @PostMapping("/api/v1/order/payment/failed")
    @ResponseStatus(HttpStatus.OK)
    OrderDto failedPaymentForOrderId(
            @RequestBody @NotBlank String orderId
    );

    @PostMapping("/api/v1/order/delivery")
    @ResponseStatus(HttpStatus.OK)
    OrderDto successfulOrderDelivery(
            @RequestBody @NotBlank String orderId
    );

    @PostMapping("/api/v1/order/delivery/failed")
    @ResponseStatus(HttpStatus.OK)
    OrderDto failedOrderDelivery(
            @RequestBody @NotBlank String orderId
    );

    @PostMapping("/api/v1/order/completed")
    @ResponseStatus(HttpStatus.OK)
    OrderDto completed(
            @RequestBody @NotBlank String orderId
    );

    @PostMapping("/api/v1/order/calculate/total")
    @ResponseStatus(HttpStatus.OK)
    OrderDto calculateTotalPrice(
            @RequestBody @NotBlank String orderId
    );

    @PostMapping("/api/v1/order/calculate/delivery")
    @ResponseStatus(HttpStatus.OK)
    OrderDto calculateDeliveryPrice(
            @RequestBody @NotBlank String orderId
    );

    @PostMapping("/api/v1/order/assembly")
    @ResponseStatus(HttpStatus.OK)
    OrderDto successfulOrderAssembly(
            @RequestBody @NotBlank String orderId
    );

    @PostMapping("/api/v1/order/assembly/failed")
    @ResponseStatus(HttpStatus.OK)
    OrderDto failedOrderAssembly(
            @RequestBody @NotBlank String orderId
    );
}
