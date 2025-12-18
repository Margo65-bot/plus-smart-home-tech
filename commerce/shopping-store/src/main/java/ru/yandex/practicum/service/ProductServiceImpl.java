package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.shopping_store.PageableDto;
import ru.yandex.practicum.dto.shopping_store.ProductCategory;
import ru.yandex.practicum.dto.shopping_store.ProductCollectionDto;
import ru.yandex.practicum.dto.shopping_store.ProductDto;
import ru.yandex.practicum.dto.shopping_store.ProductState;
import ru.yandex.practicum.dto.shopping_store.QuantityState;
import ru.yandex.practicum.dto.shopping_store.SortDto;
import ru.yandex.practicum.entity.Product;
import ru.yandex.practicum.entity.ProductMapper;
import ru.yandex.practicum.exception.shopping_store.ProductNotFoundException;
import ru.yandex.practicum.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = false)
    public ProductDto create(ProductDto productDto) {
        Product product = productRepository.save(ProductMapper.mapToModel(productDto));
        return ProductMapper.mapToDto(product);
    }

    @Override
    @Transactional(readOnly = false)
    public ProductDto update(ProductDto productDto) {
        Product product = getProductOrThrow(productDto.getProductId());

        if (productDto.getProductName() != null && !productDto.getProductName().equals(product.getProductName())) {
            product.setProductName(productDto.getProductName());
        }

        if (productDto.getDescription() != null && !productDto.getDescription().equals(product.getDescription())) {
            product.setDescription(productDto.getDescription());
        }

        if (productDto.getImageSrc() != null && !productDto.getImageSrc().equals(product.getImageSrc())) {
            product.setImageSrc(productDto.getImageSrc());
        }

        if (productDto.getQuantityState() != null && !productDto.getQuantityState().equals(product.getQuantityState())) {
            product.setQuantityState(productDto.getQuantityState());
        }

        if (productDto.getProductState() != null && !productDto.getProductState().equals(product.getProductState())) {
            product.setProductState(productDto.getProductState());
        }

        if (productDto.getProductCategory() != null && !productDto.getProductCategory().equals(product.getProductCategory())) {
            product.setProductCategory(productDto.getProductCategory());
        }

        if (productDto.getPrice() != null && !productDto.getPrice().equals(product.getPrice())) {
            product.setPrice(productDto.getPrice());
        }

        return ProductMapper.mapToDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getById(String productId) {
        Product product = getProductOrThrow(productId);
        return ProductMapper.mapToDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductCollectionDto getCollection(ProductCategory category, PageableDto pageable) {
        List<SortDto> sortList = parseSortString(pageable.getSort());
        Sort sort = sortList.stream()
                .map(s -> Sort.by(Sort.Direction.valueOf(s.getDirection()), s.getProperty()))
                .reduce(Sort.unsorted(), Sort::and);
        Pageable pageableForReq = PageRequest.of(pageable.getPage(), pageable.getSize(), sort);

        Page<Product> products = productRepository.findAllByProductCategory(category, pageableForReq);
        List<ProductDto> productsDto = products.stream()
                .map(ProductMapper::mapToDto)
                .toList();

        ProductCollectionDto result = new ProductCollectionDto();
        result.setContent(productsDto);
        result.setSort(sortList);
        return result;
    }

    @Override
    @Transactional(readOnly = false)
    public String remove(String productId) {
        String id = productId.replaceAll("\"", "");
        Product product = getProductOrThrow(id);
        if (product.getProductState() == ProductState.DEACTIVATE) {
            return "false";
        }
        product.setProductState(ProductState.DEACTIVATE);
        return "true";
    }

    @Override
    @Transactional(readOnly = false)
    public String setQuantityState(String productId, QuantityState quantityState) {
        Product product = getProductOrThrow(productId);
        product.setQuantityState(quantityState);
        return "true";
    }

    private List<SortDto> parseSortString(String sortString) {
        List<SortDto> sort = new ArrayList<>();
        if (sortString == null || sortString.isEmpty()) return sort;
        String[] parts = sortString.split(",");
        for (int i = 0; i < parts.length - 1; i += 2) {
            SortDto dto = new SortDto();
            dto.setProperty(parts[i].trim());
            dto.setDirection(parts[i + 1].trim());
            sort.add(dto);
        }
        return sort;
    }

    @Transactional(readOnly = true)
    private Product getProductOrThrow(String productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("Товар не найден, id = " + productId));
    }
}