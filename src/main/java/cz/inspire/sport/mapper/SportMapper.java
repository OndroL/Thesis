package cz.inspire.sport.mapper;

import cz.inspire.sport.dto.SportDto;
import cz.inspire.sport.entity.SportEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.awt.Color;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI,
        uses = {SportLocMapper.class, InstructorMapper.class, SportKategorieMapper.class, ActivityMapper.class})
public interface SportMapper {

    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // Map DTO to Entity
    @Mapping(target = "sportKategorie", ignore = true)
    @Mapping(target = "nadrazenySport", ignore = true)
    @Mapping(target = "navazujiciSport", ignore = true)
    @Mapping(target = "activity", ignore = true)
    @Mapping(target = "instructorSet", ignore = true)
    @Mapping(target = "podrazeneSporty", ignore = true)
    @Mapping(target = "sportInstructors", ignore = true)
    @Mapping(target = "objekty", ignore = true)
    @Mapping(target = "barvaPopredi", source = "barvaPopredi", qualifiedByName = "colorToJson")
    @Mapping(target = "barvaPozadi",  source = "barvaPozadi",  qualifiedByName = "colorToJson")
    SportEntity toEntity(SportDto dto);

    // Map Entity to DTO
    @Mapping(target = "sportKategorie", source = "sportKategorie")
    @Mapping(target = "nadrazenySportId", source = "nadrazenySport.id")
    @Mapping(target = "navazujiciSportId", source = "navazujiciSport.id")
    @Mapping(target = "activityId", source = "activity.id")
    @Mapping(target = "barvaPopredi", source = "barvaPopredi", qualifiedByName = "jsonToColor")
    @Mapping(target = "barvaPozadi",  source = "barvaPozadi",  qualifiedByName = "jsonToColor")
    @Mapping(target = "instructors", ignore = true)
    SportDto toDto(SportEntity entity);


    @Named("jsonToColor")
    default Color jsonToColor(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            JsonNode root = OBJECT_MAPPER.readTree(json); // parse the JSON
            int r = root.path("r").asInt(0);
            int g = root.path("g").asInt(0);
            int b = root.path("b").asInt(0);
            int a = root.path("a").asInt(255);
            return new Color(r, g, b, a);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid JSON for color: " + json, e);
        }
    }
    @Named("colorToJson")
    default String colorToJson(Color color) {
        if (color == null) {
            return null;
        }
        JsonNode node = OBJECT_MAPPER.createObjectNode()
                .put("r", color.getRed())
                .put("g", color.getGreen())
                .put("b", color.getBlue())
                .put("a", color.getAlpha());
        return node.toString();
    }
}
