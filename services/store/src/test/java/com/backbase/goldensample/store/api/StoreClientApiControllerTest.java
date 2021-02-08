package com.backbase.goldensample.store.api;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backbase.goldensample.product.api.client.v2.model.Product;
import com.backbase.goldensample.product.api.client.v2.model.ProductId;
import com.backbase.goldensample.review.api.client.v2.model.Review;
import com.backbase.goldensample.review.api.client.v2.model.ReviewId;
import com.backbase.goldensample.store.Application;
import com.backbase.goldensample.store.service.ProductCompositeService;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.TimeZone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
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
    @DisplayName("Should get Reviews when the Service returns Reviews of a Product")
    void shouldGetReviewsWhenServiceReturnsReviewsOfAProduct() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        LocalDate createDate = LocalDate.of(2011, 10, 16);

        Review reviewOne = createReview(1L, 1L, "author", "subject", "long content");
        Review reviewTwo = createReview(2L, 1L, "another author", "another subject", "super long content");
        Product productOne = createProduct(1L, "Product 1", 23, createDate);

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
            //TODO revisit this test
//            .andExpect(jsonPath("$.createDate", is(dateFormat.format(dateInUTC))))
            .andExpect(content().json("{'createDate': '2011-10-16'}"))
            .andExpect(jsonPath("$.name", is("Product 1")));

        verify(productCompositeService).getProduct(1L);
        verify(productCompositeService).getReviews(1L);
    }

    @Test
    @DisplayName("Should Create a Product and its Reviews")
    void shouldCreateAProductAndItsReviews() throws Exception {
        ProductId productOne = new ProductId().id(1L);

        String requestBody = "{\n" +
            "  \"name\": \"Product 1\",\n" +
            "  \"weight\": \"23\",\n" +
            "  \"createDate\": \"2020-12-01\",\n" +
            "  \"reviews\": [\n" +
            "{\n" +
            "  \"author\": \"author\",\n" +
            "  \"subject\": \"subject\",\n" +
            "  \"content\": \"long content\"\n" +
            "}\n" +
            "]\n" +
            "}";

        when(productCompositeService.createProduct(any(Product.class))).thenReturn(productOne);
        when(productCompositeService.createReview(any(Review.class))).thenReturn(new ReviewId().id(1L));

        this
            .mockMvc
            .perform(post("/client-api/v1/product-composite")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should Fail with BadRequest due to unexpected additions")
    void fail400UnexpectedAdditions() throws Exception {

        ProductId productOne = new ProductId().id(1L);

        String requestBody = "{\n" +
            "  \"name\": \"Product 1\",\n" +
            "  \"weight\": \"23\",\n" +
            "  \"createDate\": \"2020-12-01\",\n" +
            "  \"reviews\": [\n" +
            "{\n" +
            "  \"author\": \"author\",\n" +
            "  \"subject\": \"subject\",\n" +
            "  \"content\": \"long content\",\n" +
            "  \"additions\": {\n" +
                "  \"param-rev1\": \"valr1\"\n" +
                "}\n" +
            "}\n" +
            "],\n" +
            "  \"additions\": {\n" +
                "  \"param-prod1\": \"valp1\"\n" +
                "}\n" +
            "}";

        when(productCompositeService.createProduct(any(Product.class))).thenReturn(productOne);
        when(productCompositeService.createReview(any(Review.class))).thenReturn(new ReviewId().id(1L));

        MvcResult result = this.mockMvc
            .perform(post("/client-api/v1/product-composite")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content, containsString("{\"message\":\"Bad Request\""));
        assertThat(content, containsString(
            "{\"message\":\"The key is unexpected\",\"key\":\"api.AdditionalProperties.additions[param-prod1]\",\"context\":{\"rejectedValue\":\"valp1\"}}"));
        assertThat(content, containsString(
            "{\"message\":\"The key is unexpected\",\"key\":\"api.AdditionalProperties.reviews[0].additions[param-rev1]\",\"context\":{\"rejectedValue\":\"valr1\"}}"));
    }


    private Product createProduct(Long id, String name, Integer weight, LocalDate createDate) {
        Product result = new Product().productId(id).name(name).weight(weight).createDate(createDate);
        return result;
    }

    private Review createReview(Long reviewId, Long productId, String author, String subject, String content) {
        Review result =
            new Review().reviewId(reviewId).productId(productId).author(author).subject(subject).content(content);
        return result;
    }

}
