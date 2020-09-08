package com.backbase.goldensample.store.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import com.backbase.goldensample.product.api.client.ApiClient;
import com.backbase.goldensample.product.api.client.v2.ProductServiceImplApi;
import com.backbase.goldensample.product.api.client.v2.model.Product;
import com.backbase.goldensample.product.api.client.v2.model.ProductId;
import com.backbase.goldensample.review.api.client.v2.ReviewServiceImplApi;
import com.backbase.goldensample.review.api.client.v2.model.Review;
import com.backbase.goldensample.review.api.client.v2.model.ReviewId;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@ExtendWith(MockitoExtension.class)
class ProductCompositeServiceTest {

    private ProductCompositeService productCompositeService;

    @Mock
    private ProductServiceImplApi productServiceImplApi;

    @Mock
    private ReviewServiceImplApi reviewServiceImplApi;

    @Mock
    ObjectMapper objectMapper;

    private static final Instant TODAY = LocalDateTime.of(2020, 1, 28, 12, 26)
        .toInstant(ZoneOffset.UTC);
    private final Product product = new Product().productId(1L).name("Product").weight(20).createDate(Date.from(TODAY));
    private final Review review = new Review().productId(1L).reviewId(1L).author("author").content("content").subject("subject");
    private final Review review2 = new Review().productId(1L).reviewId(2L).author("author").content("long content").subject("long subject");

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        productCompositeService = new ProductCompositeService(productServiceImplApi, reviewServiceImplApi, objectMapper);

    }

    @Test
    @DisplayName("should retrieve a Product by Id")
    void getProduct() {
        when(productServiceImplApi.getProductUsingGET(1L)).thenReturn(product);
        when(productServiceImplApi.getApiClient()).thenReturn(new ApiClient());

        //test
        Product product1 = productCompositeService.getProduct(1L);

        assertAll(
            () -> assertEquals("Product", product1.getName()),
            () -> assertEquals(20, product1.getWeight()),
            () -> assertEquals(Date.from(TODAY), product1.getCreateDate()));
        verify(productServiceImplApi, times(1)).getProductUsingGET(1L);
    }

    @Test
    @DisplayName("should retrieve a Product null")
    void getProductNull() {
        when(productServiceImplApi.getProductUsingGET(1L)).thenReturn(null);
        when(productServiceImplApi.getApiClient()).thenReturn(new ApiClient());

        //test
        Product product1 = productCompositeService.getProduct(1L);

        assertEquals(null, product1);
        verify(productServiceImplApi, times(1)).getProductUsingGET(1L);
    }

    @Test
    @DisplayName("should retrieve a HttpClientErrorException")
    void getProductHttpClientErrorException() {
        when(productServiceImplApi.getProductUsingGET(1L)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        when(productServiceImplApi.getApiClient()).thenReturn(new ApiClient());

        Assertions.assertThrows(HttpClientErrorException.class, () -> {productCompositeService.getProduct(1L);});
        verify(productServiceImplApi, times(1)).getProductUsingGET(1L);
    }

    @Test
    @DisplayName("should retrieve all the reviews from a Product")
    void getReviews() {
        when(reviewServiceImplApi.getReviewListByProductId(1L)).thenReturn(List.of(review, review2));
        when(reviewServiceImplApi.getApiClient()).thenReturn(new com.backbase.goldensample.review.api.client.ApiClient());
        
        List<Review> reviews = productCompositeService.getReviews(1L);

        assertEquals(2, reviews.size());
        verify(reviewServiceImplApi, times(1)).getReviewListByProductId(1L);
    }

    @Test
    @DisplayName("should retrieve an Exception")
    void getReviewsException() {
        when(reviewServiceImplApi.getReviewListByProductId(1L)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        when(reviewServiceImplApi.getApiClient()).thenReturn(new com.backbase.goldensample.review.api.client.ApiClient());

        List<Review> reviews = productCompositeService.getReviews(1L);

        assertEquals(0, reviews.size());

        verify(reviewServiceImplApi, times(1)).getReviewListByProductId(1L);
    }
    
    @Test
    @DisplayName("should create a Product")
    void shouldCreateAProduct () {
        when(productServiceImplApi.postProduct(product)).thenReturn(new ProductId().id(1L));
        when(productServiceImplApi.getApiClient()).thenReturn(new ApiClient());
        productCompositeService.createProduct(product);
        verify(productServiceImplApi, times(1)).postProduct(product);
    }

    @Test
    @DisplayName("should create a Product with Exception")
    void shouldCreateAProductException () {
        when(productServiceImplApi.postProduct(product)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        when(productServiceImplApi.getApiClient()).thenReturn(new ApiClient());
        Assertions.assertThrows(HttpClientErrorException.class, () -> {productCompositeService.createProduct(product);});
        verify(productServiceImplApi, times(1)).postProduct(product);
    }

    @Test
    @DisplayName("should create a Review")
    void shouldCreateTwoReviews () {
        when(reviewServiceImplApi.postReview(review)).thenReturn(new ReviewId().id(1L));
        when(reviewServiceImplApi.getApiClient()).thenReturn(new com.backbase.goldensample.review.api.client.ApiClient());
        productCompositeService.createReview(review);
        verify(reviewServiceImplApi, times(1)).postReview(review);
    }

    @Test
    @DisplayName("should create a Review with Exception")
    void shouldCreateAReviewsWithException () {
        when(reviewServiceImplApi.postReview(review)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        when(reviewServiceImplApi.getApiClient()).thenReturn(new com.backbase.goldensample.review.api.client.ApiClient());
        Assertions.assertThrows(HttpClientErrorException.class, () -> {productCompositeService.createReview(review);});
        verify(reviewServiceImplApi, times(1)).postReview(review);
    }

}
