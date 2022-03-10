package com.backbase.goldensample.store.integration;

import com.backbase.goldensample.product.api.client.v1.ProductServiceApi;
import com.backbase.goldensample.product.api.client.v1.model.Product;
import com.backbase.goldensample.productenricher.webhook.api.client.v2.StoreIntegrationWebhookApi;
import com.backbase.goldensample.productenricher.webhook.api.client.v2.model.ProductAggregate;
import com.backbase.goldensample.review.api.client.v2.ReviewServiceApi;
import com.backbase.goldensample.review.api.client.v2.model.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"it", "it-with-webhook"})
public class GetProductAggregationWithWebhookExtensionIT {

    public static final long PRODUCT_ID = 1L;

    @MockBean
    private ProductServiceApi productServiceApi;

    @MockBean
    private ReviewServiceApi reviewServiceApi;

    @MockBean
    private StoreIntegrationWebhookApi storeIntegrationWebhookApi;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getProduct() throws Exception {
        ProductServiceApiMockHelper.initMock(productServiceApi);
        when(productServiceApi.getProductById(PRODUCT_ID))
                .thenReturn(new Product()
                        .productId(PRODUCT_ID)
                        .name("product")
                        .weight(42)
                        .createDate(LocalDate.now()));

        ReviewServiceApiMockHelper.initMock(reviewServiceApi);
        when(reviewServiceApi.getReviewListByProductId(PRODUCT_ID))
                .thenReturn(List.of(new Review()
                        .productId(PRODUCT_ID)
                        .author("Robin Green")
                        .subject("Subject")
                        .content("fart knocker")
                        .stars(3)));

        when(storeIntegrationWebhookApi.enrichProductWithHttpInfo(any()))
                .thenAnswer(a -> {
                    ProductAggregate productAggregate = a.getArgument(0, ProductAggregate.class);
                    productAggregate.name("PRODUCT");
                    return ResponseEntity.ok(productAggregate);
                });

        mockMvc.perform(get("/client-api/v1/product-composite/" + PRODUCT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("PRODUCT"))
                .andExpect(jsonPath("$.reviews").isArray())
                .andExpect(jsonPath("$.reviews[0].author").value("Robin Green"))
                .andExpect(jsonPath("$.reviews[0].content").value("fart knocker"));
    }

}
