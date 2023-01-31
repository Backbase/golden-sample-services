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
import com.backbase.goldensample.product.persistence.ProductEntity;
import com.backbase.product.api.service.v1.model.Product;
import com.backbase.product.api.service.v1.model.ProductId;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.hamcrest.Matchers;
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
    @DisplayName("should return a list of products")
    void shouldGetProductsWhenServiceReturnsProducts() throws Exception {
        ProductEntity productEntityTwo= createProductEntity("Product 2", 32, LocalDate.now(), Collections
            .singletonMap("popularity","29%"));
        Product productTwo = createProduct(2L, "Product 2", 32, TODAY);

        when(productMapper.entityListToApiList(any(List.class))).thenReturn((List.of(productOne, productTwo)));
        when(productService.getAllProducts()).thenReturn(List.of(productEntityOne, productEntityTwo));

        this.mockMvc
            .perform(get("/service-api/v1/products")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " +
                    TestTokenUtil.encode(TestTokenUtil.serviceClaimSet()))
                .header("X-TID", "org_shop"))
            .andExpect(status().is(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size()", is(2)))
            .andExpect(jsonPath("$[0].name", is("Product 1")))
            .andExpect(jsonPath("$[0].weight", is(23)))
            .andExpect(jsonPath("$[1].name", is("Product 2")))
            .andExpect(jsonPath("$[1].weight", is(32)));
    }


    @Test
    void shouldFailWithUnrecognisedAdditionsForTenantOrgShop() throws Exception {
        String requestBody = """
            {
              "name": "Product 1",
              "weight": "23",
              "createDate": "2020-12-01",
              "additions": {
              "description": "long desc"}
            }
            """;

        when(productService.createProduct(any())).thenReturn(new ProductId().id(1L));

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
        assertThat(content, containsString("""
            {"message":"Bad Request\""""));
        assertThat(content, containsString(
            """
                     {"message":"The key is unexpected","key":"api.AdditionalProperties.additions[description]","context":{"rejectedValue":"long desc"}}"""));
    }

    @Test
    @DisplayName("should create a new Product with valid additions for tenant")
    void shouldCreateNewProductWithRecognisedAdditionsForTenantRebrandShop() throws Exception {
        String requestBody = """
            {
              "name": "Product 1",
              "weight": "23",
              "createDate": "2020-12-01",
              "additions": {
              "description": "long desc"}
            }
            """;

        when(productMapper.apiToEntity(any(Product.class))).thenReturn(productEntityOne);
        when(productService.createProduct(any())).thenReturn(new ProductId().id(1L));

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
