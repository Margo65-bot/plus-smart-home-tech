package ru.yandex.practicum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Entity
@Table(name = "products")
public class Product {
    @Id
    @Column(name = "product_id", nullable = false, length = 50)
    private String productId;

    @Column(name = "depth", nullable = false)
    private BigDecimal depth;

    @Column(name = "height", nullable = false)
    private BigDecimal height;

    @Column(name = "width", nullable = false)
    private BigDecimal width;

    @Column(name = "weight", nullable = false)
    private BigDecimal weight;

    @Column(name = "fragile", nullable = true)
    private Boolean fragile;

    @Column(name = "quantity", nullable = false)
    private Long quantity;
}
