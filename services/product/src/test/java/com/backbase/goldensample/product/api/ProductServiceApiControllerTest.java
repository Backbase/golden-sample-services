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

import com.backbase.goldensample.product.config.WebSecurityConfiguration;
import com.backbase.goldensample.product.persistence.ProductEntity;
import com.backbase.product.api.service.v1.model.Product;
import com.backbase.product.api.service.v1.model.ProductId;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(ProductServiceApiController.class)
@Import(WebSecurityConfiguration.class)
class ProductServiceApiControllerTest extends ProductApiController {

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
    @DisplayName("should return a list of products")
    void shouldGetProductsWhenServiceReturnsProducts() throws Exception {
        ProductEntity productEntityTwo= createProductEntity("Product 2", 32, LocalDate.now(), Collections
            .singletonMap("popularity","29%"));
        Product productTwo = createProduct(2L, "Product 2", 32, TODAY);

        when(productMapper.entityListToApiList(any(List.class))).thenReturn((List.of(productOne, productTwo)));
        when(productService.getAllProducts()).thenReturn(List.of(productEntityOne, productEntityTwo));

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
    @DisplayName("should get a Product when service receives a valid id")
    void shouldGetProductWhenServiceReturnProduct() throws Exception {
        when(productMapper.entityToApi(any(ProductEntity.class))).thenReturn(productOne);
        when(productService.getProduct(1)).thenReturn(productEntityOne);

        this.mockMvc
            .perform(get("/service-api/v1/products/{productId}", 1L)
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
        String requestBody = """
            {
            "name": "Product 1",
            "weight": "23",
            "createDate": "2020-12-01"
            }""";

        when(productMapper.apiToEntity(any(Product.class))).thenReturn(productEntityOne);
        when(productService.createProduct(any())).thenReturn(new ProductId().id(1L));

        this
            .mockMvc
            .perform(post("/service-api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should update a Product with a valid a payload")
    void shouldUpdateAProductWithValidPayload() throws Exception {

        String requestBody = """
            {
            "productId": "1",
            "name": "Product 1",
            "weight": "5",
            "createDate": "2020-12-01"
            }""";

        when(productMapper.apiToEntity(any(Product.class))).thenReturn(productEntityOne);

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

}
