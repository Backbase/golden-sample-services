package com.backbase.goldensample.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import com.backbase.goldensample.product.mapper.ProductMapper;
import com.backbase.goldensample.product.persistence.ProductEntity;
import com.backbase.product.api.service.v2.model.Product;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import org.junit.jupiter.api.Test;

class MapperTests {

  private final ProductMapper mapper = ProductMapper.INSTANCE;

  private static final Instant TODAY = LocalDateTime.of(2020, 1, 28, 12, 26)
      .toInstant(ZoneOffset.UTC);

  @Test
  public void mapperTests() {

    assertNotNull(mapper);

    Product api = new Product();
    api.setProductId(1L);
    api.setName("n");
    api.setWeight(1);
    api.setCreateDate(Date.from(TODAY));

    ProductEntity entity = mapper.apiToEntity(api);

    assertEquals(api.getProductId(), entity.getId());
    assertEquals(api.getName(), entity.getName());
    assertEquals(api.getWeight(), entity.getWeight());
    assertEquals(api.getCreateDate(), Date.from(entity.getCreateDate()));

    Product api2 = mapper.entityToApi(entity);

    assertEquals(api.getProductId(), api2.getProductId());
    assertEquals(api.getName(), api2.getName());
    assertEquals(api.getWeight(), api2.getWeight());
    assertEquals(api.getCreateDate(), api2.getCreateDate());
  }
}
