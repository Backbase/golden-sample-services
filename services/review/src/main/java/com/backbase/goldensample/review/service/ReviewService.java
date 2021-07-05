package com.backbase.goldensample.review.service;


import com.backbase.goldensample.review.persistence.ReviewEntity;
import java.util.List;

/**
 * Interface that define the general service contract (methods) for the Review
 *
 * <ol>
 *   <li>Service and,
 *   <li>Controller interfaces.
 * </ol>
 *
 * @author backbase
 * @version v4.0
 * @since v0.1
 */
public interface ReviewService {

    /**
     * Get all reviews for specific product by product id.
     *
     * @param productId that you are looking for its reviews.
     * @return list of reviews for this product, or empty list if there are no reviews.
     */
    default List<ReviewEntity> getReviewsByProductId(long productId) {
        return List.of();
    }

    /**
     * Create a new review for a product.
     *
     * @param body review to be created.
     * @return just created review.
     */
    default ReviewEntity createReview(
        ReviewEntity body) {
        return null;
    }

    /**
     * Create a new review for a product.
     *
     * @param body review to be created.
     * @return just created review.
     */
    default ReviewEntity updateReview(
        ReviewEntity body) {
        return null;
    }

    /**
     * Delete all product reviews.
     *
     * @param productId to delete its reviews.
     */
    default void deleteReviews(long productId) {
    }

    /**
     * Get a review per Id
     *
     * @param reviewId to get the review
     * @return
     */
    default ReviewEntity getReview(long reviewId) {
        return null;
    }

    /**
     * Delete a review per Id
     *
     * @param reviewId to get deleted
     */
    default void deleteReview(long reviewId) {
    }
}
