package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.order.OrderState;
import ru.yandex.practicum.dto.order.ProductReturnRequest;
import ru.yandex.practicum.dto.payment.PaymentDto;
import ru.yandex.practicum.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.entity.Order;
import ru.yandex.practicum.entity.OrderMapper;
import ru.yandex.practicum.exception.order.NoOrderFoundException;
import ru.yandex.practicum.feign.DeliveryClient;
import ru.yandex.practicum.feign.PaymentClient;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final DeliveryClient deliveryClient;
    private final PaymentClient paymentClient;
    private final WarehouseClient warehouseClient;
    private final OrderRepository orderRepository;

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getAllByUsername(String username) {
        return orderRepository.findAllByUsername(username).stream()
                .map(OrderMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = false)
    public OrderDto create(String username, CreateNewOrderRequest createNewOrderRequest) {
        Optional<Order> optionalOrder = orderRepository.findByShoppingCartId(createNewOrderRequest.shoppingCart().shoppingCartId());
        if (optionalOrder.isPresent()) {
            return OrderMapper.mapToDto(optionalOrder.get());
        }

        Order order = new Order();
        order.setUsername(username);
        order.setShoppingCartId(createNewOrderRequest.shoppingCart().shoppingCartId());
        order.setProducts(createNewOrderRequest.shoppingCart().products());
        order.setState(OrderState.NEW);

        orderRepository.save(order);

        BookedProductsDto bookedProductsDto = warehouseClient.assemblyProductsForOrder(
                new AssemblyProductsForOrderRequest(
                        order.getId(),
                        order.getProducts()
                )
        );

        order.setDeliveryWeight(bookedProductsDto.deliveryWeight());
        order.setDeliveryVolume(bookedProductsDto.deliveryVolume());
        order.setFragile(bookedProductsDto.fragile());

        DeliveryDto deliveryDto = deliveryClient.create(
                new DeliveryDto(
                        null,
                        warehouseClient.getAddress(),
                        createNewOrderRequest.deliveryAddress(),
                        order.getId(),
                        null
                )
        );

        order.setDeliveryId(deliveryDto.deliveryId());

        PaymentDto paymentDto = paymentClient.makePayment(
                OrderMapper.mapToDto(order)
        );

        order.setPaymentId(paymentDto.paymentId());
        order.setTotalPrice(paymentDto.totalPayment());
        order.setDeliveryPrice(paymentDto.deliveryTotal());
        order.setProductPrice(
                paymentClient.calculateProductCost(
                        OrderMapper.mapToDto(order)
                )
        );
        return OrderMapper.mapToDto(order);
    }

    @Override
    @Transactional(readOnly = false)
    public OrderDto returnProducts(ProductReturnRequest productReturnRequest) {
        Order order = findOrderOrThrow(productReturnRequest.orderId());
        warehouseClient.returnProducts(productReturnRequest.products());
        order.setState(OrderState.PRODUCT_RETURNED);
        return OrderMapper.mapToDto(order);
    }

    @Override
    @Transactional(readOnly = false)
    public OrderDto setState(String orderId, OrderState state) {
        Order order = findOrderOrThrow(orderId);
        order.setState(state);
        return OrderMapper.mapToDto(order);
    }

    @Override
    @Transactional(readOnly = false)
    public OrderDto calculateTotalPrice(String orderId) {
        Order order = findOrderOrThrow(orderId);
        if (order.getTotalPrice() == null) {
            order.setTotalPrice(
                    paymentClient.calculateTotalCost(OrderMapper.mapToDto(order))
            );
        }
        return OrderMapper.mapToDto(order);
    }

    @Override
    @Transactional(readOnly = false)
    public OrderDto calculateDeliveryPrice(String orderId) {
        Order order = findOrderOrThrow(orderId);
        if (order.getDeliveryPrice() == null) {
            order.setDeliveryPrice(
                    deliveryClient.calculateCost(OrderMapper.mapToDto(order))
            );
        }
        return OrderMapper.mapToDto(order);
    }

    private Order findOrderOrThrow(String orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new NoOrderFoundException("Заказ с id=" + orderId + " не найден")
        );
    }
}
