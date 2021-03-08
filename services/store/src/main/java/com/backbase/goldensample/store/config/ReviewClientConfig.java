package com.backbase.goldensample.store.config;

import com.backbase.goldensample.review.api.client.ApiClient;
import com.backbase.goldensample.review.api.client.v1.ReviewServiceApi;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("app.review-service")
public class ReviewClientConfig extends AbstractClientConfig {

    private static final String DEFAULT_SERVICE_ID = "review";

    public ReviewClientConfig() {
        super(DEFAULT_SERVICE_ID);
    }

    @Bean
    public ReviewServiceApi reviewServiceImplApi() {
        return new ReviewServiceApi(createApiClient());
    }

    @Bean
    public com.backbase.goldensample.review.api.client.v2.ReviewServiceApi reviewServiceImplApiV2() {
        return new com.backbase.goldensample.review.api.client.v2.ReviewServiceApi(createApiClient());
    }

    public ApiClient createApiClient() {
        return new ApiClient(restTemplate).setBasePath(basePath());
    }
}
