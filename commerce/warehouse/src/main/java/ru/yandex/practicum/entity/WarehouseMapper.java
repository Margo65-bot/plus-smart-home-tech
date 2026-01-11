package ru.yandex.practicum.entity;

import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;

public class WarehouseMapper {
    public static Product mapToProductModel(NewProductInWarehouseRequest request) {
        Product result = new Product();
        result.setProductId(request.productId());
        result.setDepth(request.dimension().depth());
        result.setHeight(request.dimension().height());
        result.setWidth(request.dimension().width());
        result.setWeight(request.weight());
        result.setFragile(request.fragile());
        result.setQuantity(0L);
        return result;
    }

    public static BookedProductsDto mapToBookedProductsDto(OrderBooking orderBooking) {
        return new BookedProductsDto(
                orderBooking.getDeliveryWeight(),
                orderBooking.getDeliveryVolume(),
                orderBooking.getFragile()
        );
    }
}
