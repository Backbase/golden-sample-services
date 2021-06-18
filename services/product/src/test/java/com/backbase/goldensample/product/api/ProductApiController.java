package com.backbase.goldensample.product.api;

import com.backbase.goldensample.product.mapper.ProductMapper;
import com.backbase.goldensample.product.persistence.ProductEntity;
import com.backbase.goldensample.product.service.ProductService;
import com.backbase.product.api.service.v1.model.Product;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

public class ProductApiController {

    protected static final LocalDate TODAY = LocalDate.of(2020, 1, 28);
    protected final Product productOne = createProduct(1L, "Product 1", 23, TODAY);
    protected final ProductEntity productEntityOne = createProductEntity("Product 1", 23, TODAY, Collections
        .singletonMap("popularity","29%"));
    @MockBean
    protected ProductService productService;
    @Autowired
    protected MockMvc mockMvc;
    @MockBean
    protected ProductMapper productMapper;

    Product createProduct(Long id, String name, Integer weight, LocalDate createDate) {
        return new Product().productId(id).name(name).weight(weight).createDate(createDate);
    }

    ProductEntity createProductEntity(String name, Integer weight, LocalDate createDate, Map<String, String> additions) {
        return new ProductEntity(name, weight, createDate, additions);
    }
}
