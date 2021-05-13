package com.backbase.goldensample.store.client;

import com.backbase.goldensample.product.api.client.v1.ProductServiceApi;
import com.backbase.goldensample.store.domain.Product;
import com.backbase.goldensample.store.mapper.ProductMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductClientImpl implements ProductClient {

    private final ProductServiceApi productServiceApi;
    private final ProductMapper productMapper;

    @Override
    public Optional<Product> getProductById(long productId) {
        log.debug("Will call the getProduct API on URL: {}", productServiceApi.getApiClient().getBasePath());
        try {
            return Optional.of(productMapper.map(productServiceApi.getProductById(productId)));
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return Optional.empty();
            }
            throw e;
        }
    }

    @Override
    public long postProduct(Product product) {
        log.debug("Will post a new product to URL: {}", productServiceApi.getApiClient().getBasePath());
        return productServiceApi.postProduct(productMapper.map(product)).getId();
    }
}
