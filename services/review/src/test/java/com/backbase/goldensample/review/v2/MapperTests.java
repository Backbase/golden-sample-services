package com.backbase.goldensample.review.v2;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.backbase.goldensample.review.dto.ReviewDTO;
import com.backbase.goldensample.review.v2.mapper.ReviewV2Mapper;
import com.backbase.reviews.api.service.v2.model.Review;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class MapperTests {

    private final ReviewV2Mapper mapper = Mappers
        .getMapper(ReviewV2Mapper.class);

    @Test
    void mapperTests() {

        assertNotNull(mapper);

        Review review =
            new Review().productId(1L).reviewId(1L).author("author").content("long content").subject("subject").stars(5);

        ReviewDTO dto = mapper.apiToDto(review);

        assertAll(() -> assertEquals(review.getProductId(), dto.getProductId(), "Product ID is different"),
            () -> assertEquals(review.getReviewId(), dto.getId(), "Review ID is different"),
            () -> assertEquals(review.getAuthor(), dto.getAuthor(), "Author is different"),
            () -> assertEquals(review.getSubject(), dto.getSubject(), "Subject is different"),
            () -> assertEquals(review.getContent(), dto.getContent(), "Content is different"),
            () -> assertEquals(review.getStars(), dto.getStars(), "Stars is different"));

        Review api2 = mapper.dtoToApi(dto);

        assertAll(() -> assertEquals(review.getProductId(), api2.getProductId(), "Product ID is different"),
            () -> assertEquals(review.getReviewId(), api2.getReviewId(), "Review ID is different"),
            () -> assertEquals(review.getAuthor(), api2.getAuthor(), "Author is different"),
            () -> assertEquals(review.getSubject(), api2.getSubject(), "Subject is different"),
            () -> assertEquals(review.getContent(), api2.getContent(), "Content is different"),
            () -> assertEquals(review.getStars(), api2.getStars(), "Stars is different"));
    }

    @Test
    void mapperListTests() {

        assertNotNull(mapper);

        Review review =
            new Review().productId(1L).reviewId(1L).author("author").content("long content").subject("subject").stars(4);
        List<Review> apiList = Collections.singletonList(review);

        List<ReviewDTO> dtoList = mapper.apiListToDtoList(apiList);
        assertEquals(apiList.size(), dtoList.size());

        ReviewDTO dto = dtoList.get(0);

        assertAll(() -> assertEquals(review.getProductId(), dto.getProductId(), "Product ID is different"),
            () -> assertEquals(review.getReviewId(), dto.getId(), "Review ID is different"),
            () -> assertEquals(review.getAuthor(), dto.getAuthor(), "Author is different"),
            () -> assertEquals(review.getSubject(), dto.getSubject(), "Subject is different"),
            () -> assertEquals(review.getContent(), dto.getContent(), "Content is different"),
            () -> assertEquals(review.getStars(), dto.getStars(), "Stars is different"));

        List<Review> api2List = mapper.dtoListToApiList(dtoList);
        assertEquals(apiList.size(), api2List.size());

        Review api2 = api2List.get(0);

        assertAll(() -> assertEquals(review.getProductId(), api2.getProductId(), "Product ID is different"),
            () -> assertEquals(review.getReviewId(), api2.getReviewId(), "Review ID is different"),
            () -> assertEquals(review.getAuthor(), api2.getAuthor(), "Author is different"),
            () -> assertEquals(review.getSubject(), api2.getSubject(), "Subject is different"),
            () -> assertEquals(review.getContent(), api2.getContent(), "Content is different"),
            () -> assertEquals(review.getStars(), api2.getStars(), "Stars is different"));
    }
}
