package com.backbase.goldensample.review.service;


import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.review.mapper.ReviewMapper;
import com.backbase.goldensample.review.persistence.ReviewEntity;
import com.backbase.goldensample.review.persistence.ReviewRepository;
import com.backbase.reviews.api.service.v2.model.Review;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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
    public Review createReview(Review review) {

        ReviewEntity entity = mapper.apiToEntity(review);
        ReviewEntity newEntity = repository.save(entity);

        log.debug(
            "createReview: created a review entity: {}/{}", newEntity.getProductId(), newEntity.getId());
        return mapper.entityToApi(newEntity);

    }

    @Override
    public Review updateReview(Review review) {
        ReviewEntity entity = mapper.apiToEntity(review);
        ReviewEntity newEntity = repository.save(entity);

        log.debug(
            "updateReview: update a review entity: {}/{}", newEntity.getProductId(), newEntity.getId());
        return mapper.entityToApi(newEntity);
    }

    @Override
    public List<Review> getReviewsByProductId(long productId) {

        List<Review> list = mapper.entityListToApiList(repository.findByProductId(productId));

        log.debug("getReviews: response size: {}", list.size());

        return list;
    }

    @Override
    public Review getReview(long reviewId) {

        log.debug("getReview: {}", reviewId);

        return repository.findById(reviewId).map(mapper::entityToApi)
            .orElseThrow(() -> new NotFoundException(String.format("Item is not found with id : '%s'", reviewId)));
    }

    @Override
    public void deleteReviews(long productId) {
        log.debug(
            "deleteReviews: tries to delete reviews for the product with productId: {}", productId);
        repository.deleteAll(repository.findByProductId(productId));
    }

    /*
     Implementation is idempotent, that is,
     it will not report any failure if the entity is not found Always 200
    */
    @Override
    public void deleteReview(long reviewId) {
        log.debug(
            "deleteReview: tries to delete review with reviewId: {}", reviewId);
        repository.findById(reviewId).ifPresent(repository::delete);
    }

}
