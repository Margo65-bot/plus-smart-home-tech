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
import ru.yandex.practicum.dto.shopping_store.ProductCategory;
import ru.yandex.practicum.dto.shopping_store.ProductState;
import ru.yandex.practicum.dto.shopping_store.QuantityState;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, length = 50)
    private String productId;;

    @Column(name = "name", nullable = false, length = 50)
    private String productName;

    @Column(name = "description", nullable = true, length = 255)
    private String description;

    @Column(name = "image_src", nullable = true, length = 255)
    private String imageSrc;

    @Enumerated(EnumType.STRING)
    @Column(name = "quantity_state", nullable = false, length = 15)
    private QuantityState quantityState;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 15)
    private ProductState productState;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 15)
    private ProductCategory productCategory;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

}
