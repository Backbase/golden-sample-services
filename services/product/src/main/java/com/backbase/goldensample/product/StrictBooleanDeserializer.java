package com.backbase.goldensample.product;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class StrictBooleanDeserializer extends JsonDeserializer {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken text = p.getCurrentToken();
        if (text == JsonToken.VALUE_TRUE) {
            return Boolean.TRUE;
        }
        if (text == JsonToken.VALUE_FALSE) {
            return Boolean.FALSE;
        }
        if (text.equals(JsonToken.VALUE_NULL)) {
            return getNullValue(ctxt);
        }
        throw ctxt.weirdStringException(text.asString(), Boolean.class,
            "Only boolean values supported");
    }

    @Override
    public Boolean getNullValue(DeserializationContext ctxt) {
        return Boolean.FALSE;
    }
}
