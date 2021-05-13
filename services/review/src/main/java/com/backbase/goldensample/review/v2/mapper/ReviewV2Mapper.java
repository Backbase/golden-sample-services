package com.backbase.goldensample.review.v2.mapper;


import com.backbase.goldensample.review.persistence.ReviewEntity;
import com.backbase.reviews.api.service.v2.model.Review;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewV2Mapper {

  @Mapping(source = "id", target = "reviewId")
  Review entityToApi(ReviewEntity entity);

  @Mapping(source = "reviewId", target = "id")
  ReviewEntity apiToEntity(Review api);

  List<Review> entityListToApiList(List<ReviewEntity> entity);

  List<ReviewEntity> apiListToEntityList(List<Review> api);
}
