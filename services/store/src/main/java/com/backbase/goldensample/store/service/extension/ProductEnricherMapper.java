package com.backbase.goldensample.store.service.extension;

import com.backbase.goldensample.store.domain.Product;
import com.backbase.goldensample.store.domain.Review;
import com.backbase.goldensample.store.service.extension.api.client.v2.model.ProductAggregate;
import com.backbase.goldensample.store.service.extension.api.client.v2.model.ReviewSummary;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ProductEnricherMapper {

    ProductAggregate map(Product product);

    @Mapping(target = "productId", ignore = true)
    Review map(ReviewSummary reviewSummary);

    void map(@MappingTarget Product product, ProductAggregate productAggregate);

    ReviewSummary map(Review review);

    void map(@MappingTarget ReviewSummary reviewSummary, Review review);
}
