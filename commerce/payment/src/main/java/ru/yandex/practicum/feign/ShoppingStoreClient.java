package ru.yandex.practicum.feign;

import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.dto.shopping_store.ProductDto;

@FeignClient(name = "shopping_store")
public interface ShoppingStoreClient {
    @GetMapping("/api/v1/shopping-store/{productId}")
    ProductDto getById(
            @PathVariable String productId
    ) throws FeignException;
}
