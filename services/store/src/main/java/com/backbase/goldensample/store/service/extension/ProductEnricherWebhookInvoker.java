package com.backbase.goldensample.store.service.extension;

import com.backbase.buildingblocks.presentation.errors.ApiErrorException;
import com.backbase.buildingblocks.presentation.errors.InternalServerErrorException;
import com.backbase.goldensample.productenricher.webhook.api.client.v2.StoreIntegrationWebhookApi;
import com.backbase.goldensample.productenricher.webhook.api.client.v2.model.ProductAggregate;
import com.backbase.goldensample.store.domain.Product;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * This implementation of the Product Enricher allows for extending the service behaviour by 'web-hook'.
 *
 * <p>When enabled (by setting the service id of the extension service) an endpoint is called allowing the
 * implementing service to update the product or its reviews.
 */
@Component
@ConditionalOnProperty(havingValue = "true", value = "backbase.store.extensions.product-enricher.enabled")
@ConfigurationProperties("backbase.store.extensions.product-enricher")
@RequiredArgsConstructor
@Slf4j
public class ProductEnricherWebhookInvoker implements ProductEnricher {

    private final StoreIntegrationWebhookApi webhook;

    private final ProductEnricherMapper mapper;

    @Setter
    private boolean ignoreErrorResponse;

    @Override
    public void enrichProduct(@NonNull Product product) {
        try {
            ProductAggregate productAggregate = mapper.map(product);
            ResponseEntity<ProductAggregate> entity = webhook.enrichProductWithHttpInfo(productAggregate);
            switch (entity.getStatusCode().value()) {
                case 204:
                    break;
                case 200:
                    mapper.map(product, entity.getBody());
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected http response " + entity.getStatusCode());
            }
        } catch (ApiErrorException | IllegalArgumentException e) {
            if (ignoreErrorResponse) {
                log.warn("Error invoking product enricher for product {}", product.getProductId(), e);
            } else {
                throw new InternalServerErrorException(
                        "Error invoking product enricher for product " + product.getProductId(), e);
            }
        }
    }
}
