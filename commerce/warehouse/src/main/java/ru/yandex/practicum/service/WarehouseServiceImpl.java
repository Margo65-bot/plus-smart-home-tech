package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.entity.Product;
import ru.yandex.practicum.entity.WarehouseMapper;
import ru.yandex.practicum.exception.warehouse.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exception.warehouse.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.repository.ProductRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final ProductRepository productRepository;

    private static final String[] ADDRESSES = new String[]{"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS = ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];

    @Override
    @Transactional(readOnly = false)
    public void createProduct(NewProductInWarehouseRequest newProductInWarehouseRequest) {
        if (productRepository.existsById(newProductInWarehouseRequest.productId())) {
            throw new SpecifiedProductAlreadyInWarehouseException(
                    "Товар уже существует: id = " + newProductInWarehouseRequest.productId()
            );
        }
        Product newProduct = WarehouseMapper.mapToModel(newProductInWarehouseRequest);
        productRepository.save(newProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public BookedProductsDto checkShoppingCart(ShoppingCartDto shoppingCartDto) {
        Map<String, Long> productsMap = shoppingCartDto.products();
        List<Product> products = productRepository.findAllById(productsMap.keySet());

        String lackString = "";
        String notEnoughString = "";

        boolean isLackOfProducts = products.size() < productsMap.size();
        boolean isNotEnoughProducts = products.stream()
                .anyMatch(p -> p.getQuantity() < productsMap.get(p.getProductId()));

        if (isLackOfProducts) {
            Set<String> lackProductIds = new HashSet<>(productsMap.keySet());

            for (Product wp : products) {
                lackProductIds.remove(wp.getProductId());
            }

            lackString = lackProductIds.stream()
                    .map(id -> id + "; ")
                    .collect(Collectors.joining("", "Отсутствуют товары на складе с идентификаторами: ", ""))
                    .replaceAll("; $", "");
        }

        if (isNotEnoughProducts) {
            notEnoughString = products.stream()
                    .filter(p -> p.getQuantity() < productsMap.get(p.getProductId()))
                    .map(p -> p.getProductId() + ": " + productsMap.get(p.getProductId()) + " в корзине, " + p.getQuantity() + " в наличии")
                    .collect(Collectors.joining("; ", "Недостаточно товаров на складе - ", ""));
        }

        if (isLackOfProducts || isNotEnoughProducts) {
            throw new ProductInShoppingCartLowQuantityInWarehouse(lackString + notEnoughString);
        }

        return calculateDeliveryDetails(products, productsMap);
    }

    @Override
    @Transactional(readOnly = false)
    public void changeQuantity(AddProductToWarehouseRequest addProductToWarehouseRequest) {
        Product product = productRepository.findById(addProductToWarehouseRequest.productId()).orElseThrow(
                () -> new NoSpecifiedProductInWarehouseException(
                        "Товар не найден: id = " + addProductToWarehouseRequest.productId()
                )
        );
        product.setQuantity(product.getQuantity() + addProductToWarehouseRequest.quantity());
    }

    @Override
    public AddressDto getAddress() {
        return new AddressDto(CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS);
    }

    private BookedProductsDto calculateDeliveryDetails(List<Product> products, Map<String, Long> productsMap) {
        BigDecimal deliveryWeight = products.stream()
                .map(Product::getWeight)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.UP);

        BigDecimal deliveryVolume = products.stream()
                .map(p -> BigDecimal.valueOf(productsMap.get(p.getProductId()))
                        .multiply(p.getWidth())
                        .multiply(p.getHeight())
                        .multiply(p.getDepth()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.UP);

        Boolean fragile = products.stream()
                .map(Product::getFragile)
                .anyMatch(Boolean.TRUE::equals);

        return new BookedProductsDto(deliveryWeight, deliveryVolume, fragile);
    }
}