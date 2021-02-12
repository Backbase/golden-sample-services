package com.backbase.goldensample.store.service.review;

import com.backbase.goldensample.review.api.client.v1.ReviewServiceApi;
import com.backbase.goldensample.store.domain.Review;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Adapts the internal domain to the model of the v1 Review api.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "api-version", prefix = "app.review-service", havingValue = "v1", matchIfMissing = true)
public class ReviewClient implements com.backbase.goldensample.store.service.ReviewClient {

    private final ReviewServiceApi reviewServiceApi;
    private final ReviewMapper reviewMapper;

    @Override
    public List<Review> getReviewListByProductId(long productId) {
        log.debug("Will call the getReviews API on URL: {}", reviewServiceApi.getApiClient().getBasePath());
        return reviewMapper.map(reviewServiceApi.getReviewListByProductId(productId));
    }

    @Override
    public long postReview(Review review) {
        log.debug("Will post a new review to URL: {}", reviewServiceApi.getApiClient().getBasePath());
        return reviewServiceApi.postReview(reviewMapper.map(review)).getId();
    }
}
