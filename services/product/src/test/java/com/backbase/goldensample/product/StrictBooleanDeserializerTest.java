package com.backbase.goldensample.product;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Test that only boolean values that are deserialized
 * with {@link StrictBooleanDeserializer} return true, false respectively
 *
 *
 */
public class StrictBooleanDeserializerTest {
	
	private ObjectMapper mapper;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		mapper = new ObjectMapper();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		mapper = null;
	}

	@Test
	public void is_true() throws JsonParseException, IOException {
		String yes = "true";
		Boolean result = deserialize(yes);
		assertTrue(result);
	}
	
	@Test
	public void is_false() throws JsonParseException, IOException {
		String no = "false";
		Boolean result = deserialize(no);
		assertFalse(result);
	}
	
	@Test
	public void nullValue() throws JsonParseException, IOException {
		// null values must deserialize as Boolean.FALSE to support primitives
		Boolean result = deserialize(null);		
		assertFalse(result);
	}
	
	@Test(expected=JsonMappingException.class)
	public void is_number() throws JsonParseException, IOException {
		deserialize("12");
	}

	@Test(expected=JsonMappingException.class)
	public void is_string() throws JsonParseException, IOException {
		String random_string = "\"" + "random_string" + "\"";
		deserialize(random_string);
	}

	private Boolean deserialize(String value) throws IOException, JsonParseException,
			JsonProcessingException {
		TestObject testObject = mapper.readValue("{\"exists\":" +  value + "}", TestObject.class);
		
		return testObject.getExists();
	}
	
	private static class TestObject {
		
		@JsonDeserialize(using = StrictBooleanDeserializer.class)
		private final Boolean exists = null;
		
		public Boolean getExists() {
			return exists;
		}
	}

}
