package com.backbase.goldensample.review.api;

import com.backbase.goldensample.review.service.ReviewService;
import com.backbase.reviews.api.service.v2.ReviewServiceImplApi;
import com.backbase.reviews.api.service.v2.model.Review;
import com.backbase.reviews.api.service.v2.model.ReviewId;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class <code>ReviewController</code> is the implementation of the main Review Endpoint API
 * definition.
 *
 * @see ReviewController
 */
@RestController
@Log4j2
public class ReviewController implements ReviewServiceImplApi {

  /** Review service business logic interface. */
  private final ReviewService reviewService;

  @Autowired
  public ReviewController(@Qualifier("ReviewServiceImpl") ReviewService reviewService) {
    this.reviewService = reviewService;
  }


  @Override
  public ResponseEntity<Void> deleteReview(Long reviewId) {
    return null;
  }

  @Override
  public ResponseEntity<Void> deleteReviewsByProductId(Long productId) {
    this.reviewService.deleteReviews(productId);

    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<List<Review>> getReviewListByProductId(Long productId) {
    List<Review> listReview = reviewService.getReviewsByProductId(productId);
    return ResponseEntity.ok(listReview);
  }

  @Override
  public ResponseEntity<Review> getReviewUsingGET(Long reviewId) {
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
    ReviewId productId = new ReviewId();
    productId.setId(reviewWithId.getReviewId());

    return ResponseEntity.ok(productId);
  }

  @Override
  public ResponseEntity<ReviewId> putReview(@Valid Review review) {
    try {
      Review productWithId = reviewService.updateReview(review);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return ResponseEntity.ok().build();
  }
}
