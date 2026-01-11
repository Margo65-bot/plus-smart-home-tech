package ru.yandex.practicum.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@Entity
@Table(name = "order_bookings")
public class OrderBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, length = 50)
    private String id;

    @Column(name = "order_id", nullable = false, length = 50)
    private String orderId;

    @Column(name = "delivery_id", nullable = true, length = 50)
    private String deliveryId;

    @Column(name = "delivery_weight", nullable = false)
    private BigDecimal deliveryWeight;

    @Column(name = "delivery_volume", nullable = false)
    private BigDecimal deliveryVolume;

    @Column(name = "fragile", nullable = false)
    private Boolean fragile;

    @ToString.Exclude
    @ElementCollection
    @CollectionTable(name = "booked_products", joinColumns = @JoinColumn(name = "booking_id"))
    @MapKeyColumn(name = "product_id", nullable = false, length = 50)
    @Column(name = "quantity", nullable = false)
    private Map<String, Long> products = new HashMap<>();
}
