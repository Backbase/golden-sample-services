package com.backbase.goldensample.review.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import com.backbase.goldensample.review.mapper.ReviewMapper;
import com.backbase.goldensample.review.persistence.ReviewEntity;
import com.backbase.goldensample.review.persistence.ReviewRepository;
import com.backbase.reviews.api.service.v2.model.Review;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Spy
    ReviewMapper reviewMapper = Mappers.getMapper(ReviewMapper.class);

    private final Review review = new Review().reviewId(1L).productId(1L).author("author").subject("subject").content("long content");
    private final ReviewEntity reviewEntity = new ReviewEntity(1L,1L, "author", "subject", "long content");

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        reviewService = new ReviewServiceImpl(reviewRepository, reviewMapper);

    }

    @Test
    void getAllReviewsByProductTest() {
        List<ReviewEntity> list = new ArrayList<ReviewEntity>();
        ReviewEntity reviewEntity1 = new ReviewEntity(1L, "author", "subject", "content");
        ReviewEntity reviewEntity2 = new ReviewEntity(1L, "angryauthor", "angry subject", "worst product ever");

        list.add(reviewEntity1);
        list.add(reviewEntity2);

        when(reviewRepository.findByProductId(1L)).thenReturn(list);

        //test
        List<Review> reviews = reviewService.getReviewsByProductId(1L);

        assertEquals(2, reviews.size());
        verify(reviewRepository, times(1)).findByProductId(1L);
    }

    @Test
    void getReviewByIdTest() {
        when(reviewRepository.findById(1L)).thenReturn(java.util.Optional.of(reviewEntity));

        Review review = reviewService.getReview(1);

        assertAll(
            () -> assertEquals(1L, review.getReviewId()),
            () -> assertEquals(1L, review.getProductId()),
            () -> assertEquals("author", review.getAuthor()),
            () -> assertEquals("subject", review.getSubject()),
            () -> assertEquals("long content", review.getContent()));
    }

    @Test
    void createReviewTest() {
        when(reviewRepository.save(any(ReviewEntity.class))).thenReturn(reviewEntity);
        reviewService.createReview(review);
        verify(reviewRepository, times(1)).save(any(ReviewEntity.class));
    }

    @Test
    void updateReviewTest() {
        when(reviewRepository.save(any(ReviewEntity.class))).thenReturn(reviewEntity);
        reviewService.updateReview(review);
        verify(reviewRepository, times(1)).save(any(ReviewEntity.class));
    }

    @Test
    void deleteReviewsByProductId() {
        reviewService.deleteReviews(review.getProductId());
        verify(reviewRepository, times(1)).deleteAll(any(List.class));
    }

    @Test
    void deleteReviewsById() {
        when(reviewRepository.findById(1L)).thenReturn(java.util.Optional.of(reviewEntity));
        reviewService.deleteReview(review.getReviewId());
        verify(reviewRepository, times(1)).delete(any(ReviewEntity.class));
    }
}
