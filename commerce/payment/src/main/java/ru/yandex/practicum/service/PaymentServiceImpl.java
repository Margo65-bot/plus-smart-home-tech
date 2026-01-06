package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;
import ru.yandex.practicum.dto.payment.PaymentState;
import ru.yandex.practicum.dto.shopping_store.ProductDto;
import ru.yandex.practicum.entity.Payment;
import ru.yandex.practicum.entity.PaymentMapper;
import ru.yandex.practicum.exception.payment.NoPaymentFoundException;
import ru.yandex.practicum.exception.payment.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.feign.OrderClient;
import ru.yandex.practicum.feign.ShoppingStoreClient;
import ru.yandex.practicum.repository.PaymentRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final ShoppingStoreClient shoppingStoreClient;

    @Override
    @Transactional(readOnly = false)
    public PaymentDto makePayment(OrderDto orderDto) {
        Payment payment = new Payment();
        payment.setOrderId(orderDto.orderId());
        payment.setTotalPayment(orderDto.totalPrice());
        payment.setDeliveryTotal(orderDto.deliveryPrice());
        payment.setFeeTotal(orderDto.totalPrice().multiply(new BigDecimal("0.1")));
        payment.setPaymentState(PaymentState.PENDING);

        paymentRepository.save(payment);
        return PaymentMapper.mapToDto(payment);
    }

    @Override
    @Transactional(readOnly = false)
    public BigDecimal calculateTotalCost(OrderDto orderDto) {
        if (orderDto.productPrice() == null || orderDto.deliveryPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Не хватает данных для расчета суммы по заказу");
        }

        return orderDto.productPrice()
                .multiply(BigDecimal.valueOf(1.0)
                        .multiply(new BigDecimal("0.1")))
                .add(orderDto.deliveryPrice());
    }

    @Override
    public void successfulPayment(String orderId) {
        Payment payment = findByOrderIdOrThrow(orderId);
        payment.setPaymentState(PaymentState.SUCCESS);
        orderClient.successfulPaymentForOrderId(orderId);
    }

    @Override
    public BigDecimal calculateProductCost(OrderDto orderDto) {
        return orderDto.products().entrySet().stream()
                .map(entry -> {
                    ProductDto productInfo = shoppingStoreClient.getById(entry.getKey());
                    return productInfo.getPrice().multiply(BigDecimal.valueOf(entry.getValue()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void failedPayment(String orderId) {
        Payment payment = findByOrderIdOrThrow(orderId);
        payment.setPaymentState(PaymentState.FAILED);
        orderClient.failedPaymentForOrderId(orderId);
    }

    private Payment findByOrderIdOrThrow(String orderId) {
        return paymentRepository.findByOrderId(orderId).orElseThrow(
                () -> new NoPaymentFoundException("Для заказа с id=" + orderId + " платежи не найдены")
        );
    }
}
