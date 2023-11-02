package com.backbase.goldensample.store.mapper;

import static org.mapstruct.ReportingPolicy.ERROR;

import com.backbase.goldensample.store.domain.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ERROR)
public interface ProductMapper {

    @Mapping(target = "reviews", ignore = true)
    Product map(com.backbase.goldensample.product.api.client.v1.model.Product source);

    com.backbase.goldensample.product.api.client.v1.model.Product map(Product source);

}
