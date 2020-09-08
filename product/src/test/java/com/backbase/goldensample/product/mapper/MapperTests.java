package com.backbase.goldensample.product.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import com.backbase.goldensample.product.persistence.ProductEntity;
import com.backbase.product.api.service.v2.model.Product;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class MapperTests {

    private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    private static final Instant TODAY = LocalDateTime.of(2020, 1, 28, 12, 26)
        .toInstant(ZoneOffset.UTC);

    @Test
    void mapperTests() {

        assertNotNull(mapper);

        Product api = new Product().productId(1L).name("Product").weight(20).createDate(Date.from(TODAY));

        ProductEntity entity = mapper.apiToEntity(api);

        assertAll(() -> assertEquals(api.getProductId(), entity.getId()),
            () -> assertEquals(api.getName(), entity.getName()),
            () -> assertEquals(api.getWeight(), entity.getWeight()),
            () -> assertEquals(api.getCreateDate(), Date.from(entity.getCreateDate())));

        Product api2 = mapper.entityToApi(entity);

        assertAll(() -> assertEquals(api.getProductId(), api2.getProductId()),
            () -> assertEquals(api.getName(), api2.getName()),
            () -> assertEquals(api.getWeight(), api2.getWeight()),
            () -> assertEquals(api.getCreateDate(), api2.getCreateDate()));
    }
}
