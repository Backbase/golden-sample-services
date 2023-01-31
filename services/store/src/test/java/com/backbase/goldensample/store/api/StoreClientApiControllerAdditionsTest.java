package com.backbase.goldensample.store.api;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backbase.goldensample.store.Application;
import com.backbase.goldensample.store.domain.Product;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(classes = {Application.class})
@ActiveProfiles({"it", "extensions"})
class StoreClientApiControllerAdditionsTest extends StoreClientApiControllerTestHelper {

    @Test
    @DisplayName("Should Create a Product and its Reviews with additions")
    void shouldCreateAProductAndItsReviewsWithAdditions() throws Exception {

        when(productCompositeService.createProductWithReviews(any(Product.class))).thenReturn(productOne);

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

        MvcResult result = this.mockMvc
            .perform(post("/client-api/v1/product-composite")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content, containsString("\"additions\":"));
    }
}
