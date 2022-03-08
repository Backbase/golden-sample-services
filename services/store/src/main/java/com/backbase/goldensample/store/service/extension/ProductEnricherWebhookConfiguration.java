package com.backbase.goldensample.store.service.extension;

import com.backbase.buildingblocks.communication.client.ApiClientConfig;
import com.backbase.buildingblocks.context.ContextScoped;
import com.backbase.goldensample.store.service.extension.api.client.ApiClient;
import com.backbase.goldensample.store.service.extension.api.client.v2.StoreIntegrationWebhookApi;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 *
 */
@Validated
@Configuration
@ConfigurationProperties("backbase.store.extensions.product-enricher.client")
@ContextScoped
public class ProductEnricherWebhookConfiguration extends ApiClientConfig {

    public ProductEnricherWebhookConfiguration() {
        super(null);
    }

    @Bean
    @ContextScoped
    public StoreIntegrationWebhookApi storeIntegrationWebhookApi() {
        return new StoreIntegrationWebhookApi(createClient());
    }

    private ApiClient createClient() {
        return new ApiClient(getRestTemplate()).setBasePath(createBasePath());
    }
}
