package cz.inspire.common.mapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import cz.inspire.common.entity.NastaveniEntity;
import cz.inspire.common.dto.NastaveniDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.MappingConstants;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;


@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface NastaveniMapper {
    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Mapping(source = "value", target = "value", qualifiedByName = "jsonNodeToSerializable")
    NastaveniDto toDto(NastaveniEntity entity);

    @Mapping(source = "value", target = "value", qualifiedByName = "serializableToJsonNode")
    NastaveniEntity toEntity(NastaveniDto dto);


    @Named("jsonNodeToSerializable")
    static Serializable jsonNodeToSerializable(JsonNode jsonNode) {
        if (jsonNode == null || !jsonNode.has("Value")) {
            return null;
        }

        JsonNode valueNode = jsonNode.get("Value");
        String className = jsonNode.has("ClassName") ? jsonNode.get("ClassName").asText() : "java.lang.String";

        try {
            Class<?> clazz = Class.forName(className);
            return (Serializable) OBJECT_MAPPER.treeToValue(valueNode, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing JSON to " + className, e);
        }
    }

    @Named("serializableToJsonNode")
    static JsonNode serializableToJsonNode(Serializable value) {
        if (value == null) {
            return null;
        }

        ObjectNode jsonNode = OBJECT_MAPPER.createObjectNode();
        jsonNode.put("ClassName", value.getClass().getName());
        jsonNode.putPOJO("Value", value);

        return jsonNode;
    }
}