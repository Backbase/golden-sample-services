package com.backbase.goldensample.store.api;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backbase.buildingblocks.testutils.TestTokenUtil;
import com.backbase.goldensample.store.Application;
import com.backbase.goldensample.store.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
@ActiveProfiles({"it","m10y"})
class StoreClientApiControllerMultitenancyTest extends StoreClientApiControllerTestHelper {

    @Test
    @DisplayName("Should Create a Product and its Reviews with tenant specific additions and receive different header")
    void shouldCreateAProductAndItsReviewsWithRebrandShopSpecificAdditions() throws Exception {

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
              "purchaseDate": "date1"
            }
            }
            ],
              "additions": {
              "description": "desc1"
            }
            }""";

        MvcResult result = this.mockMvc
            .perform(post("/client-api/v1/product-composite")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " +
                    TestTokenUtil.encode(
                        new TestTokenUtil.TestTokenBuilder()
                            .user()
                            .withClaim("tid", "rebrand_shop")
                            .build()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string("x-store-theme", "rebranded"))
            .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content, containsString("\"additions\":"));
    }
}
