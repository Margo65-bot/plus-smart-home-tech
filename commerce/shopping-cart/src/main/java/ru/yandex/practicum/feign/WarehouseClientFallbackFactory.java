package ru.yandex.practicum.feign;

import feign.FeignException;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class WarehouseClientFallbackFactory implements FallbackFactory<WarehouseClient> {
    @Override
    public WarehouseClient create(Throwable cause) {
        return new WarehouseClient() {

            @Override
            public void createProduct(NewProductInWarehouseRequest newProductInWarehouseRequest) {
                throw new UnsupportedOperationException("Метод createProduct() не поддерживается");
            }

            @Override
            public BookedProductsDto checkShoppingCart(ShoppingCartDto shoppingCartDto) {
                if (cause instanceof FeignException.UnprocessableEntity e) {
                    throw e;
                } else if (cause instanceof CallNotPermittedException e) {
                    log.warn("[{}] Circuit Breaker в состоянии OPEN для сервиса Warehouse",
                            cause.getClass().getSimpleName());
                } else if (cause instanceof FeignException.FeignServerException e) {
                    log.warn("[{}] Ошибка сервера (5xx) в сервисе Warehouse",
                            cause.getClass().getSimpleName());
                } else if (cause instanceof FeignException.FeignClientException e) {
                    log.warn("[{}] Ошибка клиента (4xx) в сервисе Warehouse",
                            cause.getClass().getSimpleName());
                } else if (cause instanceof RetryableException e) {
                    log.warn("[{}] Таймаут при вызове сервиса Warehouse",
                            cause.getClass().getSimpleName());
                } else if (cause instanceof TimeoutException e) {
                    log.warn("[{}] Таймаут выполнения при вызове сервиса Warehouse",
                            cause.getClass().getSimpleName());
                } else if (cause instanceof IOException e) {
                    log.warn("[{}] Сетевая ошибка при вызове сервиса Warehouse",
                            cause.getClass().getSimpleName());
                } else {
                    log.warn("[{}] Неизвестная ошибка при вызове сервиса Warehouse",
                            cause.getClass().getSimpleName());
                }
                return null;
            }

            @Override
            public void changeQuantity(AddProductToWarehouseRequest addProductToWarehouseRequest) {
                throw new UnsupportedOperationException("Метод changeQuantity() не поддерживается");
            }

            @Override
            public AddressDto getAddress() {
                throw new UnsupportedOperationException("Метод getAddress() не поддерживается");
            }
        };
    }
}