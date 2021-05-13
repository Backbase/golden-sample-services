package com.backbase.goldensample.product.api;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backbase.goldensample.product.persistence.ProductEntity;
import com.backbase.product.api.service.v1.model.Product;
import com.backbase.product.api.service.v1.model.ProductId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@WebMvcTest(ProductIntegrationApiController.class)
class ProductIntegrationApiControllerTest extends ProductApiController {

    @Test
    @DisplayName("should get a Product when service receives a valid id")
    void shouldGetProductWhenServiceReturnProduct() throws Exception {
        when(productMapper.entityToApi(any(ProductEntity.class))).thenReturn(productOne);
        when(productService.getProduct(1)).thenReturn(productEntityOne);

        this.mockMvc
            .perform(get("/integration-api/v1/products/{productId}", 1L)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
            .andExpect(status().is(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.productId", is(1)))
            .andExpect(jsonPath("$.name", is("Product 1")))
            .andExpect(jsonPath("$.weight", is(23)));

        verify(productService).getProduct(1L);
    }

    @Test
    @DisplayName("should create a new Product with a valid a payload")
    void shouldCreateNewProductWithValidPayload() throws Exception {
        String requestBody = "{\n" +
            "  \"name\": \"Product 1\",\n" +
            "  \"weight\": \"23\",\n" +
            "  \"createDate\": \"2020-12-01\"\n" +
            "}";

        when(productMapper.apiToEntity(any(Product.class))).thenReturn(productEntityOne);
        when(productService.createProduct(any())).thenReturn(new ProductId().id(1L));

        this
            .mockMvc
            .perform(post("/integration-api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should update a Product with a valid a payload")
    void shouldUpdateAProductWithValidPayload() throws Exception {

        String requestBody = "{\n" +
            "  \"productId\": \"1\",\n" +
            "  \"name\": \"Product 1\",\n" +
            "  \"weight\": \"5\",\n" +
            "  \"createDate\": \"2020-12-01\"\n" +
            "}";

        when(productMapper.apiToEntity(any(Product.class))).thenReturn(productEntityOne);

        this
            .mockMvc
            .perform(put("/integration-api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isNoContent());

    }

    @Test
    void shouldAllowDeletingProduct() throws Exception {
        this.mockMvc
            .perform(delete("/integration-api/v1/products/{productId}", 1L)
            )
            .andExpect(status().isNoContent());

        verify(productService).deleteProduct(1L);
    }

}
