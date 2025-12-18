package ru.yandex.practicum.dto.shopping_store;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageableDto {
    Integer page;
    Integer size;
    String sort;
}
