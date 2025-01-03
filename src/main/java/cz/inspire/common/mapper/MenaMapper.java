package cz.inspire.common.mapper;

import cz.inspire.common.dto.MenaDto;
import cz.inspire.common.entity.MenaEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "cdi")
public interface MenaMapper {
    Logger logger = LogManager.getLogger(MenaMapper.class);

    @Mapping(target = "vycetkaList", expression = "java(parseVycetkaList(entity.getVycetka()))")
    MenaDto toDto(MenaEntity entity);
    MenaEntity toEntity(MenaDto dto);

    default List<BigDecimal> parseVycetkaList(String vycetkaDef) {
        List<BigDecimal> vycetka = new ArrayList<>();
        if (vycetkaDef != null && !vycetkaDef.isEmpty()) {
            String[] vycetkaSplit = vycetkaDef.split(";");
            for (String v : vycetkaSplit) {
                try {
                    vycetka.add(new BigDecimal(v.trim()));
                } catch (NumberFormatException e) {
                    logger.warn("Cannot parse vycetka: {}", v);
                }
            }
        }
        return vycetka;
    }
}
