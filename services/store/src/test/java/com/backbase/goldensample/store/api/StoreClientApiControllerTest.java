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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backbase.goldensample.store.Application;
import com.backbase.goldensample.store.domain.Product;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.TimeZone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
@ActiveProfiles({"it"})
class StoreClientApiControllerTest extends StoreClientApiControllerTestHelper {

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

        when(productCompositeService.retrieveProductWithReviews(1L)).thenReturn(Optional.of(productOne));

        this.mockMvc
            .perform(get("/client-api/v1/product-composite/{productId}", 1L)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
            .andExpect(status().is(200))
            .andExpect(header().string("x-store-theme", "original"))
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

        verify(productCompositeService).retrieveProductWithReviews(1L);
    }

    @Test
    @DisplayName("Should Create a Product and its Reviews")
    void shouldCreateAProductAndItsReviews() throws Exception {

        String requestBody = """
            {
              "name": "Product 1",
              "weight": "23",
              "createDate": "2020-12-01",
              "reviews": [
            {
              "author": "author",
              "subject": "subject",
              "content": "long content"
            }
            ]
            }""";

        when(productCompositeService.createProductWithReviews(any())).thenReturn(productOne);

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

        Product productOne = new Product();

        String requestBody = """
            {
              "name": "Product 1",
              "weight": "23",
              "createDate": "2020-12-01",
              "reviews": [
            {
              "author": "author",
              "subject": "subject",
              "content": "long content",
              "additions": {
              "param-rev1": "valr1"
            }
            }
            ],
              "additions": {
              "param-prod1": "valp1"
            }
            }""";

        when(productCompositeService.createProductWithReviews(any(Product.class))).thenReturn(productOne);

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

}
