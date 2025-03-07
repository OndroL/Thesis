package cz.inspire.enterprise.module.sport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.inspire.common.util.PeriodOfTime;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Map;

@Converter(autoApply = true)
public class OtviraciDobaConverter implements AttributeConverter<OtviraciDoba, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .enable(SerializationFeature.INDENT_OUTPUT)
            .registerModule(new SimpleModule() {{
                addAbstractTypeMapping(OtviraciDoba.class, RozsirenaTydenniOtviraciDoba.class);
                addKeySerializer(PeriodOfTime.class, new PeriodOfTimeKeySerializer());
                addKeyDeserializer(PeriodOfTime.class, new PeriodOfTimeKeyDeserializer());

                addKeySerializer(PeriodOfTimeInDay.class, new PeriodOfTimeKeySerializer());
                addKeyDeserializer(PeriodOfTimeInDay.class, new PeriodOfTimeKeyDeserializer());
            }});

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
            JsonNode rootNode = objectMapper.readTree(dbData);

            if (rootNode.has("doby")) {
                JsonNode firstDayNode = rootNode.get("doby").get(0);

                if (firstDayNode != null && !firstDayNode.isEmpty()) {
                    Map.Entry<String, JsonNode> firstEntry = firstDayNode.fields().next();
                    JsonNode valueNode = firstEntry.getValue();

                    if (valueNode.isArray()) {
                        return objectMapper.treeToValue(rootNode, RozsirenaTydenniOtviraciDoba.class);
                    } else if (valueNode.isTextual()) {
                        return objectMapper.treeToValue(rootNode, TydeniOtviraciDoba.class);
                    }
                }
            }

            return objectMapper.treeToValue(rootNode, TydeniOtviraciDoba.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing JSON to OtviraciDoba", e);
        }
    }

}
