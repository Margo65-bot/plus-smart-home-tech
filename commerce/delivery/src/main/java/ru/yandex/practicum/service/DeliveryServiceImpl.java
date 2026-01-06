package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.delivery.DeliveryState;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.entity.Delivery;
import ru.yandex.practicum.entity.DeliveryMapper;
import ru.yandex.practicum.exception.delivery.NoDeliveryFoundException;
import ru.yandex.practicum.feign.OrderClient;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.repository.DeliveryRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    @Override
    @Transactional(readOnly = false)
    public DeliveryDto create(DeliveryDto deliveryDto) {
        Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryDto.deliveryId());
        if (optionalDelivery.isPresent()) {
            return DeliveryMapper.mapToDto(optionalDelivery.get());
        }

        Delivery delivery = deliveryRepository.save(DeliveryMapper.mapToModel(deliveryDto));
        return DeliveryMapper.mapToDto(delivery);
    }

    @Override
    @Transactional(readOnly = false)
    public void successfulDeliveryForOrder(String orderId) {
        Delivery delivery = findByOrderIdOrThrow(orderId);
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        orderClient.successfulOrderDelivery(orderId);
    }

    @Override
    @Transactional(readOnly = false)
    public void failedDeliveryForOrder(String orderId) {
        Delivery delivery = findByOrderIdOrThrow(orderId);
        delivery.setDeliveryState(DeliveryState.FAILED);
        orderClient.failedOrderDelivery(orderId);
    }

    @Override
    @Transactional(readOnly = false)
    public void pickedProductsToDelivery(String orderId) {
        Delivery delivery = findByOrderIdOrThrow(orderId);
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        orderClient.successfulOrderAssembly(orderId);
        warehouseClient.sendToDelivery(
                new ShippedToDeliveryRequest(
                        orderId,
                        delivery.getId()
                )
        );
    }

    @Override
    @Transactional(readOnly = false)
    public BigDecimal calculateCost(OrderDto orderDto) {
        Delivery delivery = findByOrderIdOrThrow(orderDto.orderId());

        BigDecimal baseRate = BigDecimal.valueOf(5);

        String street = delivery.getFromAddress().street();
        String[] parts = street.split("_");
        int warehouseMultiplier = Integer.parseInt(parts[1]);

        BigDecimal cost = baseRate
                .multiply(BigDecimal.valueOf(warehouseMultiplier))
                .add(baseRate);

        if (Boolean.TRUE.equals(orderDto.fragile())) {
            BigDecimal fragileExtra = cost.multiply(BigDecimal.valueOf(0.2));
            cost = cost.add(fragileExtra);
        }

        if (orderDto.deliveryWeight() != null) {
            cost = cost.add(
                    orderDto.deliveryWeight()
                            .multiply(BigDecimal.valueOf(0.3))
            );
        }

        if (orderDto.deliveryVolume() != null) {
            cost = cost.add(
                    orderDto.deliveryVolume()
                            .multiply(BigDecimal.valueOf(0.2))
            );
        }

        if (delivery.getFromAddress().street() != null && delivery.getFromAddress().street().equalsIgnoreCase(delivery.getToAddress().street())) {
            BigDecimal addressExtra = cost.multiply(BigDecimal.valueOf(0.2));
            cost = cost.add(addressExtra);
        }

        return cost;
    }

    private Delivery findByOrderIdOrThrow(String orderId) {
        return deliveryRepository.findByOrderId(orderId).orElseThrow(
                () -> new NoDeliveryFoundException("Для заказа с id=" + orderId + " доставки не найдены")
        );
    }
}
