package com.backbase.goldensample.store.config;

import com.backbase.buildingblocks.communication.client.ApiClientConfig;
import com.backbase.buildingblocks.context.ContextScoped;
import com.backbase.goldensample.review.api.client.ApiClient;
import com.backbase.goldensample.review.api.client.v1.ReviewServiceApi;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
@ConfigurationProperties("backbase.review-service")
@ContextScoped
public class ReviewClientConfiguration extends ApiClientConfig {

    private static final String DEFAULT_SERVICE_ID = "review";

    public ReviewClientConfiguration() {
        super(DEFAULT_SERVICE_ID);
    }

    @Bean
    @ContextScoped
    public ReviewServiceApi reviewServiceImplApi() {
        return new ReviewServiceApi(createApiClient());
    }

    @Bean
    @ContextScoped
    public com.backbase.goldensample.review.api.client.v2.ReviewServiceApi reviewServiceImplApiV2() {
        return new com.backbase.goldensample.review.api.client.v2.ReviewServiceApi(createApiClient());
    }

    public ApiClient createApiClient() {
        return new ApiClient(getRestTemplate()).setBasePath(createBasePath());
    }
}
