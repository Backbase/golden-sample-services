package com.backbase.goldensample.store.config;

import com.backbase.goldensample.product.api.client.ApiClient;
import com.backbase.goldensample.product.api.client.v1.ProductServiceApi;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("app.product-service")
public class ProductClientConfig extends AbstractClientConfig {

    private static final String DEFAULT_SERVICE_ID = "product";

    public ProductClientConfig() {
        super(DEFAULT_SERVICE_ID);
    }

    @Bean
    public ProductServiceApi productServiceImplApi() {
        return new ProductServiceApi(createApiClient());
    }

    private ApiClient createApiClient() {
        return new ApiClient(restTemplate).setBasePath(basePath());
    }

}
