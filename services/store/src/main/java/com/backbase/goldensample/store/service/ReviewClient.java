package com.backbase.goldensample.store.service;

import com.backbase.goldensample.store.domain.Review;
import java.util.List;

/**
 * Adapts the internal domain to the model of the Review api.
 */
public interface ReviewClient {

    /**
     * Get list of reviews by product ids.
     *
     * @param productId
     * @return Empty list when nothing is found.
     */
    List<Review> getReviewListByProductId(long productId);

    /**
     * Stores a new review, returning the id.
     *
     * @param review
     * @return
     */
    long postReview(Review review);
}
