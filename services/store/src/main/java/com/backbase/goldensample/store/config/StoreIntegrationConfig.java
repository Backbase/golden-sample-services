package com.backbase.goldensample.store.config;

import com.backbase.goldensample.product.api.client.ApiClient;
import com.backbase.goldensample.product.api.client.v2.ProductServiceApi;
import com.backbase.goldensample.review.api.client.v2.ReviewServiceApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class StoreIntegrationConfig {

    @Value("${app.product-service.host}") String productServiceHost;
    @Value("${app.product-service.port}") int productServicePort;
    @Value("${app.review-service.host}") String reviewServiceHost;
    @Value("${app.review-service.port}") int reviewServicePort;

    private final RestTemplate restTemplate;

    public StoreIntegrationConfig(
        @Qualifier("interServiceRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Bean
    public ProductServiceApi productServiceImplApi() {
        return new ProductServiceApi(apiClient());
    }

    @Bean
    public ApiClient apiClient() {
        String productServiceUrl        = "http://" + productServiceHost + ":" + productServicePort;
        return new ApiClient(restTemplate).setBasePath(productServiceUrl);
    }

    @Bean
    public ReviewServiceApi reviewServiceImplApi() {
        return new ReviewServiceApi(apiReviewClient());
    }

    @Bean
    public com.backbase.goldensample.review.api.client.ApiClient apiReviewClient() {
        String reviewServiceUrl         = "http://" + reviewServiceHost + ":" + reviewServicePort;
        return new com.backbase.goldensample.review.api.client.ApiClient(restTemplate).setBasePath(reviewServiceUrl);
    }
}
