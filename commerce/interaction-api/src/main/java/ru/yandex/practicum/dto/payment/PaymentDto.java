package ru.yandex.practicum.dto.payment;

import java.math.BigDecimal;

public record PaymentDto(
        String paymentId,

        BigDecimal totalPayment,

        BigDecimal deliveryTotal,

        BigDecimal feeTotal
) {
}
