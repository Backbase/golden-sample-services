package com.backbase.goldensample.store.resttemplate;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import com.backbase.goldensample.product.api.client.ApiClient;
import com.backbase.goldensample.product.api.client.v2.ProductServiceApi;
import com.backbase.goldensample.product.api.client.v2.model.Product;
import com.backbase.goldensample.product.api.client.v2.model.ProductId;
import com.backbase.goldensample.review.api.client.v2.ReviewServiceApi;
import com.backbase.goldensample.review.api.client.v2.model.Review;
import com.backbase.goldensample.review.api.client.v2.model.ReviewId;
import com.backbase.goldensample.store.config.StoreIntegrationConfig;
import java.time.LocalDate;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@RestClientTest(value = {ProductServiceApi.class, ReviewServiceApi.class})
@AutoConfigureWebClient(registerRestTemplate = true)
@Import(StoreIntegrationConfig.class)
public class StoreRestTemplateTest {

    @Autowired
    private ProductServiceApi productServiceApi;

    @Autowired
    private ReviewServiceApi reviewServiceApi;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    private RestTemplate restTemplate;

    private final Product product = new Product().productId(1L).name("Product").weight(20).createDate(LocalDate.now());
    private final Review review =
        new Review().reviewId(1L).productId(1L).author("author").subject("subject").content("long content");

    @BeforeEach
    void init() {
        this.restTemplate = new RestTemplate();
        ApiClient productApiClient = new ApiClient(this.restTemplate);
        com.backbase.goldensample.review.api.client.ApiClient reviewApiClient =
            new com.backbase.goldensample.review.api.client.ApiClient(this.restTemplate);
        productServiceApi.setApiClient(productApiClient);
        reviewServiceApi.setApiClient(reviewApiClient);
        this.mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    @DisplayName("should Inject Beans")
    void shouldInjectBeans() {
        assertNotNull(productServiceApi);
        assertNotNull(reviewServiceApi);
        assertNotNull(mockRestServiceServer);
    }

    @Test
    @DisplayName("should return a Product when result is Success")
    void shouldReturnAProductWhenResultIsSuccess() {
        this.mockRestServiceServer
            .expect(MockRestRequestMatchers.requestTo(Matchers.containsString("/service-api/v1/products/" + 1L)))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(MockRestResponseCreators
                .withSuccess(new ClassPathResource("/stubs/product/success.json"), MediaType.APPLICATION_JSON));

        Product result = productServiceApi.getProductById(1L);

        assertNotNull(result);
    }

    @Test
    @DisplayName("should return Reviews when result is Success")
    void shouldReturnReviewsWhenResultIsSuccess() {
        this.mockRestServiceServer
            .expect(MockRestRequestMatchers
                .requestTo(Matchers.containsString("/service-api/v1/products/" + 1L + "/reviews")))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(MockRestResponseCreators
                .withSuccess(new ClassPathResource("/stubs/reviews/success.json"), MediaType.APPLICATION_JSON));

        List<Review> result = reviewServiceApi.getReviewListByProductId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("should create a Product when POST and result is Success")
    void shouldCreateAProductWhenPostAndResultIsSuccess() {
        this.mockRestServiceServer
            .expect(MockRestRequestMatchers.requestTo(Matchers.containsString("/service-api/v1/products")))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
            .andRespond(MockRestResponseCreators
                .withSuccess(new ClassPathResource("/stubs/product/success-post.json"), MediaType.APPLICATION_JSON));

        ProductId result = productServiceApi.postProduct(product);

        assertNotNull(result);
    }

    @Test
    @DisplayName("should create a Review when POST and result is Success")
    void shouldCreateAReviewWhenPostAndResultIsSuccess() {
        this.mockRestServiceServer
            .expect(MockRestRequestMatchers.requestTo(Matchers.containsString("/service-api/v1/reviews")))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
            .andRespond(MockRestResponseCreators
                .withSuccess(new ClassPathResource("/stubs/reviews/success-post.json"), MediaType.APPLICATION_JSON));

        ReviewId result = reviewServiceApi.postReview(review);

        assertNotNull(result);
    }

    @Test
    @DisplayName("should propagate exception when Product service is down")
    void shouldPropagateExceptionWhenProductServiceIsDown() {

        assertThrows(HttpServerErrorException.class, () -> {
            this.mockRestServiceServer
                .expect(MockRestRequestMatchers.requestTo(Matchers.containsString("/service-api/v1/products/999")))
                .andRespond(MockRestResponseCreators.withServerError());

            productServiceApi.getProductById(999L);
        });
    }

    @Test
    @DisplayName("should propagate exception when Review service is down")
    void shouldPropagateExceptionWhenReviewSystemIsDown() {

        assertThrows(HttpServerErrorException.class, () -> {
            this.mockRestServiceServer
                .expect(MockRestRequestMatchers.requestTo(Matchers.containsString("/service-api/v1/products/999/reviews")))
                .andRespond(MockRestResponseCreators.withServerError());

            reviewServiceApi.getReviewListByProductId(999L);
        });
    }

    @Test
    @DisplayName("should propagate Not Found exception when a Product is not found")
    void shouldPropagateNotFoundExceptionWhenAProductIsNotFound() {

        assertThrows(NotFound.class, () -> {
            this.mockRestServiceServer
                .expect(MockRestRequestMatchers.requestTo(Matchers.containsString("/service-api/v1/products/999")))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.NOT_FOUND));

            productServiceApi.getProductById(999L);
        });
    }

}
