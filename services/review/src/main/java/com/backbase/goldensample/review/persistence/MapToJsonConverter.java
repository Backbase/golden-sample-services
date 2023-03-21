package com.backbase.goldensample.review.persistence;

import static org.springframework.util.CollectionUtils.isEmpty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
@Converter
public class MapToJsonConverter implements AttributeConverter<Map<String, String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final TypeReference<Map<String, String>> MAP_OF_STRINGS_TYPE_REFERENCE =
        new TypeReference<>() {
        };

    @Override
    public String convertToDatabaseColumn(Map<String, String> attribute) {
        if (!isEmpty(attribute)) {
            try {
                return objectMapper.writeValueAsString(attribute);
            } catch (JsonProcessingException e) {
                log.warn("Convert Error: {}", e.getLocalizedMessage());
            }
        }
        return null;
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String dbData) {
        if (!StringUtils.isEmpty(dbData)) {
            try {
                return objectMapper.readValue(dbData, MAP_OF_STRINGS_TYPE_REFERENCE);
            } catch (IOException ex) {
                log.warn("Convert Error: {}", ex.getLocalizedMessage());
            }
        }
        return null;
    }
}