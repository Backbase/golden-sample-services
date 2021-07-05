package com.backbase.goldensample.store.client;

import com.backbase.goldensample.review.api.client.v1.ReviewServiceApi;
import com.backbase.goldensample.store.domain.Review;
import com.backbase.goldensample.store.mapper.ReviewV1Mapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Adapts the internal domain to the model of the v1 Review api.
 *
 * @Deprecated. This class wraps the Deprecated v1 ReviewServiceApi. It will be replaced by the v2 ReviewServiceApi.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "api-version", prefix = "app.review-service", havingValue = "v1", matchIfMissing = true)
@Deprecated
public class ReviewClientV1Impl implements ReviewClient {

    private final ReviewServiceApi reviewServiceApi;
    private final ReviewV1Mapper reviewV1Mapper;

    @Override
    public List<Review> getReviewListByProductId(long productId) {
        log.debug("Will call the getReviews API on URL: {}", reviewServiceApi.getApiClient().getBasePath());
        return reviewV1Mapper.map(reviewServiceApi.getReviewListByProductId(productId));
    }

    @Override
    public long postReview(Review review) {
        log.debug("Will post a new review to URL: {}", reviewServiceApi.getApiClient().getBasePath());
        return reviewServiceApi.postReview(reviewV1Mapper.map(review)).getId();
    }
}
