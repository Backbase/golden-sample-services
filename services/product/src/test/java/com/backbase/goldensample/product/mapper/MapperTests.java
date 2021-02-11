package com.backbase.goldensample.product.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.backbase.goldensample.product.persistence.ProductEntity;
import com.backbase.product.api.service.v1.model.Product;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class MapperTests {

    private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    private static final LocalDate TODAY = LocalDate.of(2020, 1, 28);

    @Test
    void mapperTests() {

        assertNotNull(mapper);

        Product api = new Product().productId(1L).name("Product").weight(20).createDate(TODAY);

        ProductEntity entity = mapper.apiToEntity(api);

        assertAll(() -> assertEquals(api.getProductId(), entity.getId(), "Product ID is different"),
            () -> assertEquals(api.getName(), entity.getName(), "Name is different"),
            () -> assertEquals(api.getWeight(), entity.getWeight(), "Weight is different"),
            () -> assertEquals(api.getCreateDate(), entity.getCreateDate(), "Create Date is different"));

        Product api2 = mapper.entityToApi(entity);

        assertAll(() -> assertEquals(api.getProductId(), api2.getProductId(), "Product ID is different"),
            () -> assertEquals(api.getName(), api2.getName(), "Name is different"),
            () -> assertEquals(api.getWeight(), api2.getWeight(), "Weight is different"),
            () -> assertEquals(api.getCreateDate(), api2.getCreateDate(), "Create Date is different"));
    }
}
