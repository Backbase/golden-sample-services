package com.backbase.goldensample.store.service;

import com.backbase.goldensample.product.api.client.v2.ProductServiceImplApi;
import com.backbase.goldensample.product.api.client.v2.model.Product;
import com.backbase.goldensample.review.api.client.v2.ReviewServiceImplApi;
import com.backbase.goldensample.review.api.client.v2.model.Review;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

            LOG.debug("Will call the getProduct API on URL: {}", productServiceImplApi.getApiClient());

            Product product = productServiceImplApi.getProductUsingGET(productId);
            if (product != null) {
                LOG.debug("Found a product with id: {}", product.getProductId());
            }

            return product;
    }

    public List<Review> getReviews(long productId) {

        try {
            LOG.debug("Will call the getReviews API on URL: {}", reviewServiceImplApi.getApiClient().getBasePath());
            List<Review> reviews = reviewServiceImplApi.getReviewListByProductId(productId);

            LOG.debug("Found {} reviews for a product with id: {}", reviews.size(), productId);
            return reviews;

        } catch (Exception ex) {
            LOG.warn("Got an exception while requesting reviews, return zero reviews: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }



}
