package cz.inspire.sport.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OtviraciDobaConverter implements AttributeConverter<OtviraciDoba, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule()) // Support for Java 8 time types
            .enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    public String convertToDatabaseColumn(OtviraciDoba attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing OtviraciDoba to JSON", e);
        }
    }

    @Override
    public OtviraciDoba convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(dbData, SimpleOtviraciDoba.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing JSON to OtviraciDoba", e);
        }
    }
}
