package com.backbase.goldensample.store.mapper;

import static org.mapstruct.ReportingPolicy.ERROR;

import com.backbase.goldensample.store.api.service.v1.model.ProductAggregate;
import com.backbase.goldensample.store.api.service.v1.model.ReviewSummary;
import com.backbase.goldensample.store.domain.Product;
import com.backbase.goldensample.store.domain.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ERROR)
public interface StoreMapper {

    ProductAggregate map(Product source);

    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "stars", ignore = true)
    Review map(ReviewSummary review);

    Product map(ProductAggregate source);

}
