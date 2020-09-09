package com.backbase.goldensample.store.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import com.backbase.goldensample.product.api.client.ApiClient;
import com.backbase.goldensample.product.api.client.v2.ProductServiceApi;
import com.backbase.goldensample.product.api.client.v2.model.Product;
import com.backbase.goldensample.product.api.client.v2.model.ProductId;
import com.backbase.goldensample.review.api.client.v2.ReviewServiceApi;
import com.backbase.goldensample.review.api.client.v2.model.Review;
import com.backbase.goldensample.review.api.client.v2.model.ReviewId;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@ExtendWith(MockitoExtension.class)
class ProductCompositeServiceTest {

    private ProductCompositeService productCompositeService;

    @Mock
    private ProductServiceApi productServiceApi;

    @Mock
    private ReviewServiceApi reviewServiceApi;

    @Mock
    ObjectMapper objectMapper;

    private static final LocalDate TODAY = LocalDate.of(2020, 1, 28);

    private final Product product = new Product().productId(1L).name("Product").weight(20).createDate(TODAY);
    private final Review review = new Review().productId(1L).reviewId(1L).author("author").content("content").subject("subject");
    private final Review review2 = new Review().productId(1L).reviewId(2L).author("author").content("long content").subject("long subject");

    @BeforeEach
    public void init() {
        productCompositeService = new ProductCompositeService(productServiceApi, reviewServiceApi, objectMapper);
    }

    @Test
    @DisplayName("should retrieve a Product by Id")
    void getProduct() {
        when(productServiceApi.getProductById(1L)).thenReturn(product);
        when(productServiceApi.getApiClient()).thenReturn(new ApiClient());

        //test
        Product product1 = productCompositeService.getProduct(1L);

        assertAll(
            () -> assertEquals("Product", product1.getName()),
            () -> assertEquals(20, product1.getWeight()),
            () -> assertEquals(TODAY, product1.getCreateDate()));
        verify(productServiceApi, times(1)).getProductById(1L);
    }

    @Test
    @DisplayName("should retrieve a Product null")
    void getProductNull() {
        when(productServiceApi.getProductById(1L)).thenReturn(null);
        when(productServiceApi.getApiClient()).thenReturn(new ApiClient());

        //test
        Product product1 = productCompositeService.getProduct(1L);

        assertEquals(null, product1);
        verify(productServiceApi, times(1)).getProductById(1L);
    }

    @Test
    @DisplayName("should retrieve a HttpClientErrorException")
    void getProductHttpClientErrorException() {
        when(productServiceApi.getProductById(1L)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        when(productServiceApi.getApiClient()).thenReturn(new ApiClient());

        Assertions.assertThrows(HttpClientErrorException.class, () -> {productCompositeService.getProduct(1L);});
        verify(productServiceApi, times(1)).getProductById(1L);
    }

    @Test
    @DisplayName("should retrieve all the reviews from a Product")
    void getReviews() {
        when(reviewServiceApi.getReviewListByProductId(1L)).thenReturn(List.of(review, review2));
        when(reviewServiceApi.getApiClient()).thenReturn(new com.backbase.goldensample.review.api.client.ApiClient());
        
        List<Review> reviews = productCompositeService.getReviews(1L);

        assertEquals(2, reviews.size());
        verify(reviewServiceApi, times(1)).getReviewListByProductId(1L);
    }

    @Test
    @DisplayName("should retrieve an Exception")
    void getReviewsException() {
        when(reviewServiceApi.getReviewListByProductId(1L)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        when(reviewServiceApi.getApiClient()).thenReturn(new com.backbase.goldensample.review.api.client.ApiClient());

        List<Review> reviews = productCompositeService.getReviews(1L);

        assertEquals(0, reviews.size());

        verify(reviewServiceApi, times(1)).getReviewListByProductId(1L);
    }
    
    @Test
    @DisplayName("should create a Product")
    void shouldCreateAProduct () {
        when(productServiceApi.postProduct(product)).thenReturn(new ProductId().id(1L));
        when(productServiceApi.getApiClient()).thenReturn(new ApiClient());
        productCompositeService.createProduct(product);
        verify(productServiceApi, times(1)).postProduct(product);
    }

    @Test
    @DisplayName("should create a Product with Exception")
    void shouldCreateAProductException () {
        when(productServiceApi.postProduct(product)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        when(productServiceApi.getApiClient()).thenReturn(new ApiClient());
        Assertions.assertThrows(HttpClientErrorException.class, () -> {productCompositeService.createProduct(product);});
        verify(productServiceApi, times(1)).postProduct(product);
    }

    @Test
    @DisplayName("should create a Review")
    void shouldCreateTwoReviews () {
        when(reviewServiceApi.postReview(review)).thenReturn(new ReviewId().id(1L));
        when(reviewServiceApi.getApiClient()).thenReturn(new com.backbase.goldensample.review.api.client.ApiClient());
        productCompositeService.createReview(review);
        verify(reviewServiceApi, times(1)).postReview(review);
    }

    @Test
    @DisplayName("should create a Review with Exception")
    void shouldCreateAReviewsWithException () {
        when(reviewServiceApi.postReview(review)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        when(reviewServiceApi.getApiClient()).thenReturn(new com.backbase.goldensample.review.api.client.ApiClient());
        Assertions.assertThrows(HttpClientErrorException.class, () -> {productCompositeService.createReview(review);});
        verify(reviewServiceApi, times(1)).postReview(review);
    }

}
