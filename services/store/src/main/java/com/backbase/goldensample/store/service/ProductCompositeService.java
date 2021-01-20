package com.backbase.goldensample.store.service;

import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.product.api.client.v2.ProductServiceApi;
import com.backbase.goldensample.product.api.client.v2.model.Product;
import com.backbase.goldensample.product.api.client.v2.model.ProductId;
import com.backbase.goldensample.review.api.client.v2.ReviewServiceApi;
import com.backbase.goldensample.review.api.client.v2.model.Review;
import com.backbase.goldensample.review.api.client.v2.model.ReviewId;
import com.backbase.goldensample.store.HttpErrorInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
@Slf4j
public class ProductCompositeService {

    private final ProductServiceApi productServiceImplApi;
    private final ReviewServiceApi  reviewServiceImplApi;
    private final ObjectMapper      mapper;

    @Autowired
    public ProductCompositeService(
        final ProductServiceApi productServiceImplApi,
        final ReviewServiceApi reviewServiceImplApi,
        final ObjectMapper mapper
    ) {
        this.productServiceImplApi = productServiceImplApi;
        this.reviewServiceImplApi = reviewServiceImplApi;
        this.mapper = mapper;
    }

    public Product getProduct(final long productId) {

        try {
            if (log.isDebugEnabled()) {
                log.debug("Will call the getProduct API on URL: {}", productServiceImplApi
                    .getApiClient()
                    .getBasePath());
            }

            final Product product = productServiceImplApi.getProductById(productId);
            if (product != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Found a product with id: {}", product.getProductId());
                }
            }

            return product;

        }
        catch (final HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public List<Review> getReviews(final long productId) {

        try {
            if (log.isDebugEnabled()) {
                log.debug("Will call the getReviews API on URL: {}", reviewServiceImplApi
                    .getApiClient()
                    .getBasePath());
            }
            final List<Review> reviews = reviewServiceImplApi.getReviewListByProductId(productId);

            if (log.isDebugEnabled()) {
                log.debug("Found {} reviews for a product with id: {}", reviews.size(), productId);
            }
            return reviews;

        }
        catch (final HttpClientErrorException ex) {
            if (log.isWarnEnabled()) {
                log.warn("Got an exception while requesting reviews, return zero reviews: {}", ex.getMessage());
            }
            return new ArrayList<>();
        }
    }

    public ProductId createProduct(final Product product) {

        try {
            if (log.isDebugEnabled()) {
                log.debug("Will post a new product to URL: {}", productServiceImplApi
                    .getApiClient()
                    .getBasePath());
            }
            final ProductId productId = productServiceImplApi.postProduct(product);
            if (log.isWarnEnabled()) {
                log.debug("Created a product with id: {}", productId.getId());
            }
            return productId;
        }
        catch (
            final HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }

    }

    public ReviewId createReview(final Review body) {

        try {
            if (log.isDebugEnabled()) {
                log.debug("Will post a new review to URL: {}", reviewServiceImplApi
                    .getApiClient()
                    .getBasePath());
            }
            final ReviewId reviewId = reviewServiceImplApi.postReview(body);
            if (log.isDebugEnabled()) {
                log.debug("Created a review with id: {}", reviewId.getId());
            }
            return reviewId;
        }
        catch (
            final HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    private RuntimeException handleHttpClientException(final HttpClientErrorException ex) {
        switch (ex.getStatusCode()) {

            case NOT_FOUND:
                return new NotFoundException(getErrorMessage(ex));

            default:
                if (log.isWarnEnabled()) {
                    log.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                    log.warn("Error body: {}", ex.getResponseBodyAsString());
                }
                return ex;
        }
    }

    private String getErrorMessage(final HttpClientErrorException ex) {
        try {
            return mapper
                .readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class)
                .getMessage();
        }
        catch (final IOException ioex) {
            return ex.getMessage();
        }
    }
}
