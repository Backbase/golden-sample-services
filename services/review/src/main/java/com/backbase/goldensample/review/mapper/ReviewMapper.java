package com.backbase.goldensample.review.mapper;


import com.backbase.goldensample.review.persistence.ReviewEntity;
import com.backbase.reviews.api.service.v1.model.Review;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Deprecated
@Mapper(componentModel = "spring")
public interface ReviewMapper {

  @Mapping(source = "id", target = "reviewId")
  Review entityToApi(ReviewEntity entity);

  @Mapping(source = "reviewId", target = "id")
  ReviewEntity apiToEntity(Review api);

  List<Review> entityListToApiList(List<ReviewEntity> entity);

  List<ReviewEntity> apiListToEntityList(List<Review> api);
}
