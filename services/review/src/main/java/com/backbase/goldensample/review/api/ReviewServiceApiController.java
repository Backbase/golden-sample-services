package com.backbase.goldensample.review.api;

import com.backbase.goldensample.review.dto.ReviewDTO;
import com.backbase.goldensample.review.mapper.ReviewMapper;
import com.backbase.goldensample.review.service.ReviewService;
import com.backbase.reviews.api.service.v1.ReviewServiceApi;
import com.backbase.reviews.api.service.v1.model.Review;
import com.backbase.reviews.api.service.v1.model.ReviewId;
import java.util.List;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class <code>ReviewController</code> is the implementation of the main Review Endpoint API
 * definition.
 *
 * @see ReviewServiceApi
 *
 * @deprecated Will be removed next major released. Use {@link ReviewServiceApiController} instead.
 */

@Deprecated
@RestController
@Log4j2
public class ReviewServiceApiController implements ReviewServiceApi {

  /** Review service business logic interface. */
  private final ReviewService reviewService;
  private final ReviewMapper mapper;

  @Autowired
  public ReviewServiceApiController(ReviewService reviewService, ReviewMapper mapper) {
    this.reviewService = reviewService;
    this.mapper = mapper;
  }


  @Override
  public ResponseEntity<Void> deleteReview(Long reviewId) {
    log.debug("Deleting Review with id {}", reviewId);
    this.reviewService.deleteReview(reviewId);
    log.debug("Review id {} removed", reviewId);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> deleteReviewsByProductId(Long productId) {
    log.debug("Deleting Product {} Reviews", productId);
    this.reviewService.deleteReviews(productId);
    log.debug("Product {} Reviews removed", productId);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<List<Review>> getReviewListByProductId(Long productId) {
    log.debug("Get product {} reviews", productId);
    List<Review> listReview = mapper.dtoListToApiList(reviewService.getReviewsByProductId(productId));
    return ResponseEntity.ok(listReview);
  }

  @Override
  public ResponseEntity<Review> getReviewById(Long reviewId) {
    log.debug("Get review id {}", reviewId);
    Review review = mapper.dtoToApi(reviewService.getReview(reviewId));
    return ResponseEntity.ok(review);
  }

  @Override
  public ResponseEntity<ReviewId> postReview(@Valid Review review) {
    log.debug("creating review {}", review);
    ReviewDTO dto = mapper.apiToDto(review);
    ReviewDTO reviewWithId = reviewService.createReview(dto);
    log.debug("review with id {} created", reviewWithId.getProductId());
    ReviewId reviewId = new ReviewId();
    reviewId.setId(reviewWithId.getId());
    return ResponseEntity.ok(reviewId);
  }

  @Override
  public ResponseEntity<ReviewId> putReview(@Valid Review review) {
    log.debug("updating review {}", review);
    ReviewDTO dto = mapper.apiToDto(review);
    ReviewDTO reviewWithId = reviewService.updateReview(dto);
    log.debug("review with id {} updated", reviewWithId.getProductId());
    return ResponseEntity.noContent().build();
  }
}
