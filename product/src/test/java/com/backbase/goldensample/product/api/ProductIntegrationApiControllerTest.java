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

import com.backbase.goldensample.product.service.ProductService;
import com.backbase.product.api.service.v2.model.Product;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductIntegrationApiController.class)
class ProductIntegrationApiControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetProductWhenServiceReturnProduct() throws Exception {
        Product productOne = createProduct(1L, "Product 1", 23, LocalDate.now());

        when(productService.getProduct(1,0,0)).thenReturn(productOne);

        this.mockMvc
            .perform(get("/integration-api/v1/products/{productId}", 1L)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
            .andExpect(status().is(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.productId", is(1)))
            .andExpect(jsonPath("$.name", is("Product 1")))
            .andExpect(jsonPath("$.weight", is(23)));

        verify(productService).getProduct(1L, 0, 0);
    }

    @Test
    void shouldCreateNewProductWithValidPayload() throws Exception {
        Product productOne = createProduct(1L, "Product 1", 23, LocalDate.now());

        String requestBody = "{\n" +
            "  \"name\": \"Product 1\",\n" +
            "  \"weight\": \"23\",\n" +
            "  \"createDate\": \"2020-12-01\"\n" +
            "}";

        when(productService.createProduct(any(Product.class)))
            .thenReturn(productOne);

        this
            .mockMvc
            .perform(post("/integration-api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateAProductWithValidPayload() throws Exception {

        String requestBody = "{\n" +
            "  \"productId\": \"1\",\n" +
            "  \"name\": \"Product 1\",\n" +
            "  \"weight\": \"5\",\n" +
            "  \"createDate\": \"2020-12-01\"\n" +
            "}";

        when(productService.updateProduct(any(Product.class)))
            .thenReturn(any(Product.class));

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


    private Product createProduct(Long id, String name, Integer weight, LocalDate createDate) {
        Product result = new Product().productId(id).name(name).weight(weight).createDate(createDate);
        return result;
    }

}
