package com.backbase.goldensample.review.mapper;


import com.backbase.goldensample.review.dto.ReviewDTO;
import com.backbase.reviews.api.service.v1.model.Review;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Deprecated
@Mapper(componentModel = "spring")
public interface ReviewMapper {

  @Mapping(source = "id", target = "reviewId")
  Review dtoToApi(ReviewDTO dto);

  @Mapping(source = "reviewId", target = "id")
  ReviewDTO apiToDto(Review api);

  List<Review> dtoListToApiList(List<ReviewDTO> entity);

  List<ReviewDTO> apiListToDtoList(List<Review> api);
}
