package com.backbase.goldensample.review;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import com.backbase.goldensample.review.mapper.ReviewMapper;
import com.backbase.goldensample.review.persistence.ReviewEntity;
import com.backbase.reviews.api.service.v2.model.Review;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class MapperTests {

  private final ReviewMapper mapper = ReviewMapper.INSTANCE;

  @Test
  public void mapperTests() {

    assertNotNull(mapper);

    Review review = new Review();
    review.setProductId(1L);
    review.setReviewId(1L);
    review.setAuthor("author");
    review.setContent("long content");
    review.setSubject("subject");

    ReviewEntity entity = mapper.apiToEntity(review);

    assertEquals(review.getProductId(), entity.getProductId());
    assertEquals(review.getReviewId(), entity.getId());
    assertEquals(review.getAuthor(), entity.getAuthor());
    assertEquals(review.getSubject(), entity.getSubject());
    assertEquals(review.getContent(), entity.getContent());

    Review api2 = mapper.entityToApi(entity);

    assertEquals(review.getProductId(), api2.getProductId());
    assertEquals(review.getReviewId(), api2.getReviewId());
    assertEquals(review.getAuthor(), api2.getAuthor());
    assertEquals(review.getSubject(), api2.getSubject());
    assertEquals(review.getContent(), api2.getContent());
  }

  @Test
  public void mapperListTests() {

    assertNotNull(mapper);

    Review review = new Review();
    review.setProductId(1L);
    review.setReviewId(1L);
    review.setAuthor("author");
    review.setContent("long content");
    review.setSubject("subject");
    List<Review> apiList = Collections.singletonList(review);

    List<ReviewEntity> entityList = mapper.apiListToEntityList(apiList);
    assertEquals(apiList.size(), entityList.size());

    ReviewEntity entity = entityList.get(0);

    assertEquals(review.getProductId(), entity.getProductId());
    assertEquals(review.getReviewId(), entity.getId());
    assertEquals(review.getAuthor(), entity.getAuthor());
    assertEquals(review.getSubject(), entity.getSubject());
    assertEquals(review.getContent(), entity.getContent());

    List<Review> api2List = mapper.entityListToApiList(entityList);
    assertEquals(apiList.size(), api2List.size());

    Review api2 = api2List.get(0);

    assertEquals(review.getProductId(), api2.getProductId());
    assertEquals(review.getReviewId(), api2.getReviewId());
    assertEquals(review.getAuthor(), api2.getAuthor());
    assertEquals(review.getSubject(), api2.getSubject());
    assertEquals(review.getContent(), api2.getContent());
  }
}
