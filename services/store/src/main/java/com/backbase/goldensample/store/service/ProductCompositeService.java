package com.backbase.goldensample.store.service;

import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.product.api.client.v2.model.Product;
import com.backbase.goldensample.product.api.client.v2.model.ProductId;
import com.backbase.goldensample.review.api.client.v2.model.Review;
import com.backbase.goldensample.review.api.client.v2.model.ReviewId;
import com.backbase.goldensample.store.HttpErrorInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import org.slf4j.Logger;
import org.springframework.web.client.HttpClientErrorException;

public interface ProductCompositeService {
    ObjectMapper getMapper();

    Logger getLogger();

    Product getProduct(long productId);

    List<Review> getReviews(long productId);

    ProductId createProduct(Product product);

    ReviewId createReview(Review body);

    default <T, R> Function<T, R> handleClientException(final Function<T, R> fn) {
        return t -> {
            try {
                return fn.apply(t);
            }
            catch (final HttpClientErrorException e) {
                getLogger().info("EXCEPTION IS {}!!!!", e);
                throw handleHttpClientException(e);
            }
        };
    }

    default String getErrorMessage(final HttpClientErrorException ex) {
        try {
            return getMapper()
                .readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class)
                .getMessage();
        }
        catch (final IOException ioex) {
            return ex.getMessage();
        }
    }

    default RuntimeException handleHttpClientException(final HttpClientErrorException ex) {
        switch (ex.getStatusCode()) {

            case NOT_FOUND:
                return new NotFoundException(getErrorMessage(ex));

            default:
                getLogger().warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                getLogger().warn("Error body: {}", ex.getResponseBodyAsString());
                return ex;
        }
    }
}
