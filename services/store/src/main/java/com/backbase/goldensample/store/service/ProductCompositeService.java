package com.backbase.goldensample.store.service;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

import com.backbase.buildingblocks.presentation.errors.InternalServerErrorException;
import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.store.HttpErrorInfo;
import com.backbase.goldensample.store.domain.Product;
import com.backbase.goldensample.store.domain.Review;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    /**
     * Retrieve a product with its reviews.
     *
     * @param productId
     * @return
     */
    public Product retrieveProductWithReviews(long productId) {

        try {

            Product product = productClient.getProductById(productId);
            if (product == null) {
                return null;
            }
            log.debug("Found a product with id: {}", product.getProductId());

            List<Review> reviews = retrieveReviews(productId);
            product.setReviews(reviews);

            return product;
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    private List<Review> retrieveReviews(long productId) {

        try {
            List<Review> reviews = reviewClient.getReviewListByProductId(productId);

            log.debug("Found {} reviews for a product with id: {}", reviews.size(), productId);
            return reviews;

        } catch (HttpClientErrorException ex) {
            log.warn("Got an exception while requesting reviews, return zero reviews: {}", ex.getMessage());
            return new ArrayList<>();
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
