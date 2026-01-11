package ru.yandex.practicum.feign;

import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;

import java.util.Map;

@FeignClient(name = "warehouse")
public interface WarehouseClient {
    @GetMapping("/api/v1/warehouse/address")
    AddressDto getAddress() throws FeignException;

    @PostMapping("/api/v1/warehouse/assembly")
    BookedProductsDto assemblyProductsForOrder(
            @RequestBody AssemblyProductsForOrderRequest assemblyProductsForOrderRequest
    ) throws FeignException;

    @PostMapping("/api/v1/warehouse/return")
    void returnProducts(
            @RequestBody Map<String, Long> products
    ) throws FeignException;
}
