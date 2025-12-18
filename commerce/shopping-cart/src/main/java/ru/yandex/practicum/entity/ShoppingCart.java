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

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, length = 50)
    private String id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ElementCollection
    @ToString.Exclude
    @CollectionTable(name = "cart_products", schema = "shopping_cart", joinColumns = @JoinColumn(name = "shopping_cart_id"))
    @MapKeyColumn(name = "product_id", nullable = false, length = 50)
    @Column(name = "quantity", nullable = false)
    private Map<String, Long> products = new HashMap<>();
}
