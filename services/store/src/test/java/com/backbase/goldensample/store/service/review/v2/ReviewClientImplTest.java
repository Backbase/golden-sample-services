package com.backbase.goldensample.store.service.review.v2;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backbase.goldensample.review.api.client.ApiClient;
import com.backbase.goldensample.review.api.client.v2.ReviewServiceApi;
import com.backbase.goldensample.review.api.client.v2.model.ReviewId;
import com.backbase.goldensample.store.domain.Review;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@ExtendWith(MockitoExtension.class)
class ReviewClientImplTest {

    private static final long PRODUCT_ID = 123;

    private ReviewClientImpl reviewClientImpl;

    @Mock
    private ReviewServiceApi reviewServiceApi;

    @Mock
    private ReviewMapper reviewMapper;

    private static final Review DOMAIN_REVIEW = new Review();
    private static final com.backbase.goldensample.review.api.client.v2.model.Review MODEL_REVIEW =
        new com.backbase.goldensample.review.api.client.v2.model.Review();

    @BeforeEach
    void init() {
        when(reviewServiceApi.getApiClient()).thenReturn(new ApiClient().setBasePath("/base/path"));
        reviewClientImpl = new ReviewClientImpl(reviewServiceApi, reviewMapper);
    }

    @Test
    void getReviewsById() {
        when(reviewServiceApi.getReviewListByProductId(PRODUCT_ID)).thenReturn(List.of(MODEL_REVIEW));
        when(reviewMapper.map(anyList())).thenReturn(List.of(DOMAIN_REVIEW));

        List<Review> reviews = reviewClientImpl.getReviewListByProductId(PRODUCT_ID);
        verify(reviewServiceApi, times(1)).getReviewListByProductId(PRODUCT_ID);
        verify(reviewMapper, times(1)).map(anyList());
    }

    @Test
    void getReviewByIdNotFoundReturnsEmptyList() {
        when(reviewServiceApi.getReviewListByProductId(PRODUCT_ID)).thenReturn(emptyList());

        List<Review> reviews = reviewClientImpl.getReviewListByProductId(PRODUCT_ID);
        assertNotNull(reviews);
        assertTrue(reviews.isEmpty());
        verify(reviewServiceApi, times(1)).getReviewListByProductId(PRODUCT_ID);
        verify(reviewMapper, times(0)).map(MODEL_REVIEW);
    }

    @Test
    void getReviewByIdOtherErrorThrows() {
        when(reviewServiceApi.getReviewListByProductId(PRODUCT_ID))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        assertThrows(HttpClientErrorException.class, () -> {
            reviewClientImpl.getReviewListByProductId(PRODUCT_ID);
        });

        verify(reviewServiceApi, times(1)).getReviewListByProductId(PRODUCT_ID);
        verify(reviewMapper, times(0)).map(MODEL_REVIEW);
    }

    @Test
    void postReview() {
        when(reviewServiceApi.postReview(MODEL_REVIEW)).thenReturn(new ReviewId().id(1L));
        when(reviewMapper.map(DOMAIN_REVIEW)).thenReturn(MODEL_REVIEW);

        long reviewId = reviewClientImpl.postReview(DOMAIN_REVIEW);
        assertEquals(1L, reviewId);
        verify(reviewMapper, times(1)).map(DOMAIN_REVIEW);
    }
}