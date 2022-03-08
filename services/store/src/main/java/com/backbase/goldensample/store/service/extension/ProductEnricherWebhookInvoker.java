package com.backbase.goldensample.store.service.extension;

import com.backbase.buildingblocks.presentation.errors.InternalServerErrorException;
import com.backbase.goldensample.store.domain.Product;
import com.backbase.goldensample.store.service.extension.api.client.v2.StoreIntegrationWebhookApi;
import com.backbase.goldensample.store.service.extension.api.client.v2.model.ProductAggregate;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * This implementation of the Product Enricher allows for extending the service behaviour by 'web-hook'.
 *
 * <p>When enabled (by setting the service id of the extension service) an endpoint is called allowing the
 * implementing service to update the product or its reviews.
 */
@Component
@ConditionalOnProperty(matchIfMissing = false, value = "backbase.store.extensions.product-enricher.client.service-id")
@RequiredArgsConstructor
@ConfigurationProperties("backbase.store.extensions.product-enricher")
@Slf4j
public class ProductEnricherWebhookInvoker implements ProductEnricher {

    private final StoreIntegrationWebhookApi webhook;

    private final ProductEnricherMapper mapper;

    @Setter
    private boolean ignoreErrorResponse;

    @Override
    public void enrichProduct(Product product) {
        try {
            ProductAggregate productAggregate = mapper.map(product);
            mapper.map(product, webhook.enrichProduct(productAggregate));
        } catch (RuntimeException e) {
            if (!ignoreErrorResponse) {
                log.warn("Error invoking product enricher for product {}", product.getProductId(), e);
                throw new InternalServerErrorException();
            }
            log.info("Ignoring exception {}", e.getMessage());
        }
    }
}
