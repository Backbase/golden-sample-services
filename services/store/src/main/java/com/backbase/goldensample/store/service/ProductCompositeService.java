package com.backbase.goldensample.store.service;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

import com.backbase.buildingblocks.presentation.errors.InternalServerErrorException;
import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.store.HttpErrorInfo;
import com.backbase.goldensample.store.client.ProductClient;
import com.backbase.goldensample.store.client.ReviewClient;
import com.backbase.goldensample.store.domain.Product;
import com.backbase.goldensample.store.domain.Review;
import com.backbase.goldensample.store.service.extension.ProductEnricher;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Business logic of building and storing a composite of product and reviews.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ProductCompositeService {

    private final ProductClient productClient;
    private final ReviewClient reviewClient;
    private final ObjectMapper mapper;
    private final ProductEnricher productEnricher;

    /**
     * Retrieve a product with its reviews.
     *
     * @param productId
     * @return
     */
    public Optional<Product> retrieveProductWithReviews(long productId) {
        try {
            Optional<Product> product = productClient.getProductById(productId);
            product.ifPresent(this::addReviews);
            product.ifPresent(productEnricher::enrichProduct);
            return product;
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    private void addReviews(Product product) {
        try {
            List<Review> reviews = reviewClient.getReviewListByProductId(product.getProductId());

            log.debug("Found {} reviews for a product with id: {}", reviews.size(), product.getProductId());
            product.setReviews(reviews);
        } catch (HttpClientErrorException ex) {
            log.warn("Got an exception while requesting reviews, return zero reviews: {}", ex.getMessage());
        }
    }

    /**
     * Stores the product and its reviews.
     *
     * @param product
     * @return
     */
    public Product createProductWithReviews(Product product) {

        try {
            long productId = productClient.postProduct(product);
            log.debug("Created a product with id: {}", productId);
            product.setProductId(productId);

            if (isEmpty(product.getReviews())) {
                return product;
            }
            product.getReviews().stream()
                .peek(r -> r.setProductId(productId))
                .forEach(this::storeReview);

            return product;
        } catch (
            HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }

    }

    private void storeReview(Review body) {
        try {
            long reviewId = reviewClient.postReview(body);
            log.debug("Created a review with id: {}", reviewId);
            body.setReviewId(reviewId);
        } catch (
            HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        switch (ex.getStatusCode()) {

            case NOT_FOUND:
                return new NotFoundException(getErrorMessage(ex));

            case BAD_REQUEST:
                // Our bad request cannot simply be blamed on the client that called us.
                return new InternalServerErrorException();

            default:
                log.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                log.warn("Error body: {}", ex.getResponseBodyAsString());
                return ex;
        }
    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }
}
