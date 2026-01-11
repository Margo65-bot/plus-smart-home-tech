package ru.yandex.practicum.entity;

import ru.yandex.practicum.dto.delivery.DeliveryDto;

public class DeliveryMapper {
    public static DeliveryDto mapToDto(Delivery delivery) {
        return new DeliveryDto(
                delivery.getId(),
                delivery.getFromAddress(),
                delivery.getToAddress(),
                delivery.getOrderId(),
                delivery.getDeliveryState()
        );
    }

    public static Delivery mapToModel(DeliveryDto deliveryDto) {
        Delivery delivery = new Delivery();
        delivery.setFromAddress(deliveryDto.fromAddress());
        delivery.setToAddress(deliveryDto.toAddress());
        delivery.setOrderId(deliveryDto.orderId());
        delivery.setDeliveryState(deliveryDto.deliveryState());
        return delivery;
    }
}
