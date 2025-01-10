package FacadeTests.Sequence;

import cz.inspire.sequence.dto.SequenceDto;
import cz.inspire.sequence.entity.SequenceEntity;
import cz.inspire.sequence.mapper.SequenceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class SequenceMapperTest {

    private SequenceMapper sequenceMapper;

    @BeforeEach
    void setUp() {
        sequenceMapper = Mappers.getMapper(SequenceMapper.class);
    }

    @Test
    void testToDto() {
        // Nested stornoSeq entity
        SequenceEntity stornoSeq = new SequenceEntity("storno1", "pattern", 10, "last", 2, null);
        // Main entity
        SequenceEntity entity = new SequenceEntity("seq1", "pattern", 1, "last", 2, stornoSeq);

        // Map to DTO
        SequenceDto dto = sequenceMapper.toDto(entity);

        // Assertions
        assertNotNull(dto);
        assertEquals("seq1", dto.getName());
        assertEquals("pattern", dto.getPattern());
        assertEquals(1, dto.getMinValue());
        assertEquals("last", dto.getLast());
        assertEquals(2, dto.getType());
        assertEquals("storno1", dto.getStornoSeqName());
    }

    @Test
    void testToDto_NullStornoSeq() {
        // Entity without stornoSeq
        SequenceEntity entity = new SequenceEntity("seq1", "pattern", 1, "last", 2, null);

        // Map to DTO
        SequenceDto dto = sequenceMapper.toDto(entity);

        // Assertions
        assertNotNull(dto);
        assertEquals("seq1", dto.getName());
        assertNull(dto.getStornoSeqName());
    }

    @Test
    void testToEntity() {
        // DTO with stornoSeqName
        SequenceDto dto = new SequenceDto("seq1", "pattern", 1, "last", 2, "storno1");

        // Map to Entity
        SequenceEntity entity = sequenceMapper.toEntity(dto);

        // Assertions
        assertNotNull(entity);
        assertEquals("seq1", entity.getName());
        assertEquals("pattern", entity.getPattern());
        assertEquals(1, entity.getMinValue());
        assertEquals("last", entity.getLast());
        assertEquals(2, entity.getType());
        assertNotNull(entity.getStornoSeq());
        assertEquals("storno1", entity.getStornoSeq().getName());
    }

    @Test
    void testToEntity_NullStornoSeqName() {
        // DTO without stornoSeqName
        SequenceDto dto = new SequenceDto("seq1", "pattern", 1, "last", 2, null);

        // Map to Entity
        SequenceEntity entity = sequenceMapper.toEntity(dto);

        // Assertions
        assertNotNull(entity);
        assertEquals("seq1", entity.getName());
        assertNull(entity.getStornoSeq()); // Verify stornoSeq is null
    }
}
