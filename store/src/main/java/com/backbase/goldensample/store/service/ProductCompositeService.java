package com.backbase.goldensample.store.service;

import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.product.api.client.v2.ProductServiceImplApi;
import com.backbase.goldensample.product.api.client.v2.model.Product;
import com.backbase.goldensample.product.api.client.v2.model.ProductId;
import com.backbase.goldensample.review.api.client.v2.ReviewServiceImplApi;
import com.backbase.goldensample.review.api.client.v2.model.Review;
import com.backbase.goldensample.review.api.client.v2.model.ReviewId;
import com.backbase.goldensample.store.HttpErrorInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class ProductCompositeService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeService.class);

    private final ProductServiceImplApi productServiceImplApi;
    private final ReviewServiceImplApi reviewServiceImplApi;
    private final ObjectMapper mapper;

    @Autowired
    public ProductCompositeService(
        ProductServiceImplApi productServiceImplApi,
        ReviewServiceImplApi reviewServiceImplApi,
        ObjectMapper mapper
    ) {
        this.productServiceImplApi = productServiceImplApi;
        this.reviewServiceImplApi = reviewServiceImplApi;
        this.mapper = mapper;
    }

    public Product getProduct(long productId) {

        try {
            LOG.debug("Will call the getProduct API on URL: {}", productServiceImplApi.getApiClient().getBasePath());

            Product product = productServiceImplApi.getProductUsingGET(productId);
            if (product != null) {
                LOG.debug("Found a product with id: {}", product.getProductId());
            }

            return product;

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public List<Review> getReviews(long productId) {

        try {
            LOG.debug("Will call the getReviews API on URL: {}", reviewServiceImplApi.getApiClient().getBasePath());
            List<Review> reviews = reviewServiceImplApi.getReviewListByProductId(productId);

            LOG.debug("Found {} reviews for a product with id: {}", reviews.size(), productId);
            return reviews;

        } catch (HttpClientErrorException ex) {
            LOG.warn("Got an exception while requesting reviews, return zero reviews: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    public ProductId createProduct(Product product) {

        try {
            LOG.debug("Will post a new product to URL: {}", productServiceImplApi.getApiClient().getBasePath());
            ProductId productId = productServiceImplApi.postProduct(product);
            LOG.debug("Created a product with id: {}", productId.getId());
            return productId;
        } catch (
            HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }

    }

    public ReviewId createReview(Review body) {

        try {
            LOG.debug("Will post a new review to URL: {}", reviewServiceImplApi.getApiClient().getBasePath());
            ReviewId reviewId = reviewServiceImplApi.postReview(body);
            LOG.debug("Created a review with id: {}", reviewId.getId());
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
                LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                LOG.warn("Error body: {}", ex.getResponseBodyAsString());
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
