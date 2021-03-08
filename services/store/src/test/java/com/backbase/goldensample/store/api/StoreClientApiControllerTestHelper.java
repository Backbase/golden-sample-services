package com.backbase.goldensample.store.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

import com.backbase.goldensample.store.api.service.v1.model.ProductAggregate;
import com.backbase.goldensample.store.domain.Product;
import com.backbase.goldensample.store.domain.Review;
import com.backbase.goldensample.store.mapper.StoreMapper;
import com.backbase.goldensample.store.service.ProductCompositeService;
import java.time.LocalDate;
import java.util.List;
import javax.annotation.PostConstruct;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Provides mocked productCompositeService, mockMvc and productOne.
 */
@AutoConfigureMockMvc
public class StoreClientApiControllerTestHelper {

    @MockBean
    protected ProductCompositeService productCompositeService;

    @Autowired
    protected MockMvc mockMvc;

    protected Product productOne;

    @MockBean
    private StoreMapper storeMapper;

    @PostConstruct
    public void setup() {
        Review reviewOne = createReview(1L, 1L, "author", "subject", "long content");
        Review reviewTwo = createReview(2L, 1L, "another author", "another subject", "super long content");
        productOne = createProduct(1L, "Product 1", 23, LocalDate.of(2011, 10, 16));
        productOne.setReviews(List.of(reviewOne, reviewTwo));

        StoreMapper realMapper = Mappers.getMapper(StoreMapper.class);
        doAnswer(a -> realMapper.map(a.getArgument(0, Product.class)))
            .when(storeMapper).map(any(Product.class));
        doAnswer(a -> realMapper.map(a.getArgument(0, ProductAggregate.class)))
            .when(storeMapper).map(any(ProductAggregate.class));
    }

    private Product createProduct(Long id, String name, Integer weight, LocalDate createDate) {
        Product product = new Product();
        product.setProductId(id);
        product.setName(name);
        product.setWeight(weight);
        product.setCreateDate(createDate);
        return product;
    }

    private Review createReview(Long reviewId, Long productId, String author, String subject, String content) {
        Review review = new Review();
        review.setReviewId(reviewId);
        review.setProductId(productId);
        review.setAuthor(author);
        review.setSubject(subject);
        review.setContent(content);
        return review;
    }



}
