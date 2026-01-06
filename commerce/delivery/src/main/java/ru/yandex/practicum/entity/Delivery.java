package ru.yandex.practicum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.dto.delivery.DeliveryState;
import ru.yandex.practicum.dto.warehouse.AddressDto;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Entity
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, length = 50)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_state", nullable = false, length = 25)
    private DeliveryState deliveryState;

    @Column(name = "fragile", nullable = false)
    private Boolean fragile;

    @Column(name = "total_weight", nullable = false)
    private BigDecimal totalWeight;

    @Column(name = "total_volume", nullable = false)
    private BigDecimal totalVolume;

    @Column(name = "from_address", nullable = false, length = 500)
    private AddressDto fromAddress;

    @Column(name = "to_address", nullable = false, length = 500)
    private AddressDto toAddress;

    @Column(name = "order_id", nullable = false, length = 50)
    private String orderId;
}
