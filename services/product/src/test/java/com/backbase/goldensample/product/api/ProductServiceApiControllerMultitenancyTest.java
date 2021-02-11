package com.backbase.goldensample.product.api;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backbase.buildingblocks.testutils.TestTokenUtil;
import com.backbase.goldensample.product.Application;
import com.backbase.product.api.service.v2.model.Product;
import java.time.LocalDate;
import java.util.List;
import org.hamcrest.Matchers;
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
@ActiveProfiles({"it","m10y"})
class ProductServiceApiControllerMultitenancyTest extends ProductApiController {

    @Test
    void shouldGetEmptyArrayWhenNoProducts() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/service-api/v1/products")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " +
                    TestTokenUtil.encode(TestTokenUtil.serviceClaimSet()))
                .header("X-TID", "org_shop"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size()", Matchers.is(0)))
            .andDo(print())
            .andReturn();

    }

    @Test
    void shouldGetProductsWhenServiceReturnsProducts() throws Exception {
        Product productTwo= createProduct(2L, "Product 2", 32, LocalDate.now());

        when(productService.getAllProducts()).thenReturn(List.of(productOne, productTwo));

        this.mockMvc
            .perform(get("/service-api/v1/products")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " +
                    TestTokenUtil.encode(TestTokenUtil.serviceClaimSet()))
                .header("X-TID", "org_shop"))
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
    void shouldFailWithUnrecognisedAdditionsForTenant1() throws Exception {
        String requestBody = "{\n" +
            "  \"name\": \"Product 1\",\n" +
            "  \"weight\": \"23\",\n" +
            "  \"createDate\": \"2020-12-01\",\n" +
            "  \"additions\": {\n" +
            "    \"description\": \"long desc\"}\n" +
            "}";

        when(productService.createProduct(any(Product.class)))
            .thenReturn(productOne);

        MvcResult result = this.mockMvc
            .perform(post("/service-api/v1/products")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " +
                    TestTokenUtil.encode(TestTokenUtil.serviceClaimSet()))
                .header("X-TID", "org_shop")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest())
            .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content, containsString("{\"message\":\"Bad Request\""));
        assertThat(content, containsString(
            "{\"message\":\"The key is unexpected\",\"key\":\"api.AdditionalProperties.additions[description]\",\"context\":{\"rejectedValue\":\"long desc\"}}"));
    }

    @Test
    void shouldCreateNewProductWithRecognisedAdditionsForTenant2() throws Exception {
        String requestBody = "{\n" +
            "  \"name\": \"Product 1\",\n" +
            "  \"weight\": \"23\",\n" +
            "  \"createDate\": \"2020-12-01\",\n" +
            "  \"additions\": {\n" +
            "    \"description\": \"long desc\"}\n" +
            "}";

        when(productService.createProduct(any(Product.class)))
            .thenReturn(productOne);

        this
            .mockMvc
            .perform(post("/service-api/v1/products")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " +
                    TestTokenUtil.encode(TestTokenUtil.serviceClaimSet()))
                .header("X-TID", "rebrand_shop")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk());
    }

}
