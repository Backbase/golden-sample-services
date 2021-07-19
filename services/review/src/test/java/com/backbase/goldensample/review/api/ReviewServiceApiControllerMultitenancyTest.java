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
import com.backbase.goldensample.review.dto.ReviewDTO;
import com.backbase.goldensample.review.mapper.ReviewMapper;
import com.backbase.goldensample.review.service.ReviewService;
import com.backbase.reviews.api.service.v1.model.Review;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    @MockBean
    private ReviewMapper reviewMapper;

    @Autowired
    private MockMvc mockMvc;

    private final Review reviewOne = createReview(1L, 1L, "author", "subject", "long content", Collections.singletonMap("verified", "true"));
    private final ReviewDTO reviewEntity = createDto();

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
    void shouldGetReviewsWhenServiceReturnsReviewsOfAProductWithAdditions() throws Exception {
        Review reviewTwo = createReview(2L, 1L, "another author", "another subject", "super long content", Collections.singletonMap("verified", "false"));

        when(reviewMapper.dtoListToApiList(any(List.class))).thenReturn((List.of(reviewOne, reviewTwo)));

        this.mockMvc
            .perform(get("/service-api/v1/products/{productId}/reviews", 1L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " +
                    TestTokenUtil.encode(TestTokenUtil.serviceClaimSet()))
                .header("X-TID", "org_shop"))
            .andDo(print())
            .andExpect(status().is(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size()", is(2)))
            .andExpect(jsonPath("$[0].productId", is(1)))
            .andExpect(jsonPath("$[0].reviewId", is(1)))
            .andExpect(jsonPath("$[0].author", is("author")))
            .andExpect(jsonPath("$[0].subject", is("subject")))
            .andExpect(jsonPath("$[0].content", is("long content")))
            .andExpect(jsonPath("$[0].additions.verified", is("true")))
            .andExpect(jsonPath("$[1].productId", is(1)))
            .andExpect(jsonPath("$[1].reviewId", is(2)))
            .andExpect(jsonPath("$[1].author", is("another author")))
            .andExpect(jsonPath("$[1].subject", is("another subject")))
            .andExpect(jsonPath("$[1].content", is("super long content")))
            .andExpect(jsonPath("$[1].additions.verified", is("false")));
    }

    @Test
    void shouldFailWithUnrecognisedAdditionsForTenantOrgShop() throws Exception {
        String requestBody = "{\n" +
            "  \"productId\": \"1\",\n" +
            "  \"author\": \"author\",\n" +
            "  \"subject\": \"subject\",\n" +
            "  \"content\": \"long content\",\n" +
            "  \"additions\": {\n" +
            "    \"purchaseDate\": \"today\"}\n" +
            "}";

        when(reviewMapper.dtoToApi(any(ReviewDTO.class))).thenReturn(reviewOne);
        when(reviewService.getReview(1)).thenReturn(reviewEntity);

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
    void shouldCreateNewProductWithRecognisedAdditionsForTenantRebrandShop() throws Exception {
        String requestBody = "{\n" +
            "  \"productId\": \"1\",\n" +
            "  \"author\": \"author\",\n" +
            "  \"subject\": \"subject\",\n" +
            "  \"content\": \"long content\",\n" +
            "  \"additions\": {\n" +
            "    \"purchaseDate\": \"today\"}\n" +
            "}";

        when(reviewMapper.apiToDto(any(Review.class))).thenReturn(reviewEntity);
        when(reviewService.createReview(any(ReviewDTO.class))).thenReturn(reviewEntity);

        this.mockMvc
            .perform(post("/service-api/v1/reviews")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " +
                    TestTokenUtil.encode(TestTokenUtil.serviceClaimSet()))
                .header("X-TID", "rebrand_shop")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print())
            .andExpect(status().isOk());
    }

    private Review createReview(Long reviewId, Long productId, String author, String subject, String content, Map<String, String> additions) {
        Review result = new Review().reviewId(reviewId).productId(productId).author(author).subject(subject).content(content).additions(additions);
        return result;
    }

    private ReviewDTO createDto(){
        ReviewDTO reviewIdentity = new ReviewDTO();
        reviewIdentity.setId(1L);
        return reviewIdentity;
    }

}
