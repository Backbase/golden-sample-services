package com.backbase.goldensample.product;

import static org.junit.Assert.assertEquals;

import com.backbase.goldensample.product.mapper.ProductMapper;
import com.backbase.goldensample.product.persistence.ProductEntity;
import com.backbase.goldensample.product.persistence.ProductRepository;
import com.backbase.goldensample.product.service.ProductService;
import com.blazebit.persistence.PagedList;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringBlazePersistenceTest {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public static final int PRODUCT_COUNT = 50;

    public static final int PAGE_SIZE = 25;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    @BeforeAll
    public static void envSetup() {
        System.setProperty("SIG_SECRET_KEY", "JWTSecretKeyDontUseInProduction!");
    }

    @BeforeEach
    public void init() {

        repository.deleteAll();

        final LocalDate TODAY = LocalDate.of(2020, 1, 28);

        for (int i = 0; i < PRODUCT_COUNT; i++) {
            ProductEntity productEntity = new ProductEntity("product"+i, 1, TODAY,
                Collections.singletonMap("popularity", "87%"));
                repository.save(productEntity);
        }

    }

    @Test
    public void test() {
        PagedList<ProductEntity> topPage = productService.firstLatestPosts(PAGE_SIZE);

        assertEquals(PRODUCT_COUNT, topPage.getTotalSize());
        assertEquals(PRODUCT_COUNT / PAGE_SIZE, topPage.getTotalPages());
        assertEquals(1, topPage.getPage());
        List<Long> topIds = topPage.stream()
            .map(ProductEntity::getId)
            .collect(Collectors.toList());
        assertEquals(Long.valueOf(52), topIds.get(0));
        assertEquals(Long.valueOf(51), topIds.get(1));

        LOGGER.info("Top ids: {}", topIds);

        PagedList<ProductEntity> nextPage = productService.findNextLatestPosts(topPage);

        assertEquals(2, nextPage.getPage());

        List<Long> nextIds = nextPage.stream()
            .map(ProductEntity::getId)
            .collect(Collectors.toList());
        assertEquals(Long.valueOf(27), nextIds.get(0));
        assertEquals(Long.valueOf(26), nextIds.get(1));

        LOGGER.info("Next ids: {}", nextIds);
    }
}

