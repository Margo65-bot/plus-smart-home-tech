package ru.yandex.practicum.entity;

import ru.yandex.practicum.dto.payment.PaymentDto;

public class PaymentMapper {
    public static PaymentDto mapToDto(Payment payment) {
        return new PaymentDto(
                payment.getId(),
                payment.getTotalPayment(),
                payment.getDeliveryTotal(),
                payment.getFeeTotal()
        );
    }
}
