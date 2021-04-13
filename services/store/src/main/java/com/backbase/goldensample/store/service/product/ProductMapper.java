package com.backbase.goldensample.store.service.product;

import com.backbase.goldensample.store.domain.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    Product map(com.backbase.goldensample.product.api.client.v1.model.Product source);

    com.backbase.goldensample.product.api.client.v1.model.Product map(Product source);

}
