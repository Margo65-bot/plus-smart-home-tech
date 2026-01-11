package ru.yandex.practicum.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.OrderDto;

import java.math.BigDecimal;

public interface DeliveryApi {
    @PutMapping("/api/v1/delivery")
    @ResponseStatus(HttpStatus.OK)
    DeliveryDto create(
            @RequestBody @Valid DeliveryDto deliveryDto
    );

    @PostMapping("/api/v1/delivery/successful")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void successfulDeliveryForOrder(
            @RequestBody @NotBlank String orderId
    );

    @PostMapping("/api/v1/delivery/failed")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void failedDeliveryForOrder(
            @RequestBody @NotBlank String orderId
    );

    @PostMapping("/api/v1/delivery/picked")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void pickedProductsToDelivery(
            @RequestBody @NotBlank String orderId
    );

    @PostMapping("/api/v1/delivery/cost")
    @ResponseStatus(HttpStatus.OK)
    BigDecimal calculateCost(
            @RequestBody @Valid OrderDto orderDto
    );
}
