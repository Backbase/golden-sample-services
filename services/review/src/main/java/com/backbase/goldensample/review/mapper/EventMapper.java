package com.backbase.goldensample.review.mapper;

import com.backbase.goldensample.review.persistence.ReviewEntity;
import com.backbase.product.event.spec.v1.ProductCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(source = "name", target = "subject")
    @Mapping(target = "author", constant = "Store Admin")
    @Mapping(target = "content", constant = "we review and only add 5 star items to the store!")
    @Mapping(target = "stars", constant = "5")
    ReviewEntity eventToEntity(ProductCreatedEvent event);
}
