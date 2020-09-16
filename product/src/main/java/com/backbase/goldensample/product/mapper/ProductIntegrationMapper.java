package com.backbase.goldensample.product.mapper;

import com.backbase.product.api.service.v2.model.Product;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductIntegrationMapper {

  com.backbase.product.api.integration.v2.model.Product fromApiToIntegration(Product v1s);

  Product fromIntegrationToApi(com.backbase.product.api.integration.v2.model.Product api);

  List<Product> integrationListToApiList(List<com.backbase.product.api.integration.v2.model.Product> v1List);

  List<com.backbase.product.api.integration.v2.model.Product> apiListToIntegrationList(List<Product> api);
}
