package com.backbase.goldensample.product.mapper;

import com.backbase.product.api.service.v2.model.Product;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductVersionMapper {

  Product v1ToV2(com.backbase.product.api.service.v1.model.Product v1s);

  com.backbase.product.api.service.v1.model.Product v2ToV1(Product api);

  List<Product> v1ListToV2List(List<com.backbase.product.api.service.v1.model.Product> v1List);

  List<com.backbase.product.api.service.v1.model.Product> v2ListToV1List(List<Product> api);
}
