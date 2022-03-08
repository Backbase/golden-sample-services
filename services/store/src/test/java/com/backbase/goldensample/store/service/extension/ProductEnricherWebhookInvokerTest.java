package com.backbase.goldensample.store.service.extension;

import com.backbase.buildingblocks.presentation.errors.InternalServerErrorException;
import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.store.domain.Product;
import com.backbase.goldensample.store.service.extension.api.client.v2.StoreIntegrationWebhookApi;
import com.backbase.goldensample.store.service.extension.api.client.v2.model.ProductAggregate;
import com.backbase.goldensample.store.service.extension.api.client.v2.model.ReviewSummary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductEnricherWebhookInvokerTest {

    @Mock
    private StoreIntegrationWebhookApi api;

    @Spy
    private ProductEnricherMapper mapper = Mappers.getMapper(ProductEnricherMapper.class);

    @InjectMocks
    private ProductEnricherWebhookInvoker webhookInvoker;

    @Test
    void shouldInvokeClient() {
        Product p = new Product();
        webhookInvoker.enrichProduct(p);
        Mockito.verify(api).enrichProduct(ArgumentMatchers.any(ProductAggregate.class));
    }

    @Test
    void shouldUpdateProduct() {
        ProductAggregate updated = new ProductAggregate();
        updated.setAdditions(Map.of("key", "value"));
        updated.setReviews(List.of(new ReviewSummary().content("nice...")));
        Mockito.when(api.enrichProduct(ArgumentMatchers.any(ProductAggregate.class)))
                .thenReturn(updated);

        Product p = new Product();
        webhookInvoker.enrichProduct(p);
        assertEquals(1, p.getAdditions().size());
        assertEquals("nice...", p.getReviews().get(0).getContent());

    }

    @Test
    void shouldNotIgnoreFailingExtensionService() {
        Mockito.when(api.enrichProduct(ArgumentMatchers.any(ProductAggregate.class)))
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
        Mockito.when(api.enrichProduct(ArgumentMatchers.any(ProductAggregate.class)))
                .thenThrow(NotFoundException.class);
        webhookInvoker.setIgnoreErrorResponse(true);
        Product p = new Product();
        try {
            webhookInvoker.enrichProduct(p);
        } catch (RuntimeException e) {
            fail("Unexpected exception ", e);
        }
    }


}