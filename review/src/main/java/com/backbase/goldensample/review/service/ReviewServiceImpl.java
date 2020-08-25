package com.backbase.goldensample.review.service;


import com.backbase.goldensample.review.mapper.ReviewMapper;
import com.backbase.goldensample.review.persistence.ReviewEntity;
import com.backbase.goldensample.review.persistence.ReviewRepository;
import com.backbase.reviews.api.service.v2.model.Review;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service("ReviewServiceImpl")
@Log4j2
public class ReviewServiceImpl implements ReviewService {

  private final ReviewRepository repository;
  private final ReviewMapper mapper;

  @Autowired
  public ReviewServiceImpl(ReviewRepository repository, ReviewMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Review createReview(Review body) throws Exception {

//    isValidProductId(body.getProductId());

    try {
      ReviewEntity entity = mapper.apiToEntity(body);
      ReviewEntity newEntity = repository.save(entity);

      log.debug(
          "createReview: created a review entity: {}/{}", body.getProductId(), body.getReviewId());
      return mapper.entityToApi(newEntity);

    } catch (DataIntegrityViolationException dive) {

      throw new Exception("Invalid productId: " + body.getProductId());
//      throw new InvalidInputException(
//          "Duplicate key, Product Id: "
//              + body.getProductId()
//              + ", Review Id:"
//              + body.getReviewId());
    }
  }

  @Override
  public Review updateReview(Review body) throws Exception {
    ReviewEntity entity = mapper.apiToEntity(body);
    ReviewEntity newEntity = repository.save(entity);

    log.debug(
        "updateReview: update a review entity: {}/{}", body.getProductId(), body.getReviewId());
    return mapper.entityToApi(newEntity);
  }

  //  @Override
//  public List<Review> getReviews(long productId) {
//
//    List<Review> list = mapper.entityListToApiList(repository.findByProductId(productId));
//
//    log.debug("getReviews: response size: {}", list.size());
//
//    return list;
//  }

  @Override
  public List<Review> getReviewsByProductId(long productId) {

    List<Review> list = mapper.entityListToApiList(repository.findByProductId(productId));

    log.debug("getReviews: response size: {}", list.size());

    return list;
  }

  @Override
  public Review getReview(long reviewId) {

    Review review = mapper.entityToApi(repository.findById(reviewId).get());

    log.debug("getReview: {}", review.getReviewId());

    return review;
  }

  @Override
  public void deleteReviews(long productId) {
//    isValidProductId(productId);
    log.debug(
        "deleteReviews: tries to delete reviews for the product with productId: {}", productId);
    repository.deleteAll(repository.findByProductId(productId));
  }

  private void isValidProductId(long productId) throws Exception {
    if (productId < 1) throw new Exception("Invalid productId: " + productId);
  }

}
