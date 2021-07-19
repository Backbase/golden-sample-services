package com.backbase.goldensample.review.service;


import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.review.dto.ReviewDTO;
import com.backbase.goldensample.review.mapper.EntityMapper;
import com.backbase.goldensample.review.persistence.ReviewEntity;
import com.backbase.goldensample.review.persistence.ReviewRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;
    private final EntityMapper mapper;

    @Autowired
    public ReviewServiceImpl(ReviewRepository repository, EntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ReviewDTO createReview(ReviewDTO dto) {

        log.debug("creating review {}", dto);
        ReviewEntity newEntity = repository.save(mapper.dtoToEntity(dto));
        log.debug(
            "created a review entity: {}/{}", newEntity.getProductId(), newEntity.getId());
        return mapper.entityToDto(newEntity);

    }

    @Override
    public ReviewDTO updateReview(ReviewDTO dto) {
        log.debug("updating review {}", dto);
        ReviewEntity newEntity = repository.save(mapper.dtoToEntity(dto));

        log.debug(
            "update a review entity: {}/{}", newEntity.getProductId(), newEntity.getId());
        return mapper.entityToDto(newEntity);
    }

    @Override
    public List<ReviewDTO> getReviewsByProductId(long productId) {

        log.debug("get reviews by product id {}", productId);
        List<ReviewEntity> list = repository.findByProductId(productId);

        log.debug("response size: {}", list.size());

        return mapper.entityListToDtoList(list);
    }

    @Override
    public ReviewDTO getReview(long reviewId) {

        log.debug("get review with id: {}", reviewId);

        return mapper.entityToDto(
            repository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(String.format("Item is not found with id : '%s'", reviewId))));
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
