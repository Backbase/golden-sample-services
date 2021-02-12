package com.backbase.goldensample.review.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backbase.goldensample.review.persistence.ReviewEntity;
import com.backbase.goldensample.review.persistence.ReviewRepository;
import com.backbase.reviews.api.service.v2.model.Review;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    private final Review review = new Review().reviewId(1L).productId(1L).author("author").subject("subject")
        .content("long content").stars(5);
    private final ReviewEntity reviewEntity = new ReviewEntity(1L, 1L, "author", "subject", "long content", 5);

    @BeforeEach
    public void init() {
        reviewService = new ReviewServiceImpl(reviewRepository);
    }

    @Test
    void getAllReviewsByProductTest() {
        List<ReviewEntity> list = new ArrayList<ReviewEntity>();
        ReviewEntity reviewEntity1 = new ReviewEntity(1L, "author", "subject", "content", 5);
        ReviewEntity reviewEntity2 = new ReviewEntity(1L, "angryauthor", "angry subject", "worst product ever", 1);

        list.add(reviewEntity1);
        list.add(reviewEntity2);

        when(reviewRepository.findByProductId(1L)).thenReturn(list);

        List<ReviewEntity> reviews = reviewService.getReviewsByProductId(1L);

        assertEquals(2, reviews.size());
        verify(reviewRepository, times(1)).findByProductId(1L);
    }

    @Test
    void getReviewByIdTest() {
        when(reviewRepository.findById(1L)).thenReturn(java.util.Optional.of(reviewEntity));

        ReviewEntity review = reviewService.getReview(1);

        assertAll(
            () -> assertEquals(1L, review.getId()),
            () -> assertEquals(1L, review.getProductId()),
            () -> assertEquals("author", review.getAuthor()),
            () -> assertEquals("subject", review.getSubject()),
            () -> assertEquals("long content", review.getContent()),
            () -> assertEquals(5, review.getStars()));
    }

    @Test
    void createReviewTest() {
        when(reviewRepository.save(any(ReviewEntity.class))).thenReturn(reviewEntity);
        reviewService.createReview(reviewEntity);
        verify(reviewRepository, times(1)).save(any(ReviewEntity.class));
    }

    @Test
    void updateReviewTest() {
        when(reviewRepository.save(any(ReviewEntity.class))).thenReturn(reviewEntity);
        reviewService.updateReview(reviewEntity);
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
