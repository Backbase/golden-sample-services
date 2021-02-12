package com.backbase.goldensample.store.service;

import com.backbase.goldensample.store.domain.Review;
import java.util.List;

public interface ReviewClient {

    List<Review> getReviewListByProductId(long productId);

    long postReview(Review review);
}
