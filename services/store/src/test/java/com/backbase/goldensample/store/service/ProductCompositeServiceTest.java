package com.backbase.goldensample.store.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backbase.buildingblocks.presentation.errors.InternalServerErrorException;
import com.backbase.goldensample.store.client.ProductClient;
import com.backbase.goldensample.store.client.ReviewClient;
import com.backbase.goldensample.store.domain.Product;
import com.backbase.goldensample.store.domain.Review;
import com.backbase.goldensample.store.service.extension.ProductEnricher;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    private ProductClient productClient;

    @Mock
    private ReviewClient reviewClient;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    private ProductEnricher productEnricher;

    private static final LocalDate TODAY = LocalDate.of(2020, 1, 28);

    private final Product product = createProduct(1L, "Product", 20, TODAY);
    private final Review review = createReview(1L, 1L, "author", "content", "subject");
    private final Review review2 = createReview(2L, 1L, "author", "long content", "long subject");

    @BeforeEach
    public void init() {
        productCompositeService = new ProductCompositeService(
                productClient, reviewClient, objectMapper, productEnricher);
    }

    @Test
    @DisplayName("should retrieve a Product by Id")
    void getProduct() {
        when(productClient.getProductById(1L)).thenReturn(Optional.of(product));
        when(reviewClient.getReviewListByProductId(1L)).thenReturn(List.of(review, review2));

        //test
        Product product1 = productCompositeService.retrieveProductWithReviews(1L).get();

        assertAll(
            () -> assertEquals("Product", product1.getName()),
            () -> assertEquals(20, product1.getWeight()),
            () -> assertEquals(TODAY, product1.getCreateDate()));
        assertEquals(2, product.getReviews().size());

        verify(productClient, times(1)).getProductById(1L);
        verify(reviewClient, times(1)).getReviewListByProductId(1L);
        verify(productEnricher, times(1)).enrichProduct(product);
    }

    @Test
    @DisplayName("should retrieve a Product null")
    void getProductNull() {
        when(productClient.getProductById(1L)).thenReturn(Optional.empty());

        //test
        Optional<Product> product1 = productCompositeService.retrieveProductWithReviews(1L);

        assertTrue(product1.isEmpty());
        verify(productClient, times(1)).getProductById(1L);
        verify(reviewClient, times(0)).getReviewListByProductId(1L);
        verify(productEnricher, times(0)).enrichProduct(any());
    }

    @Test
    @DisplayName("should retrieve a HttpClientErrorException")
    void getProductHttpClientErrorException() {
        when(productClient.getProductById(1L)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        Assertions.assertThrows(InternalServerErrorException.class, () -> {
            productCompositeService.retrieveProductWithReviews(1L);
        });
        verify(productClient, times(1)).getProductById(1L);
    }

    @Test
    @DisplayName("Should still retrieve the product when review retrieval fails")
    void getReviewsException() {
        when(productClient.getProductById(1L)).thenReturn(Optional.of(product));
        when(reviewClient.getReviewListByProductId(1L)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        Optional<Product> myProduct = productCompositeService.retrieveProductWithReviews(1L);
        assertTrue(myProduct.isPresent());
        List<Review> reviews = myProduct.get().getReviews();

        assertEquals(0, reviews.size());

        verify(reviewClient, times(1)).getReviewListByProductId(1L);
    }
    
    @Test
    @DisplayName("should create a Product")
    void shouldCreateAProduct () {
        when(productClient.postProduct(product)).thenReturn(1L);

        productCompositeService.createProductWithReviews(product);
        verify(productClient, times(1)).postProduct(product);
        verify(reviewClient, times(0)).postReview(any());
    }

    @Test
    @DisplayName("should not create a Product with Exception")
    void shouldCreateAProductException () {
        when(productClient.postProduct(product)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        Assertions.assertThrows(InternalServerErrorException.class, () -> {
            productCompositeService.createProductWithReviews(product);
        });
        verify(productClient, times(1)).postProduct(product);
    }

    @Test
    @DisplayName("should create a product with two reviews")
    void shouldCreateTwoReviews () {
        Product myProduct = createProduct(1L, "name", 2, TODAY);
        myProduct.setReviews(List.of(
            createReview(1L, 1L, "me", "very good", "I like ;-) ;-) ;-)"),
            createReview(1L, 1L, "me", "not so good", "Fails on startup")
        ));
        when(productClient.postProduct(myProduct)).thenReturn(1L);
        when(reviewClient.postReview(any(Review.class))).thenAnswer(a -> a.getArgument(0, Review.class).getReviewId());

        productCompositeService.createProductWithReviews(myProduct);
        verify(productClient, times(1)).postProduct(myProduct);
        verify(reviewClient, times(2)).postReview(any(Review.class));
    }

    @Test
    @DisplayName("should create a Review with Exception")
    void shouldCreateAReviewsWithException () {
        when(productClient.postProduct(any())).thenReturn(1L);
        when(reviewClient.postReview(review)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        Product myNewProduct = createProduct(1L, "new", 1, TODAY);
        myNewProduct.setReviews(Collections.singletonList(review));

        Assertions.assertThrows(InternalServerErrorException.class, () -> {
            productCompositeService.createProductWithReviews(myNewProduct);
        });
        verify(reviewClient, times(1)).postReview(review);
    }

    private com.backbase.goldensample.store.domain.Product createProduct(
            Long id, String name, Integer weight, LocalDate createDate) {

        com.backbase.goldensample.store.domain.Product product = new com.backbase.goldensample.store.domain.Product();
        product.setProductId(id);
        product.setName(name);
        product.setWeight(weight);
        product.setCreateDate(createDate);
        return product;
    }

    private com.backbase.goldensample.store.domain.Review createReview(
            Long reviewId, Long productId, String author, String subject, String content) {

        com.backbase.goldensample.store.domain.Review review = new com.backbase.goldensample.store.domain.Review();
        review.setReviewId(reviewId);
        review.setProductId(productId);
        review.setAuthor(author);
        review.setSubject(subject);
        review.setContent(content);
        return review;
    }

}
