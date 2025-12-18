package ru.yandex.practicum.service;

import feign.FeignException;
import jakarta.ws.rs.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.yandex.practicum.dto.shopping_cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.shopping_cart.ShoppingCartDto;
import ru.yandex.practicum.entity.ShoppingCart;
import ru.yandex.practicum.entity.ShoppingCartMapper;
import ru.yandex.practicum.exception.shopping_cart.NoActiveShoppingCartException;
import ru.yandex.practicum.exception.shopping_cart.NoProductsInShoppingCartException;
import ru.yandex.practicum.exception.shopping_cart.NotAuthorizedUserException;
import ru.yandex.practicum.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.repository.ShoppingCartRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final WarehouseClient warehouseClient;
    private final TransactionTemplate transactionTemplate;

    @Override
    @Transactional(readOnly = false)
    public ShoppingCartDto addProducts(String username, Map<String, Long> addProductMap) {
        checkUsername(username);

        ShoppingCart cart = getOrCreateCart(username);
        String cartId = cart.getId();

        ShoppingCartDto checkCartDto = new ShoppingCartDto();
        checkCartDto.setShoppingCartId(cartId != null ? cartId : "new-cart-check");

        Map<String, Long> productsForCheck = new HashMap<>(cart.getProducts());
        addProductMap.forEach((key, value) ->
                productsForCheck.merge(key, value, Long::sum)
        );
        checkCartDto.setProducts(productsForCheck);

        validateWarehouseAvailability(checkCartDto);

        addProductMap.forEach((productId, quantity) ->
                cart.getProducts().merge(productId, quantity, Long::sum)
        );

        return ShoppingCartMapper.mapToDto(cart);
    }

    @Override
    @Transactional(readOnly = false)
    public ShoppingCartDto removeProducts(String username, List<String> removeProductList) {
        checkUsername(username);

        ShoppingCart cart = getActiveCart(username);

        List<String> missingProducts = removeProductList.stream()
                .filter(p -> !cart.getProducts().containsKey(p))
                .collect(Collectors.toList());

        if (!missingProducts.isEmpty()) {
            String errorMsg = "Товары не найдены в корзине: " + String.join("; ", missingProducts);
            throw new NoProductsInShoppingCartException(errorMsg);
        }

        removeProductList.forEach(cart.getProducts()::remove);

        if (cart.getProducts().isEmpty()) {
            cart.setIsActive(false);
        }

        return ShoppingCartMapper.mapToDto(cart);
    }

    @Override
    @Transactional(readOnly = false)
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        checkUsername(username);

        ShoppingCart cart = getActiveCart(username);

        if (!cart.getProducts().containsKey(request.getProductId())) {
            throw new NoProductsInShoppingCartException(
                    "Товар не найден в корзине: " + request.getProductId()
            );
        }

        ShoppingCartDto checkCartDto = new ShoppingCartDto();
        checkCartDto.setShoppingCartId("quantity-check");
        checkCartDto.setProducts(Map.of(request.getProductId(), request.getNewQuantity()));

        validateWarehouseAvailability(checkCartDto);

        cart.getProducts().put(request.getProductId(), request.getNewQuantity());

        if (request.getNewQuantity() == 0) {
            cart.getProducts().remove(request.getProductId());
            if (cart.getProducts().isEmpty()) {
                cart.setIsActive(false);
            }
        }

        return ShoppingCartMapper.mapToDto(cart);
    }

    @Override
    @Transactional(readOnly = false)
    public String deactivate(String username) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым");
        }

        ShoppingCart shoppingCart = shoppingCartRepository.findByUsernameAndIsActive(username, true)
                        .orElseThrow(() -> new NoActiveShoppingCartException(
                                "Нет активной корзины товаров пользователя с id = " + username
                        ));

        shoppingCart.setIsActive(false);
        return "OK";
    }

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartDto getByUsername(String username) {
        checkUsername(username);

        return shoppingCartRepository
                .findByUsernameAndIsActiveOrderByIdDesc(username, true)
                .stream()
                .findFirst()
                .map(ShoppingCartMapper::mapToDto)
                .orElseThrow(() ->
                        new NoActiveShoppingCartException(
                                "Не найдена активная корзина пользователя " + username
                        )
                );
    }

    private void validateWarehouseAvailability(ShoppingCartDto checkCartDto) {
        try {
            warehouseClient.checkShoppingCart(checkCartDto);
        } catch (FeignException.UnprocessableEntity e) {
            throw new ProductInShoppingCartLowQuantityInWarehouse(e.getMessage());
        } catch (FeignException e) {
            throw new ServiceUnavailableException("Сервис склада недоступен");
        }
    }

    private ShoppingCart getOrCreateCart(String username) {
        List<ShoppingCart> carts = shoppingCartRepository
                .findByUsernameAndIsActiveOrderByIdDesc(username, true);

        if (carts.isEmpty()) {
            ShoppingCart newCart = new ShoppingCart();
            newCart.setUsername(username);
            newCart.setIsActive(true);
            newCart.setProducts(new HashMap<>());
            return shoppingCartRepository.save(newCart);
        }

        return carts.getFirst();
    }

    private ShoppingCart getActiveCart(String username) {
        return shoppingCartRepository
                .findByUsernameAndIsActive(username, true)
                .orElseThrow(() -> new NoActiveShoppingCartException(
                        String.format("Не найдена активная корзина для пользователя '%s'", username)
                ));
    }

    private void checkUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым");
        }
    }
}