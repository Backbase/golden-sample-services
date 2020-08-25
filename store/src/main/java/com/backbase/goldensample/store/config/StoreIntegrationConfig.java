package com.backbase.goldensample.store.config;

import com.backbase.goldensample.product.api.client.ApiClient;
import com.backbase.goldensample.product.api.client.v2.ProductServiceImplApi;
import com.backbase.goldensample.review.api.client.v2.ReviewServiceImplApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StoreIntegrationConfig {

    @Value("${app.product-service.host}") String productServiceHost;
    @Value("${app.product-service.port}") int productServicePort;
    @Value("${app.review-service.host}") String reviewServiceHost;
    @Value("${app.review-service.port}") int reviewServicePort;

    @Bean
    public ProductServiceImplApi productServiceImplApi() {
        return new ProductServiceImplApi(apiClient());
    }

    @Bean
    public ApiClient apiClient() {
        String productServiceUrl        = "http://" + productServiceHost + ":" + productServicePort;
        return new ApiClient().setBasePath(productServiceUrl);
    }

    @Bean
    public ReviewServiceImplApi reviewServiceImplApi() {
        return new ReviewServiceImplApi(apiReviewClient());
    }

    @Bean
    public com.backbase.goldensample.review.api.client.ApiClient apiReviewClient() {
        String reviewServiceUrl         = "http://" + reviewServiceHost + ":" + reviewServicePort;
        return new com.backbase.goldensample.review.api.client.ApiClient().setBasePath(reviewServiceUrl);
    }
}
