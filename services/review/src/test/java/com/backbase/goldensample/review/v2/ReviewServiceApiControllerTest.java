package com.backbase.goldensample.review.v2;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backbase.goldensample.review.persistence.ReviewEntity;
import com.backbase.goldensample.review.service.ReviewService;
import com.backbase.goldensample.review.v2.api.ReviewServiceApiController;
import com.backbase.goldensample.review.v2.mapper.ReviewMapperV2;
import com.backbase.reviews.api.service.v2.model.Review;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(ReviewServiceApiController.class)
class ReviewServiceApiControllerTest {

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ReviewMapperV2 reviewMapper;

    private final Review reviewOne = createReview(1L, 1L, "author", "subject", "long content", 4);
    private final ReviewEntity reviewEntity = createEntity();

    @Test
    void shouldGetEmptyArrayWhenNoReviews() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/service-api/v2/products/{productId}/reviews", 99L)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size()", Matchers.is(0)))
            .andDo(print())
            .andReturn();

    }

    @Test
    void shouldGetReviewsWhenServiceReturnsReviewsOfAProduct() throws Exception {
        Review reviewTwo = createReview(2L, 1L, "another author", "another subject", "super long content", 5);

        when(reviewMapper.entityListToApiList(any(List.class))).thenReturn((List.of(reviewOne, reviewTwo)));

        this.mockMvc
            .perform(get("/service-api/v2/products/{productId}/reviews", 1L)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
            .andExpect(status().is(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size()", is(2)))
            .andExpect(jsonPath("$[0].productId", is(1)))
            .andExpect(jsonPath("$[0].reviewId", is(1)))
            .andExpect(jsonPath("$[0].author", is("author")))
            .andExpect(jsonPath("$[0].subject", is("subject")))
            .andExpect(jsonPath("$[0].content", is("long content")))
            .andExpect(jsonPath("$[0].stars", is(4)))
            .andExpect(jsonPath("$[1].productId", is(1)))
            .andExpect(jsonPath("$[1].reviewId", is(2)))
            .andExpect(jsonPath("$[1].author", is("another author")))
            .andExpect(jsonPath("$[1].subject", is("another subject")))
            .andExpect(jsonPath("$[1].stars", is(5)))
            .andExpect(jsonPath("$[1].content", is("super long content")));
    }

    @Test
    void shouldGetReviewWhenServiceReturnReviewById() throws Exception {
        when(reviewMapper.entityToApi(any(ReviewEntity.class))).thenReturn(reviewOne);
        when(reviewService.getReview(1)).thenReturn(reviewEntity);

        this.mockMvc
            .perform(get("/service-api/v2/reviews/{reviewId}", 1L)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
            .andExpect(status().is(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.productId", is(1)))
            .andExpect(jsonPath("$.reviewId", is(1)))
            .andExpect(jsonPath("$.author", is("author")))
            .andExpect(jsonPath("$.subject", is("subject")))
            .andExpect(jsonPath("$.content", is("long content")))
            .andExpect(jsonPath("$.stars", is(4)));

        verify(reviewService).getReview(1L);
    }

    @Test
    void shouldRejectNewReviewWithInvalidPayload() throws Exception {

        String requestBody = "{\n" +
            "  \"productId\": \"1\",\n" +
            "  \"author\": \"name\",\n" +
            "  \"subject\": \"subject\",\n" +
            "  \"content\": \"long content\"\n" +
            "}";

        this
            .mockMvc
            .perform(post("/service-api/v2/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateNewReviewWithValidPayload() throws Exception {
        String requestBody = "{\n" +
            "  \"productId\": \"1\",\n" +
            "  \"author\": \"author\",\n" +
            "  \"subject\": \"subject\",\n" +
            "  \"content\": \"long content\",\n" +
            "  \"stars\": \"4\"\n" +
            "}";

        when(reviewMapper.apiToEntity(any(Review.class))).thenReturn(reviewEntity);
        when(reviewService.createReview(any(ReviewEntity.class))).thenReturn(reviewEntity);

        this
            .mockMvc
            .perform(post("/service-api/v2/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateReviewWithValidPayload() throws Exception {

        String requestBody = "{\n" +
            "  \"productId\": \"1\",\n" +
            "  \"author\": \"author\",\n" +
            "  \"subject\": \"subject\",\n" +
            "  \"content\": \"long content\",\n" +
            "  \"stars\": \"4\"\n" +
            "}";

        when(reviewMapper.apiToEntity(any(Review.class))).thenReturn(reviewEntity);
        when(reviewService.updateReview(any(ReviewEntity.class))).thenReturn(reviewEntity);

        this
            .mockMvc
            .perform(put("/service-api/v2/reviews/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isNoContent());

    }

    @Test
    void shouldAllowDeletingReviewsByProductId() throws Exception {
        this.mockMvc
            .perform(delete("/service-api/v2/products/{productId}/reviews", 1L)
            )
            .andExpect(status().isNoContent());

        verify(reviewService).deleteReviews(1L);
    }

    @Test
    void shouldAllowDeletingOneReviewById() throws Exception {
        this.mockMvc
            .perform(delete("/service-api/v2/reviews/{reviewId}", 1L)
            )
            .andExpect(status().isNoContent());

        verify(reviewService).deleteReview(1L);
    }


    private Review createReview(Long reviewId, Long productId, String author, String subject, String content, Integer stars) {
        Review result = new Review().reviewId(reviewId).productId(productId).author(author).subject(subject).content(content).stars(stars);
        return result;
    }

    private ReviewEntity createEntity(){
        ReviewEntity reviewIdentity = new ReviewEntity();
        reviewIdentity.setId(1L);
        return reviewIdentity;
    }

}
