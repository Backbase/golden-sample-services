package com.backbase.goldensample.review.service;

import com.backbase.goldensample.review.dto.ReviewDTO;

/**
 * Interface to pull an average score and summary from a third party service.
 */
public interface ThirdPartyReviewAggregatorService {

    /**
     * Obtain an average score and review headline from a third party.
     *
     * @param productName name of the product to search
     * @return a product overview
     */
    ReviewDTO getAverageReview(String productName);

}
