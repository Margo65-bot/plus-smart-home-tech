package ru.yandex.practicum.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.dto.order.OrderState;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, length = 50)
    private String id;

    @Column(name = "shopping_cart_id", nullable = false, length = 50)
    private String shoppingCartId;

    @Column(name = "delivery_id", nullable = false, length = 50)
    private String deliveryId;

    @Column(name = "payment_id", nullable = false, length = 50)
    private String paymentId;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_state", nullable = false, length = 30)
    private OrderState state;

    @Column(name = "delivery_weight", nullable = true)
    private BigDecimal deliveryWeight;

    @Column(name = "delivery_volume", nullable = true)
    private BigDecimal deliveryVolume;

    @Column(name = "fragile", nullable = true)
    private Boolean fragile;

    @Column(name = "total_price", nullable = true)
    private BigDecimal totalPrice;

    @Column(name = "delivery_price", nullable = true)
    private BigDecimal deliveryPrice;

    @Column(name = "product_price", nullable = true)
    private BigDecimal productPrice;

    @ToString.Exclude
    @ElementCollection
    @CollectionTable(name = "order_products", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id", nullable = false, length = 50)
    @Column(name = "quantity", nullable = false)
    private Map<String, Long> products = new HashMap<>();
}
