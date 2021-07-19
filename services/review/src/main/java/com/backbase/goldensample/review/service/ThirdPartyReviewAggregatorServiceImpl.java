package com.backbase.goldensample.review.service;

import com.backbase.goldensample.review.dto.ReviewDTO;
import org.springframework.stereotype.Component;

@Component
public class ThirdPartyReviewAggregatorServiceImpl implements ThirdPartyReviewAggregatorService {

    @Override
    public ReviewDTO getAverageReview(String productName) {

        // could use a custom client and call another service to search for a product

        String provider = "Google Shopping";
        String totalReviews = "349";
        int averageScore = 4;

        ReviewDTO reviewDto = new ReviewDTO();
        reviewDto.setSubject(productName);
        reviewDto.setAuthor("Store Admin");
        reviewDto.setContent("Average score on " + provider + " based on " + totalReviews + " reviews for this product");
        reviewDto.setStars(averageScore);

        return reviewDto;
    }
}
