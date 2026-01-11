package ru.yandex.practicum.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.dto.shopping_cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.shopping_cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;

public interface ShoppingCartApi {
    @PutMapping("/api/v1/shopping-cart")
    @ResponseStatus(HttpStatus.OK)
    ShoppingCartDto addProducts(
            @RequestParam(required = true) String username,
            @RequestBody @Valid @NotNull @NotEmpty Map<@NotNull String, @Positive Long> addProductMap
    );

    @PostMapping("/api/v1/shopping-cart/remove")
    @ResponseStatus(HttpStatus.OK)
    ShoppingCartDto removeProducts(
            @RequestParam(required = true) String username,
            @RequestBody @Valid @NotNull @NotEmpty List<@NotNull String> removeProductList
    );

    @PostMapping("/api/v1/shopping-cart/change-quantity")
    @ResponseStatus(HttpStatus.OK)
    ShoppingCartDto changeProductQuantity(
            @RequestParam(required = true) String username,
            @RequestBody @Valid ChangeProductQuantityRequest changeProductQuantityRequest
    );

    @DeleteMapping("/api/v1/shopping-cart")
    @ResponseStatus(HttpStatus.OK)
    void deactivate(
            @RequestParam(required = true) String username
    );

    @GetMapping("/api/v1/shopping-cart")
    @ResponseStatus(HttpStatus.OK)
    ShoppingCartDto getByUsername(
            @RequestParam(required = true) String username
    );
}
