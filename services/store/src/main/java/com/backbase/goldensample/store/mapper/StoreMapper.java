package com.backbase.goldensample.store.mapper;

import com.backbase.goldensample.store.api.service.v1.model.ProductAggregate;
import com.backbase.goldensample.store.domain.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StoreMapper {

    ProductAggregate map(Product source);

    Product map(ProductAggregate source);

}
