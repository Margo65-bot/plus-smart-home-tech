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
import ru.yandex.practicum.exception.warehouse.ServiceUnavailableException;

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
                throw new ServiceUnavailableException("Сервис Warehouse недоступен: createProduct");
            }

            @Override
            public BookedProductsDto checkShoppingCart(ShoppingCartDto shoppingCartDto) {
                String causeName = cause.getClass().getSimpleName();
                if (cause instanceof FeignException.UnprocessableEntity e) {
                    throw e;
                } else if (cause instanceof CallNotPermittedException e) {
                    log.warn("[{}] Circuit Breaker в состоянии OPEN для сервиса Warehouse",
                            causeName);
                } else if (cause instanceof FeignException.FeignServerException e) {
                    log.warn("[{}] Ошибка сервера (5xx) в сервисе Warehouse",
                            causeName);
                } else if (cause instanceof FeignException.FeignClientException e) {
                    log.warn("[{}] Ошибка клиента (4xx) в сервисе Warehouse",
                            causeName);
                } else if (cause instanceof RetryableException e) {
                    log.warn("[{}] Таймаут при вызове сервиса Warehouse",
                            causeName);
                } else if (cause instanceof TimeoutException e) {
                    log.warn("[{}] Таймаут выполнения при вызове сервиса Warehouse",
                            causeName);
                } else if (cause instanceof IOException e) {
                    log.warn("[{}] Сетевая ошибка при вызове сервиса Warehouse",
                            causeName);
                } else {
                    log.warn("[{}] Неизвестная ошибка при вызове сервиса Warehouse",
                            causeName);
                }
                throw new ServiceUnavailableException("Сервис Warehouse недоступен: checkShoppingCart");
            }

            @Override
            public void changeQuantity(AddProductToWarehouseRequest addProductToWarehouseRequest) {
                throw new ServiceUnavailableException("Сервис Warehouse недоступен: changeQuantity");
            }

            @Override
            public AddressDto getAddress() {
                throw new ServiceUnavailableException("Сервис Warehouse недоступен: getAddress");
            }
        };
    }
}