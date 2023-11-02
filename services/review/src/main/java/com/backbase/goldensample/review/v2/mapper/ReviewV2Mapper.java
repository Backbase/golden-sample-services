package com.backbase.goldensample.review.v2.mapper;

import static org.mapstruct.ReportingPolicy.ERROR;

import com.backbase.goldensample.review.dto.ReviewDTO;
import com.backbase.reviews.api.service.v2.model.Review;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ERROR)
public interface ReviewV2Mapper {

  @Mapping(source = "id", target = "reviewId")
  Review dtoToApi(ReviewDTO dto);

  @Mapping(source = "reviewId", target = "id")
  ReviewDTO apiToDto(Review api);

  List<Review> dtoListToApiList(List<ReviewDTO> dto);

  List<ReviewDTO> apiListToDtoList(List<Review> api);
}
