package com.backbase.goldensample.store.service.review.v2;

import com.backbase.goldensample.store.domain.Review;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", implementationName = "ReviewMapperV2")
public interface ReviewMapper {

    Review map(com.backbase.goldensample.review.api.client.v2.model.Review source);

    List<Review> map(List<com.backbase.goldensample.review.api.client.v2.model.Review> source);

    com.backbase.goldensample.review.api.client.v2.model.Review map(Review source);

}
