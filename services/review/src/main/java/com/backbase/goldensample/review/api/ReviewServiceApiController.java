package com.backbase.goldensample.review.api;

import com.backbase.goldensample.review.service.ReviewService;
import com.backbase.reviews.api.service.v2.ReviewServiceApi;
import com.backbase.reviews.api.service.v2.model.Review;
import com.backbase.reviews.api.service.v2.model.ReviewId;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class <code>ReviewController</code> is the implementation of the main Review Endpoint API
 * definition.
 *
 * @see ReviewServiceApi
 */
@RestController
@Slf4j
public class ReviewServiceApiController implements ReviewServiceApi {

    /**
     * Review service business logic interface.
     */
    private final ReviewService reviewService;

    @Autowired
    public ReviewServiceApiController(final ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Override
    public ResponseEntity<Void> deleteReview(final Long reviewId) {
        if (log.isDebugEnabled()) {
            log.debug("Deleting Review with id {}", reviewId);
        }
        reviewService.deleteReview(reviewId);
        if (log.isDebugEnabled()) {
            log.debug("Review id {} removed", reviewId);
        }
        return ResponseEntity
            .noContent()
            .build();
    }

    @Override
    public ResponseEntity<Void> deleteReviewsByProductId(final Long productId) {
        if (log.isDebugEnabled()) {
            log.debug("Deleting Product {} Reviews", productId);
        }
        reviewService.deleteReviews(productId);
        if (log.isDebugEnabled()) {
            log.debug("Product {} Reviews removed", productId);
        }
        return ResponseEntity
            .noContent()
            .build();
    }

    @Override
    public ResponseEntity<List<Review>> getReviewListByProductId(final Long productId) {
        if (log.isDebugEnabled()) {
            log.debug("Get product {} reviews", productId);
        }
        final List<Review> listReview = reviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(listReview);
    }

    @Override
    public ResponseEntity<Review> getReviewById(final Long reviewId) {
        if (log.isDebugEnabled()) {
            log.debug("Get review id {}", reviewId);
        }
        final Review review = reviewService.getReview(reviewId);
        return ResponseEntity.ok(review);
    }

    @Override
    public ResponseEntity<ReviewId> postReview(@Valid final Review review) {
        if (log.isDebugEnabled()) {
            log.debug("creating review {}", review);
        }
        final Review reviewWithId = reviewService.createReview(review);
        if (log.isDebugEnabled()) {
            log.debug("review with id {} created", reviewWithId.getProductId());
        }
        final ReviewId reviewId = new ReviewId();
        reviewId.setId(reviewWithId.getReviewId());
        return ResponseEntity.ok(reviewId);
    }

    @Override
    public ResponseEntity<ReviewId> putReview(@Valid final Review review) {
        if (log.isDebugEnabled()) {
            log.debug("updating review {}", review);
        }
        final Review reviewWithId = reviewService.updateReview(review);
        if (log.isDebugEnabled()) {
            log.debug("review with id {} updated", reviewWithId.getProductId());
        }
        return ResponseEntity
            .noContent()
            .build();
    }
}
