package com.backbase.goldensample.store.pact.consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.backbase.goldensample.product.api.client.ApiClient;
import com.backbase.goldensample.product.api.client.v1.ProductServiceApi;
import com.backbase.goldensample.product.api.client.v1.model.Product;
import com.backbase.goldensample.product.api.client.v1.model.ProductId;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * Pact consumer tests for interactions between the Store service and Product service-api endpoints.
 * This class has a PactConsumer JUnit 5 annotation tag to allow all consumer tests to be run in a convenient manner.
 */
@Tag("PactConsumer")
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "product_service", port = "4010")
class ProductServiceApiConsumerPactTest {
	
	private static final Map<String, String> DEFAULT_RESPONSE_HEADERS =
	        Collections.singletonMap("Content-Type", "application/json");
	
	private ProductServiceApi client = new ProductServiceApi(new ApiClient());
	
	@Pact(consumer = "store_service")
	public RequestResponsePact createProduct(PactDslWithProvider builder) {
		return builder
				.given("a product is created")
				.uponReceiving("a valid request")
				.matchPath("/service-api/v1/products")
				.method("POST")
				.headers(Map.of(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
				.body(new PactDslJsonBody()
						.stringType("name", "Coke")
						.integerType("productId", 1)
						.integerType("weight", 100)
						.array("createDate")
							.integerType(2021l)
							.integerType(1l)
							.integerType(9l)
						.closeArray().asBody()
						.nullValue("additions"))
				.willRespondWith()
				.status(200)
				.body(new PactDslJsonBody()
						.integerType("id", 1))
				.headers(DEFAULT_RESPONSE_HEADERS)
				.toPact();
	}
	
	@Test
	@PactTestFor(pactMethod = "createProduct")
	void testCreateProduct() {
		
		ProductId productId = client.postProduct(new Product()
				.name("Coke")
				.productId(1l)
				.weight(100)
				.createDate(LocalDate.of(2021, 1, 9)));
		
		assertEquals(1l, productId.getId());
	}
}
