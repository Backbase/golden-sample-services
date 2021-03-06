package com.backbase.goldensample.store.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.backbase.buildingblocks.communication.http.HttpCommunicationConfiguration;
import com.backbase.goldensample.store.Application;
import com.backbase.goldensample.store.config.ProductClientConfiguration;
import com.backbase.goldensample.store.config.ReviewClientConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class AppConfigurationTest {

    @Test
    void should_check_presence_of_example_service() {
        /*
         * Setup a context runner with the class StoreIntegrationConfig
         * in it. For that, I use ApplicationContextRunner#withUserConfiguration()
         * methods to populate the context.
         */
        ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(HttpCommunicationConfiguration.class)
            .withUserConfiguration(ProductClientConfiguration.class)
            .withUserConfiguration(ReviewClientConfiguration.class)
            .withPropertyValues(
                "app.product-service.service-id=localhost",
                "app.product-service.service-port=8080",
                "app.review-service.service-id=localhost",
                "app.review-service.service-port=8080");
        /*
         * We start the context and we will be able to trigger
         * assertions in a lambda receiving a
         * AssertableApplicationContext
         */
        context.run(it -> {
            /*
             * We can use assertThat to assert on the context
             * and check if the @Beans configured are present
             */
            assertAll(
                () -> assertThat(it).hasBean("interServiceRestTemplate")
                    .as("RestTemplate bean is required to inject Sleuth headers. Don't use 'new RestTemplate()'"),
                () -> assertThat(it).hasBean("accessTokenRestTemplate"),
                () -> assertThat(it).hasBean("productServiceImplApi"),
                () -> assertThat(it).hasBean("reviewServiceImplApi"));
        });
    }

    @Test
    void testReviewV2Configured() {
        ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(Application.class)
            .withPropertyValues(
                "app.review-service.api-version=v2", "spring.application.name=store")
            .withSystemProperties("SIG_SECRET_KEY=JWTSecretKeyDontUseInProduction!");

        context.run(it -> {
            assertAll(
                () -> assertThat(it).hasBean("reviewServiceImplApiV2"),
                () -> assertThat(it).getBean("reviewServiceImplApiV2").isInstanceOf(com.backbase.goldensample.review.api.client.v2.ReviewServiceApi.class));
            });
    }
}
