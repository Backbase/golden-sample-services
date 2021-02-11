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
import com.backbase.goldensample.product.api.client.v2.model.Product;
import com.backbase.goldensample.product.api.client.v2.model.ProductId;
import com.backbase.goldensample.review.api.client.v2.model.Review;
import com.backbase.goldensample.review.api.client.v2.model.ReviewId;
import com.backbase.goldensample.store.Application;
import com.backbase.goldensample.store.service.ProductCompositeService;
import org.junit.jupiter.api.DisplayName;
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

@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
@ActiveProfiles({"it","m10y"})
class StoreClientApiControllerMultitenancyTest {

    @MockBean
    private ProductCompositeService productCompositeService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should Create a Product and its Reviews with tenant specific additions and receive different header")
    void shouldCreateAProductAndItsReviewsWithAdditions() throws Exception {

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
                "  \"purchaseDate\": \"date1\"\n" +
                "}\n" +
            "}\n" +
            "],\n" +
            "  \"additions\": {\n" +
                "  \"description\": \"desc1\"\n" +
                "}\n" +
            "}";

        when(productCompositeService.createProduct(any(Product.class))).thenReturn(productOne);
        when(productCompositeService.createReview(any(Review.class))).thenReturn(new ReviewId().id(1L));

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
