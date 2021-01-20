package com.backbase.goldensample.store.service;

import com.backbase.goldensample.product.api.client.v2.ProductServiceApi;
import com.backbase.goldensample.product.api.client.v2.model.Product;
import com.backbase.goldensample.product.api.client.v2.model.ProductId;
import com.backbase.goldensample.review.api.client.v2.ReviewServiceApi;
import com.backbase.goldensample.review.api.client.v2.model.Review;
import com.backbase.goldensample.review.api.client.v2.model.ReviewId;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class ProductCompositeServiceImpl implements ProductCompositeService {
    private final ProductServiceApi productServiceImplApi;
    private final ReviewServiceApi  reviewServiceImplApi;
    @Getter
    private final ObjectMapper      mapper;

    @Override public Product getProduct(final long productId) {
        if (log.isDebugEnabled()) {
            log.debug("Will call the getProduct API on URL: {}", productServiceImplApi
                .getApiClient()
                .getBasePath());
        }
        final Product product = handleClientException(getProductF()).apply(productId);
        if (product != null && log.isDebugEnabled()) {
            log.debug("Found a product with id: {}", product.getProductId());
        }
        return product;
    }

    @Override public List<Review> getReviews(final long productId) {
        if (log.isDebugEnabled()) {
            log.debug("Will call the getReviews API on URL: {}", reviewServiceImplApi
                .getApiClient()
                .getBasePath());
        }
        final List<Review> reviews = handleClientException(getReviewsF()).apply(productId);
        if (log.isDebugEnabled()) {
            log.debug("Found {} reviews for a product with id: {}", reviews.size(), productId);
        }
        return reviews;
    }

    @Override public ProductId createProduct(final Product product) {
        if (log.isDebugEnabled()) {
            log.debug("Will post a new product to URL: {}", productServiceImplApi
                .getApiClient()
                .getBasePath());
        }
        final ProductId productId = handleClientException(createProduct()).apply(product);
        if (log.isDebugEnabled()) {
            log.debug("Created a product with id: {}", productId.getId());
        }
        return productId;
    }

    @Override public ReviewId createReview(final Review body) {
        if (log.isDebugEnabled()) {
            log.debug("Will post a new review to URL: {}", reviewServiceImplApi
                .getApiClient()
                .getBasePath());
        }
        final ReviewId reviewId = handleClientException(createReview()).apply(body);
        if (log.isDebugEnabled()) {
            log.debug("Created a review with id: {}", reviewId.getId());
        }
        return reviewId;
    }

    private Function<Long, Product> getProductF() {
        return productServiceImplApi::getProductById;
    }

    private Function<Long, List<Review>> getReviewsF() {
        return reviewServiceImplApi::getReviewListByProductId;
    }

    private Function<Product, ProductId> createProduct() {
        return productServiceImplApi::postProduct;
    }

    private Function<Review, ReviewId> createReview() {
        return reviewServiceImplApi::postReview;
    }

    @Override public Logger getLogger() {
        return log;
    }
}
