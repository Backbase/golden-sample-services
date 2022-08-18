package com.backbase.goldensample.store.service.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backbase.goldensample.product.api.client.ApiClient;
import com.backbase.goldensample.product.api.client.v1.ProductServiceApi;
import com.backbase.goldensample.product.api.client.v1.model.ProductId;
import com.backbase.goldensample.store.client.ProductClientImpl;
import com.backbase.goldensample.store.domain.Product;
import com.backbase.goldensample.store.mapper.ProductMapper;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@ExtendWith(MockitoExtension.class)
class ProductClientImplTest {

    private static final long PRODUCT_ID = 123;

    private ProductClientImpl productClientImpl;

    @Mock
    private ProductServiceApi productServiceApi;

    @Mock
    private ProductMapper productMapper;

    private static final Product DOMAIN_PRODUCT = new Product();
    private static final com.backbase.goldensample.product.api.client.v1.model.Product MODEL_PRODUCT =
        new com.backbase.goldensample.product.api.client.v1.model.Product();

    @BeforeEach
    void init() {
        when(productServiceApi.getApiClient()).thenReturn(new ApiClient().setBasePath("/base/path"));
        productClientImpl = new ProductClientImpl(productServiceApi, productMapper);
    }

    @Test
    void getProductById() {
        when(productServiceApi.getProductById(PRODUCT_ID)).thenReturn(MODEL_PRODUCT);
        when(productMapper.map(MODEL_PRODUCT)).thenReturn(DOMAIN_PRODUCT);

        Optional<Product> product = productClientImpl.getProductById(PRODUCT_ID);
        verify(productServiceApi, times(1)).getProductById(PRODUCT_ID);
        verify(productMapper, times(1)).map(MODEL_PRODUCT);
    }

    @Test
    void getProductByIdNotFoundReturnsNull() {
        when(productServiceApi.getProductById(PRODUCT_ID))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Optional<Product> product = productClientImpl.getProductById(PRODUCT_ID);
        assertTrue(product.isEmpty());
        verify(productServiceApi, times(1)).getProductById(PRODUCT_ID);
        verify(productMapper, times(0)).map(MODEL_PRODUCT);
    }

    @Test
    void getProductByIdOtherErrorThrows() {
        when(productServiceApi.getProductById(PRODUCT_ID))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        assertThrows(HttpClientErrorException.class, () -> productClientImpl.getProductById(PRODUCT_ID));

        verify(productServiceApi, times(1)).getProductById(PRODUCT_ID);
        verify(productMapper, times(0)).map(MODEL_PRODUCT);
    }

    @Test
    void postProduct() {
        when(productServiceApi.postProduct(MODEL_PRODUCT)).thenReturn(new ProductId().id(PRODUCT_ID));
        when(productMapper.map(DOMAIN_PRODUCT)).thenReturn(MODEL_PRODUCT);

        long productId = productClientImpl.postProduct(DOMAIN_PRODUCT);
        assertEquals(PRODUCT_ID, productId);
        verify(productMapper, times(1)).map(DOMAIN_PRODUCT);
    }
}