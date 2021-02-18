package com.backbase.goldensample.store.service;

import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.product.api.client.v1.ProductServiceApi;
import com.backbase.goldensample.product.api.client.v1.model.Product;
import com.backbase.goldensample.product.api.client.v1.model.ProductId;
import com.backbase.goldensample.review.api.client.v1.ReviewServiceApi;
import com.backbase.goldensample.review.api.client.v1.model.Review;
import com.backbase.goldensample.review.api.client.v1.model.ReviewId;
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
    private final ReviewServiceApi reviewServiceImplApi;
    private final ObjectMapper mapper;

    @Autowired
    public ProductCompositeService(
        ProductServiceApi productServiceImplApi,
        ReviewServiceApi reviewServiceImplApi,
        ObjectMapper mapper
    ) {
        this.productServiceImplApi = productServiceImplApi;
        this.reviewServiceImplApi = reviewServiceImplApi;
        this.mapper = mapper;
    }

    public Product getProduct(long productId) {

        try {
            log.debug("Will call the getProduct API on URL: {}", productServiceImplApi.getApiClient().getBasePath());

            Product product = productServiceImplApi.getProductById(productId);
            if (product != null) {
                log.debug("Found a product with id: {}", product.getProductId());
            }

            return product;

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public List<Review> getReviews(long productId) {

        try {
            log.debug("Will call the getReviews API on URL: {}", reviewServiceImplApi.getApiClient().getBasePath());
            List<Review> reviews = reviewServiceImplApi.getReviewListByProductId(productId);

            log.debug("Found {} reviews for a product with id: {}", reviews.size(), productId);
            return reviews;

        } catch (HttpClientErrorException ex) {
            log.warn("Got an exception while requesting reviews, return zero reviews: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    public ProductId createProduct(Product product) {

        try {
            log.debug("Will post a new product to URL: {}", productServiceImplApi.getApiClient().getBasePath());
            ProductId productId = productServiceImplApi.postProduct(product);
            log.debug("Created a product with id: {}", productId.getId());
            return productId;
        } catch (
            HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }

    }

    public ReviewId createReview(Review body) {

        try {
            log.debug("Will post a new review to URL: {}", reviewServiceImplApi.getApiClient().getBasePath());
            ReviewId reviewId = reviewServiceImplApi.postReview(body);
            log.debug("Created a review with id: {}", reviewId.getId());
            return reviewId;
        } catch (
            HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        switch (ex.getStatusCode()) {

            case NOT_FOUND:
                return new NotFoundException(getErrorMessage(ex));

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
