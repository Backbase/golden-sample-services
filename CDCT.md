# Contract Driven Consumer Testing with Pact

Contract testing is a methodology for checking the compatibility of integration points between two separate systems. Each application is tested in isolation to confirm that the messages exchanged satisfy a shared expectation documented in a contract. Usually a contract is between a consumer (for example, a microservice that wishes to receive some data) and a provider (for example, a microservice that provides the required data). Contract testing goes beyond schema testing as it is necessary for both sides of the communication to come to an agreement on the expected interactions.

In consumer driven testing, the service consumer has control over what is tested. The tests are designed for specific known use cases and the consumer has assurance that the provider will supply responses as expected for their specified scenarios.

### Benefits

- Ensures independently developed systems will work together
- Provides a fast feedback cycle
- Testing is always completed against the latest version of the other application
- Prevents unexpected breaking changes
- Provides a clear compatibility matrix between versions of systems

## Pact

[Pact](https://docs.pact.io/) is a code-first consumer driven contract testing tool for validating HTTP and message integrations.

When using Pact, the consumer creates a collection of tests based on their specific use cases of a provider. This ensures that testing covers the parts of communication that are actually used by consumers. Each test case covers a single request/response pair. Pact generates a contract during the execution of these consumer tests which is shared with the provider to be validated. The required interactions captured in the contract are verified against the provider to confirm that the behaviour will match the expectations of the consumer.

Generally a broker is used to store pacts and verification results. This allows pact contracts to be easily shared between consumer and provider systems and manages versions of contracts which creates a clear compatibility matrix between multiple versions of systems. For more information, see [Sharing Pacts with the Pact Broker](https://docs.pact.io/getting_started/sharing_pacts/).

## Implementing CDCT

### As a consumer

Add the Pact dependency:

	<dependency>
		<groupId>au.com.dius.pact.consumer</groupId>
		<artifactId>junit5</artifactId>
		<version>${pact.version}</version>
	</dependency>

Create a test class which will contain test cases for a specific provider.

Annotate the class with:
	
	@Tag("PactConsumer")
	@ExtendWith(PactConsumerTestExt.class)
	@PactTestFor(providerName = "product_service", port = "4010")

Create a method which will create the pact for a single request/response pair. This defines an expected request and response using matchers and providing example values.

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
							.integerType(2021)
							.integerType(1)
							.integerType(9)
						.closeArray().asBody()
						.nullValue("additions"))
				.willRespondWith()
				.status(200)
				.body(new PactDslJsonBody()
						.integerType("id", 1))
				.headers(DEFAULT_RESPONSE_HEADERS)
				.toPact();
	}

For each pact there must be a corresponding test created which calls the endpoint with an example request and validates the received response. Note that the `pactMethod` defined in the `@PactTestFor` annotation must match the name of the method annotated with `@Pact`.

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
	
A successful execution of the tests will generate a pact contract which can then be shared with the provider and validated. By default this is located within `target/pacts`. Within this example the pacts are shared manually rather than using a broker.

### As a provider

Add the Pact dependencies:

	<dependency>
		<groupId>au.com.dius.pact.provider</groupId>
		<artifactId>junit5</artifactId>
		<version>${pact.version}</version>
	</dependency>
	
	<dependency>
		<groupId>au.com.dius.pact.provider</groupId>
		<artifactId>junit5spring</artifactId>
		<version>${pact.version}</version>
	</dependency>
	
Create a test class which will contain states for a specific consumer.

Annotate the class with:

	@Tag("PactProvider")
	@PactFolder("pacts")
	@Provider("product_service")
	@Consumer("store_service")
	@ExtendWith({SpringExtension.class})
	@ContextConfiguration(classes = {MvcConfiguration.class, JacksonAutoConfiguration.class, ApiConfiguration.class})
	
This tags the tests to allow provider tests to be run easily, defines the location of the pact contracts as well as the provider and consumer and sets up the required extensions and context configuration.
	
Set up the required mocks:

	public ProductServiceApiProviderPactTest() {
		service = Mockito.mock(ProductService.class);
		mapper = Mockito.mock(ProductMapper.class);
	}

Set up the configuration which specifies the controllers being tested:

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
    
For each state defined in the pact there must be a corresponding method created which mocks the expected behaviour. Note that the value defined in the `@State` annotation must match the state provided in the pact.

    @State("a product is created")
    public void productIsCreated() {
		when(service.createProduct(any(Product.class))).thenReturn(new ProductId().id(1l));
    }
    
When run, the tests will be validated against the pact to ensure that each state outlined by the consumer is covered and the responses provided match the consumer's expectations.