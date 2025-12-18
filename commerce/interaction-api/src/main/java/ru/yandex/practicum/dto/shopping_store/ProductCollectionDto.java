package ru.yandex.practicum.dto.shopping_store;

import lombok.Data;

import java.util.List;

@Data
public class ProductCollectionDto {
    private List<ProductDto> content;
    private List<SortDto> sort;
}
