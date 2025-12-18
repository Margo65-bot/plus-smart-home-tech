package ru.yandex.practicum.entity;

import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;

public class WarehouseMapper {
    public static Product mapToModel(NewProductInWarehouseRequest request) {
        Product result = new Product();
        result.setProductId(request.getProductId());
        result.setDepth(request.getDimension().getDepth());
        result.setHeight(request.getDimension().getHeight());
        result.setWidth(request.getDimension().getWidth());
        result.setWeight(request.getWeight());
        result.setFragile(request.getFragile());
        result.setQuantity(0L);
        return result;
    }
}
