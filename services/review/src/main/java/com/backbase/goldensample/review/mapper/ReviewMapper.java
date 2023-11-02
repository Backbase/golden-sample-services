package com.backbase.goldensample.review.mapper;


import static org.mapstruct.ReportingPolicy.ERROR;

import com.backbase.goldensample.review.dto.ReviewDTO;
import com.backbase.reviews.api.service.v1.model.Review;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Deprecated
@Mapper(componentModel = "spring", unmappedTargetPolicy = ERROR)
public interface ReviewMapper {

  @Mapping(source = "id", target = "reviewId")
  Review dtoToApi(ReviewDTO dto);

  @Mapping(source = "reviewId", target = "id")
  @Mapping(target = "stars", ignore = true)
  ReviewDTO apiToDto(Review api);

  List<Review> dtoListToApiList(List<ReviewDTO> entity);

  List<ReviewDTO> apiListToDtoList(List<Review> api);
}
