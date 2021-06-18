package com.backbase.goldensample.store.mapper;

import com.backbase.goldensample.store.domain.Review;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", implementationName = "ReviewMapperV1")
public interface ReviewV1Mapper {

    @Mapping(target = "stars", ignore = true)
    Review map(com.backbase.goldensample.review.api.client.v1.model.Review source);

    List<Review> map(List<com.backbase.goldensample.review.api.client.v1.model.Review> source);

    com.backbase.goldensample.review.api.client.v1.model.Review map(Review source);

}
