package com.backbase.goldensample.review.api;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backbase.buildingblocks.testutils.TestTokenUtil;
import com.backbase.goldensample.review.Application;
import com.backbase.goldensample.review.service.ReviewService;
import com.backbase.reviews.api.service.v2.model.Review;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
@ActiveProfiles({"it","m10y"})
class ReviewServiceApiControllerMultitenancyTest {

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private MockMvc mockMvc;

    private final Review reviewOne = createReview(1L, 1L, "author", "subject", "long content");

    @Test
    void shouldGetEmptyArrayWhenNoReviews() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/service-api/v1/products/{productId}/reviews", 99L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " +
                    TestTokenUtil.encode(TestTokenUtil.serviceClaimSet()))
                .header("X-TID", "org_shop"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size()", Matchers.is(0)))
            .andDo(print())
            .andReturn();

    }

    @Test
    void shouldGetReviewsWhenServiceReturnsReviewsOfAProduct() throws Exception {
        Review reviewTwo = createReview(2L, 1L, "another author", "another subject", "super long content");

        when(reviewService.getReviewsByProductId(1L)).thenReturn(List.of(reviewOne, reviewTwo));

        this.mockMvc
            .perform(get("/service-api/v1/products/{productId}/reviews", 1L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " +
                    TestTokenUtil.encode(TestTokenUtil.serviceClaimSet()))
                .header("X-TID", "org_shop"))
            .andExpect(status().is(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size()", is(2)))
            .andExpect(jsonPath("$[0].productId", is(1)))
            .andExpect(jsonPath("$[0].reviewId", is(1)))
            .andExpect(jsonPath("$[0].author", is("author")))
            .andExpect(jsonPath("$[0].subject", is("subject")))
            .andExpect(jsonPath("$[0].content", is("long content")))
            .andExpect(jsonPath("$[1].productId", is(1)))
            .andExpect(jsonPath("$[1].reviewId", is(2)))
            .andExpect(jsonPath("$[1].author", is("another author")))
            .andExpect(jsonPath("$[1].subject", is("another subject")))
            .andExpect(jsonPath("$[1].content", is("super long content")));
    }


    @Test
    void shouldFailWithUnrecognisedAdditionsForTenant1() throws Exception {
        String requestBody = "{\n" +
            "  \"productId\": \"1\",\n" +
            "  \"author\": \"author\",\n" +
            "  \"subject\": \"subject\",\n" +
            "  \"content\": \"long content\",\n" +
            "  \"additions\": {\n" +
            "    \"purchaseDate\": \"today\"}\n" +
            "}";

        when(reviewService.createReview(any(Review.class)))
            .thenReturn(reviewOne);

        MvcResult result = this.mockMvc
            .perform(post("/service-api/v1/reviews")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " +
                    TestTokenUtil.encode(TestTokenUtil.serviceClaimSet()))
                .header("X-TID", "org_shop")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content, containsString("{\"message\":\"Bad Request\""));
        assertThat(content, containsString(
            "{\"message\":\"The key is unexpected\",\"key\":\"api.AdditionalProperties.additions[purchaseDate]\",\"context\":{\"rejectedValue\":\"today\"}}"));
    }

    @Test
    void shouldCreateNewProductWithRecognisedAdditionsForTenant2() throws Exception {
        String requestBody = "{\n" +
            "  \"productId\": \"1\",\n" +
            "  \"author\": \"author\",\n" +
            "  \"subject\": \"subject\",\n" +
            "  \"content\": \"long content\",\n" +
            "  \"additions\": {\n" +
            "    \"purchaseDate\": \"today\"}\n" +
            "}";

        when(reviewService.createReview(any(Review.class)))
            .thenReturn(reviewOne);

        this.mockMvc
            .perform(post("/service-api/v1/reviews")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " +
                    TestTokenUtil.encode(TestTokenUtil.serviceClaimSet()))
                .header("X-TID", "rebrand_shop")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk());
    }

    private Review createReview(Long reviewId, Long productId, String author, String subject, String content) {
        Review result = new Review().reviewId(reviewId).productId(productId).author(author).subject(subject).content(content);
        return result;
    }

}
