package com.backbase.goldensample.review.service;


import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.review.persistence.ReviewEntity;
import com.backbase.goldensample.review.persistence.ReviewRepository;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository repository) {
        this.repository = repository;
    }

    @Override
    public ReviewEntity createReview(ReviewEntity entity) {

        log.debug("creating review {}", entity);
        ReviewEntity newEntity = repository.save(entity);
        log.debug(
            "created a review entity: {}/{}", newEntity.getProductId(), newEntity.getId());
        return newEntity;

    }

    @Override
    public ReviewEntity updateReview(ReviewEntity entity) {
        log.debug("updating review {}", entity);
        ReviewEntity newEntity = repository.save(entity);

        log.debug(
            "update a review entity: {}/{}", newEntity.getProductId(), newEntity.getId());
        return newEntity;
    }

    @Override
    public List<ReviewEntity> getReviewsByProductId(long productId) {

        log.debug("get reviews by product id {}", productId);
        List<ReviewEntity> list = repository.findByProductId(productId);

        log.debug("response size: {}", list.size());

        return list;
    }

    @Override
    public ReviewEntity getReview(long reviewId) {

        log.debug("get review with id: {}", reviewId);

        return repository.findById(reviewId).orElseThrow(() -> new NotFoundException(String.format("Item is not found with id : '%s'", reviewId)));
    }

    @Override
    public void deleteReviews(long productId) {
        log.debug(
            "tries to delete reviews for the product with productId: {}", productId);
        repository.deleteAll(repository.findByProductId(productId));
    }

    /*
     Implementation is idempotent, that is,
     it will not report any failure if the entity is not found Always 200
    */
    @Override
    public void deleteReview(long reviewId) {
        log.debug(
            "tries to delete review with reviewId: {}", reviewId);
        repository.findById(reviewId).ifPresent(repository::delete);
    }

}
