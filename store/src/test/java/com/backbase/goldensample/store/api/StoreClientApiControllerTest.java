package com.backbase.goldensample.store.api;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.backbase.goldensample.product.api.client.v2.model.Product;
import com.backbase.goldensample.review.api.client.v2.model.Review;
import com.backbase.goldensample.store.service.ProductCompositeService;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(StoreClientApiController.class)
class StoreClientApiControllerTest {

    @MockBean
    private ProductCompositeService productCompositeService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should get empty array when no Reviews")
    void shouldGetEmptyArrayWhenNoReviews() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/client-api/v1/product-composite/{productId}", 1L)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andReturn();

    }

    @Test
    void shouldGetReviewsWhenServiceReturnsReviewsOfAProduct() throws Exception {
        Review reviewOne = createReview(1L, 1L, "author", "subject", "long content");
        Review reviewTwo = createReview(2L, 1L, "another author", "another subject", "super long content");
        Product productOne = createProduct(1L, "Product 1", 23, new Date());

        when(productCompositeService.getProduct(1L)).thenReturn((productOne));
        when(productCompositeService.getReviews(1L)).thenReturn((List.of(reviewOne, reviewTwo)));

        this.mockMvc
            .perform(get("/client-api/v1/product-composite/{productId}/", 1L)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
            .andExpect(status().is(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.productId", is(1)))
            .andExpect(jsonPath("$.reviews[0].reviewId", is(1)))
            .andExpect(jsonPath("$.reviews[0].author", is("author")))
            .andExpect(jsonPath("$.reviews[0].subject", is("subject")))
            .andExpect(jsonPath("$.reviews[0].content", is("long content")))
            .andExpect(jsonPath("$.weight", is(23)))
//            .andExpect(jsonPath("$.createDate", is(new Date())))
            .andExpect(jsonPath("$.name", is("Product 1")));

        verify(productCompositeService).getProduct(1L);
        verify(productCompositeService).getReviews(1L);
    }



    private Product createProduct(Long id, String name, Integer weight, Date createDate) {
        Product result = new Product().productId(id).name(name).weight(weight).createDate(createDate);
        return result;
    }

    private Review createReview(Long reviewId, Long productId, String author, String subject, String content) {
        Review result = new Review().reviewId(reviewId).productId(productId).author(author).subject(subject).content(content);
        return result;
    }
}
