package com.backbase.goldensample.store.service.extension;

import com.backbase.buildingblocks.presentation.errors.InternalServerErrorException;
import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.productenricher.webhook.api.client.v2.StoreIntegrationWebhookApi;
import com.backbase.goldensample.productenricher.webhook.api.client.v2.model.ProductAggregate;
import com.backbase.goldensample.productenricher.webhook.api.client.v2.model.ReviewSummary;
import com.backbase.goldensample.store.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductEnricherWebhookInvokerTest {

    @Mock
    private StoreIntegrationWebhookApi api;

    @Spy
    private ProductEnricherMapper mapper = Mappers.getMapper(ProductEnricherMapper.class);

    @InjectMocks
    private ProductEnricherWebhookInvoker webhookInvoker;

    @Test
    void shouldUpdateProductWith200Response() {
        ProductAggregate updated = new ProductAggregate();
        updated.setAdditions(Map.of("key", "value"));
        updated.setReviews(List.of(new ReviewSummary().content("nice...")));
        Mockito.when(api.enrichProductWithHttpInfo(ArgumentMatchers.any(ProductAggregate.class)))
                .thenReturn(ResponseEntity.ok(updated));

        Product p = new Product();
        webhookInvoker.enrichProduct(p);
        assertEquals(1, p.getAdditions().size());
        assertEquals("nice...", p.getReviews().get(0).getContent());
    }

    @Test
    void shouldNotUpdateProductWith204Response() {
        Mockito.when(api.enrichProductWithHttpInfo(ArgumentMatchers.any(ProductAggregate.class)))
                .thenReturn(ResponseEntity.noContent().build());

        Product p = new Product();
        webhookInvoker.enrichProduct(p);
        verify(mapper, times(0)).map(any(Product.class), any());
    }

    @Test
    void shouldNotIgnoreFailingExtensionService() {
        Mockito.when(api.enrichProductWithHttpInfo(ArgumentMatchers.any(ProductAggregate.class)))
                .thenThrow(NotFoundException.class);

        Product p = new Product();
        webhookInvoker.setIgnoreErrorResponse(false);
        assertThrows(
                InternalServerErrorException.class,
                () -> webhookInvoker.enrichProduct(p)
        );
    }

    @Test
    void shouldIgnoreFailingExtensionServiceWhenConfigured() {
        Mockito.when(api.enrichProductWithHttpInfo(ArgumentMatchers.any(ProductAggregate.class)))
                .thenThrow(NotFoundException.class);

        webhookInvoker.setIgnoreErrorResponse(true);
        Product p = new Product();
        try {
            webhookInvoker.enrichProduct(p);
        } catch (RuntimeException e) {
            fail("Unexpected exception ", e);
        }
    }

    @Test
    void shouldHandleUnexpectedResponseCode() {
        Mockito.when(api.enrichProductWithHttpInfo(ArgumentMatchers.any(ProductAggregate.class)))
                .thenReturn(ResponseEntity.accepted().body(null));

        Product p = new Product();
        assertThrows(
                InternalServerErrorException.class,
                () -> webhookInvoker.enrichProduct(p)
        );
    }

}
