package com.backbase.goldensample.product.api;

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

import com.backbase.goldensample.product.service.ProductService;
import com.backbase.product.api.service.v2.model.Product;
import java.time.LocalDate;
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

@WebMvcTest(ProductServiceApiController.class)
class ProductServiceApiControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetEmptyArrayWhenNoProducts() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/service-api/v1/products")
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size()", Matchers.is(0)))
            .andDo(print())
            .andReturn();

    }

    @Test
    void shouldGetProductsWhenServiceReturnsProducts() throws Exception {
        Product productTwo= createProduct(2L, "Product 2", 32, LocalDate.now());
        Product productOne = createProduct(1L, "Product 1", 23, LocalDate.now());

        when(productService.getAllProducts()).thenReturn(List.of(productOne, productTwo));

        this.mockMvc
            .perform(get("/service-api/v1/products")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
            .andExpect(status().is(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size()", is(2)))
            .andExpect(jsonPath("$[0].productId", is(1)))
            .andExpect(jsonPath("$[0].name", is("Product 1")))
            .andExpect(jsonPath("$[0].weight", is(23)))
            .andExpect(jsonPath("$[1].productId", is(2)))
            .andExpect(jsonPath("$[1].name", is("Product 2")))
            .andExpect(jsonPath("$[1].weight", is(32)));
    }

    @Test
    void shouldGetProductWhenServiceReturnProduct() throws Exception {
        Product productOne = createProduct(1L, "Product 1", 23, LocalDate.now());

        when(productService.getProduct(1,0,0)).thenReturn(productOne);

        this.mockMvc
            .perform(get("/service-api/v1/products/{productId}", 1L)
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
            .perform(post("/service-api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateAProductWithValidPayloadAndId() throws Exception {

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
            .perform(put("/service-api/v2/products/{productId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isNoContent());

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
            .perform(put("/service-api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isNoContent());

    }

    @Test
    void shouldAllowDeletingProduct() throws Exception {
        this.mockMvc
            .perform(delete("/service-api/v1/products/{productId}", 1L)
            )
            .andExpect(status().isNoContent());

        verify(productService).deleteProduct(1L);
    }


    private Product createProduct(Long id, String name, Integer weight, LocalDate createDate) {
        Product result = new Product().productId(id).name(name).weight(weight).createDate(createDate);
        return result;
    }

}
