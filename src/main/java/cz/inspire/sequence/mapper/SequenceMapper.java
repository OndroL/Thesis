package cz.inspire.sequence.mapper;

import cz.inspire.sequence.dto.SequenceDto;
import cz.inspire.sequence.entity.SequenceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "cdi")
public interface SequenceMapper {
    @Mapping(source = "stornoSeq.name", target = "stornoSeqName")
    SequenceDto toDto(SequenceEntity entity);

    @Mapping(target = "stornoSeq", ignore = true) // Relationship resolved in the facade
    SequenceEntity toEntity(SequenceDto dto);
}
