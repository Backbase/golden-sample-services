package com.backbase.goldensample.review.v2;

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
import com.backbase.goldensample.review.persistence.ReviewEntity;
import com.backbase.goldensample.review.service.ReviewService;
import com.backbase.goldensample.review.v2.mapper.ReviewV2Mapper;
import com.backbase.reviews.api.service.v2.model.Review;
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
public class ReviewServiceApiControllerMultitenancyTest {
    @MockBean
    private ReviewService reviewService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ReviewV2Mapper reviewMapper;

    private final Review reviewOne = createReview(1L, 1L, "author", "subject", "long content", 4, Collections.singletonMap("verified", "true"));
    private final ReviewEntity reviewEntity = createEntity();

    @Test
    void shouldGetEmptyArrayWhenNoReviews() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/service-api/v2/products/{productId}/reviews", 99L)
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
        Review reviewTwo = createReview(2L, 1L, "another author", "another subject", "super long content", 5, Collections.singletonMap("verified", "false"));

        when(reviewMapper.entityListToApiList(any(List.class))).thenReturn((List.of(reviewOne, reviewTwo)));

        this.mockMvc
            .perform(get("/service-api/v2/products/{productId}/reviews", 1L)
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
            .andExpect(jsonPath("$[0].stars", is(4)))
            .andExpect(jsonPath("$[0].additions.verified", is("true")))
            .andExpect(jsonPath("$[1].productId", is(1)))
            .andExpect(jsonPath("$[1].reviewId", is(2)))
            .andExpect(jsonPath("$[1].author", is("another author")))
            .andExpect(jsonPath("$[1].subject", is("another subject")))
            .andExpect(jsonPath("$[1].content", is("super long content")))
            .andExpect(jsonPath("$[1].stars", is(5)))
            .andExpect(jsonPath("$[1].additions.verified", is("false")));
    }

    @Test
    void shouldFailWithUnrecognisedAdditionsForTenantOrgShop() throws Exception {
        String requestBody = "{\n" +
            "  \"productId\": \"1\",\n" +
            "  \"author\": \"author\",\n" +
            "  \"subject\": \"subject\",\n" +
            "  \"content\": \"long content\",\n" +
            "  \"stars\": \"4\",\n" +
            "  \"additions\": {\n" +
            "    \"purchaseDate\": \"today\"}\n" +
            "}";

        when(reviewMapper.entityToApi(any(ReviewEntity.class))).thenReturn(reviewOne);
        when(reviewService.getReview(1)).thenReturn(reviewEntity);

        MvcResult result = this.mockMvc
            .perform(post("/service-api/v2/reviews")
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
            "  \"stars\": \"4\",\n" +
            "  \"additions\": {\n" +
            "    \"purchaseDate\": \"today\"}\n" +
            "}";

        when(reviewMapper.apiToEntity(any(Review.class))).thenReturn(reviewEntity);
        when(reviewService.createReview(any(ReviewEntity.class))).thenReturn(reviewEntity);

        this.mockMvc
            .perform(post("/service-api/v2/reviews")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " +
                    TestTokenUtil.encode(TestTokenUtil.serviceClaimSet()))
                .header("X-TID", "rebrand_shop")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print())
            .andExpect(status().isOk());
    }

    private Review createReview(Long reviewId, Long productId, String author, String subject, String content, Integer stars, Map<String, String> additions) {
        Review result = new Review().reviewId(reviewId).productId(productId).author(author).subject(subject).content(content).stars(stars).additions(additions);
        return result;
    }

    private ReviewEntity createEntity(){
        ReviewEntity reviewIdentity = new ReviewEntity();
        reviewIdentity.setId(1L);
        return reviewIdentity;
    }
}
