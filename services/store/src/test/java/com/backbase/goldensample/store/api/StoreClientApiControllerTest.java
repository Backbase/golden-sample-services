package com.backbase.goldensample.store.api;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backbase.goldensample.store.api.service.v1.model.ProductAggregate;
import com.backbase.goldensample.store.domain.Product;
import com.backbase.goldensample.store.domain.Review;
import com.backbase.goldensample.store.mapper.StoreMapper;
import com.backbase.goldensample.store.service.ProductCompositeService;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(StoreClientApiController.class)
class StoreClientApiControllerTest {

    @MockBean
    private ProductCompositeService productCompositeService;

    @MockBean
    private StoreMapper storeMapper;

    @Autowired
    private MockMvc mockMvc;

    private Product productOne;

    @PostConstruct
    public void setup() {
        Review reviewOne = createReview(1L, 1L, "author", "subject", "long content");
        Review reviewTwo = createReview(2L, 1L, "another author", "another subject", "super long content");
        productOne = createProduct(1L, "Product 1", 23, LocalDate.of(2011, 10, 16));
        productOne.setReviews(List.of(reviewOne, reviewTwo));

        StoreMapper realMapper = Mappers.getMapper(StoreMapper.class);
        doAnswer(a -> realMapper.map(a.getArgument(0, Product.class)))
            .when(storeMapper).map(any(Product.class));
        doAnswer(a -> realMapper.map(a.getArgument(0, ProductAggregate.class)))
            .when(storeMapper).map(any(ProductAggregate.class));
    }

    @Test
    @DisplayName("Should get empty array when no Reviews")
    void shouldGetEmptyArrayWhenNoReviews() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/client-api/v1/product-composite/{productId}", 1L)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andReturn();

    }

    @Test
    @DisplayName("Should get Reviews when the Service returns Reviews of a Product")
    void shouldGetReviewsWhenServiceReturnsReviewsOfAProduct() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        when(productCompositeService.retrieveProductWithReviews(1L)).thenReturn(Optional.of(productOne));

        this.mockMvc
            .perform(get("/client-api/v1/product-composite/{productId}/", 1L)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
            .andExpect(status().is(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.productId", is(1)))
            .andExpect(jsonPath("$.reviews[0].reviewId", is(1)))
            .andExpect(jsonPath("$.reviews[0].author", is("author")))
            .andExpect(jsonPath("$.reviews[0].subject", is("subject")))
            .andExpect(jsonPath("$.reviews[0].content", is("long content")))
            .andExpect(jsonPath("$.weight", is(23)))
            //TODO revisit this test
//            .andExpect(jsonPath("$.createDate", is(dateFormat.format(dateInUTC))))
            .andExpect(content().json("{'createDate': '2011-10-16'}"))
            .andExpect(jsonPath("$.name", is("Product 1")));

        verify(productCompositeService).retrieveProductWithReviews(1L);
    }

    @Test
    @DisplayName("Should Create a Product and its Reviews")
    void shouldCreateAProductAndItsReviews() throws Exception {

        String requestBody = "{\n" +
            "  \"name\": \"Product 1\",\n" +
            "  \"weight\": \"23\",\n" +
            "  \"createDate\": \"2020-12-01\",\n" +
            "  \"reviews\": [\n" +
            "{\n" +
            "  \"author\": \"author\",\n" +
            "  \"subject\": \"subject\",\n" +
            "  \"content\": \"long content\"\n" +
            "}\n" +
            "]\n" +
            "}";

        when(productCompositeService.createProductWithReviews(any())).thenReturn(productOne);

        this
            .mockMvc
            .perform(post("/client-api/v1/product-composite")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk());
    }

    private Product createProduct(Long id, String name, Integer weight, LocalDate createDate) {
        Product product = new Product();
        product.setProductId(id);
        product.setName(name);
        product.setWeight(weight);
        product.setCreateDate(createDate);
        return product;
    }

    private Review createReview(Long reviewId, Long productId, String author, String subject, String content) {
        Review review = new Review();
        review.setReviewId(reviewId);
        review.setProductId(productId);
        review.setAuthor(author);
        review.setSubject(subject);
        review.setContent(content);
        return review;
    }

}
