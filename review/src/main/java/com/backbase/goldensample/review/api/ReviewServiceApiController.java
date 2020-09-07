package com.backbase.goldensample.review.api;

import com.backbase.goldensample.review.service.ReviewService;
import com.backbase.reviews.api.service.v2.ReviewServiceApi;
import com.backbase.reviews.api.service.v2.model.Review;
import com.backbase.reviews.api.service.v2.model.ReviewId;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class ReviewServiceApiController implements ReviewServiceApi {

  /** Review service business logic interface. */
  private final ReviewService reviewService;

  @Autowired
  public ReviewServiceApiController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }


  @Override
  public ResponseEntity<Void> deleteReview(Long reviewId) {
    this.reviewService.deleteReview(reviewId);

    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> deleteReviewsByProductId(Long productId) {
    this.reviewService.deleteReviews(productId);

    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<List<Review>> getReviewListByProductId(Long productId) {
    List<Review> listReview = reviewService.getReviewsByProductId(productId);
    return ResponseEntity.ok(listReview);
  }

  @Override
  public ResponseEntity<Review> getReviewById(Long reviewId) {
    Review review = reviewService.getReview(reviewId);
    return ResponseEntity.ok(review);
  }

  @Override
  public ResponseEntity<ReviewId> postReview(@Valid Review review) {
    Review reviewWithId = null;
    try {
      reviewWithId = reviewService.createReview(review);
    } catch (Exception e) {
      e.printStackTrace();
    }
    ReviewId reviewId = new ReviewId();
    reviewId.setId(reviewWithId.getReviewId());

    return ResponseEntity.ok(reviewId);
  }

  @Override
  public ResponseEntity<ReviewId> putReview(@Valid Review review) {
    try {
      Review reviewWithId = reviewService.updateReview(review);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return ResponseEntity.noContent().build();
  }
}
