package com.backbase.goldensample.product.pact.provider;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Consumer;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import au.com.dius.pact.provider.spring.junit5.MockMvcTestTarget;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import com.backbase.buildingblocks.backend.api.MvcConfiguration;
import com.backbase.buildingblocks.backend.api.config.ApiConfiguration;
import com.backbase.buildingblocks.backend.api.exceptionhandler.AbstractApiExceptionHandler;
import com.backbase.goldensample.product.api.ProductServiceApiController;
import com.backbase.goldensample.product.mapper.ProductMapper;
import com.backbase.goldensample.product.service.ProductService;
import com.backbase.product.api.service.v1.model.Product;
import com.backbase.product.api.service.v1.model.ProductId;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Pact provider tests for interactions between the Store service and Product service-api endpoints.
 * It uses Spring to configure certain MVC features without requiring the whole Spring Boot context to keep the tests quick.
 * This class has a PactProvider JUnit 5 annotation tag to allow all provider tests to be run in a convenient manner.
 */
@Tag("PactProvider")
@PactFolder("pacts")
@Provider("product_service")
@Consumer("store_service")
@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {MvcConfiguration.class, JacksonAutoConfiguration.class, ApiConfiguration.class})
class ProductServiceApiProviderPactTest {
	
    @Autowired
    private ObjectMapper objectMapper;
	
    @Autowired
    private List<AbstractApiExceptionHandler> exceptionHandlers;
	
	private final ProductService service;
	private final ProductMapper mapper;
	
	public ProductServiceApiProviderPactTest() {
		service = Mockito.mock(ProductService.class);
		mapper = Mockito.mock(ProductMapper.class);
	}
	
    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        if (context != null) {
            context.verifyInteraction();
        }
    }
    
    @BeforeEach
    void before(PactVerificationContext context) {
        if (context != null) {
            context.setTarget(new MockMvcTestTarget(MockMvcBuilders
                                    .standaloneSetup(new ProductServiceApiController(service, mapper))
                                    .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                                    .setControllerAdvice(exceptionHandlers)
                                    .build()));
        }
    }
    
    @State("a product is created")
    public void productIsCreated() {
		when(service.createProduct(any(Product.class))).thenReturn(new ProductId().id(1L));
    }

}
