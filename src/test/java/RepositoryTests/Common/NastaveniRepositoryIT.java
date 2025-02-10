package RepositoryTests.Common;

import com.fasterxml.jackson.databind.JsonNode;
import cz.inspire.common.dto.MenaDto;
import cz.inspire.common.entity.NastaveniEntity;
import cz.inspire.common.repository.NastaveniRepository;
import cz.inspire.common.mapper.NastaveniMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@QuarkusTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Allows non-static @BeforeAll
public class NastaveniRepositoryIT {

    @Inject
    NastaveniRepository nastaveniRepository;

    /**
     * Clears the database before tests to ensure isolation.
     */
    @BeforeAll
    @ActivateRequestContext
    public void clearDatabase() {
        List<NastaveniEntity> allEntities = new ArrayList<>();
        nastaveniRepository.findAll().forEach(allEntities::add);
        if (!allEntities.isEmpty()) {
           nastaveniRepository.deleteAll(allEntities);
        }
    }

    /**
     * Tests saving and retrieving a NastaveniEntity by key.
     */
    @Test
    public void testSaveAndFindByKey() {
        JsonNode jsonNode = NastaveniMapper.serializableToJsonNode("This is a string");
        NastaveniEntity entity = new NastaveniEntity("setting-1", jsonNode);
        nastaveniRepository.save(entity);

        Optional<NastaveniEntity> retrieved = nastaveniRepository.findById("setting-1");
        Assertions.assertTrue(retrieved.isPresent(), "Entity should be present in repository.");
        Assertions.assertEquals("This is a string",
                NastaveniMapper.jsonNodeToSerializable(retrieved.get().getValue()),
                "Stored value should match.");
    }

    /**
     * Tests storing different types in JSONB.
     */
    @Test
    public void testSaveDifferentTypes() {
        JsonNode jsonNodeString = NastaveniMapper.serializableToJsonNode("Test String");
        JsonNode jsonNodeInteger = NastaveniMapper.serializableToJsonNode(12345);
        JsonNode jsonNodeBoolean = NastaveniMapper.serializableToJsonNode(true);

        NastaveniEntity entityString = new NastaveniEntity("str-setting", jsonNodeString);
        NastaveniEntity entityInteger = new NastaveniEntity("int-setting", jsonNodeInteger);
        NastaveniEntity entityBoolean = new NastaveniEntity("bool-setting", jsonNodeBoolean);

        nastaveniRepository.save(entityString);
        nastaveniRepository.save(entityInteger);
        nastaveniRepository.save(entityBoolean);

        Optional<NastaveniEntity> retrievedStr = nastaveniRepository.findById("str-setting");
        Optional<NastaveniEntity> retrievedInt = nastaveniRepository.findById("int-setting");
        Optional<NastaveniEntity> retrievedBool = nastaveniRepository.findById("bool-setting");

        Assertions.assertEquals("Test String",
                NastaveniMapper.jsonNodeToSerializable(retrievedStr.get().getValue()),
                "String value should match.");
        Assertions.assertEquals(12345,
                NastaveniMapper.jsonNodeToSerializable(retrievedInt.get().getValue()),
                "Integer value should match.");
        Assertions.assertEquals(true,
                NastaveniMapper.jsonNodeToSerializable(retrievedBool.get().getValue()),
                "Boolean value should match.");
    }

    /**
     * Tests updating an existing entity.
     */
    @Test
    public void testUpdateEntity() {
        JsonNode initialValue = NastaveniMapper.serializableToJsonNode(10);
        NastaveniEntity entity = new NastaveniEntity("setting-2", initialValue);
        nastaveniRepository.save(entity);

        // Update the value
        JsonNode updatedValue = NastaveniMapper.serializableToJsonNode("Updated Value");
        entity.setValue(updatedValue);
        nastaveniRepository.save(entity);

        Optional<NastaveniEntity> updated = nastaveniRepository.findById("setting-2");
        Assertions.assertTrue(updated.isPresent(), "Entity should still exist after update.");
        Assertions.assertEquals("Updated Value",
                NastaveniMapper.jsonNodeToSerializable(updated.get().getValue()),
                "Updated value should match.");
    }

    /**
     * Tests deleting an entity.
     */
    @Test
    public void testDeleteEntity() {
        JsonNode jsonNode = NastaveniMapper.serializableToJsonNode(false);
        NastaveniEntity entity = new NastaveniEntity("setting-3", jsonNode);
        nastaveniRepository.save(entity);

        nastaveniRepository.deleteById("setting-3");
        Optional<NastaveniEntity> deleted = nastaveniRepository.findById("setting-3");
        Assertions.assertFalse(deleted.isPresent(), "Entity should be deleted from repository.");
    }

    /**
     * Tests that retrieving a non-existent entity returns empty.
     */
    @Test
    public void testFindNonExistentEntity() {
        Optional<NastaveniEntity> retrieved = nastaveniRepository.findById("non-existent");
        Assertions.assertFalse(retrieved.isPresent(), "Should return empty for non-existent entity.");
    }

    /**
     * Tests that stores class as value and retrieves it.
     */
    @Test
    public void testStoreAndRetrieveMenaEntity() {
        MenaDto menaDto = new MenaDto("ID-001", "EUR", "Euro", 978, 0, 0, new ArrayList<BigDecimal>());

        JsonNode jsonNode = NastaveniMapper.serializableToJsonNode(menaDto);

        NastaveniEntity nastaveniEntity = new NastaveniEntity("mena-setting", jsonNode);
        nastaveniRepository.save(nastaveniEntity);

        // Retrieve it from the database
        Optional<NastaveniEntity> retrievedEntity = nastaveniRepository.findById("mena-setting");
        Assertions.assertTrue(retrievedEntity.isPresent(), "Entity should be present in repository.");

        // Convert JsonNode back to MenaEntity
        Serializable retrievedObject = NastaveniMapper.jsonNodeToSerializable(retrievedEntity.get().getValue());
        Assertions.assertTrue(retrievedObject instanceof MenaDto, "Expected retrieved object to be a MenaEntity");

        // Verify all attributes match
        MenaDto retrievedMena = (MenaDto) retrievedObject;
        Assertions.assertEquals(menaDto.getId(), retrievedMena.getId(), "ID should match");
        Assertions.assertEquals(menaDto.getKod(), retrievedMena.getKod(), "Kod should match");
        Assertions.assertEquals(menaDto.getVycetka(), retrievedMena.getVycetka(), "Vycetka should match");
        Assertions.assertEquals(menaDto.getKodNum(), retrievedMena.getKodNum(), "KodNum should match");
        Assertions.assertEquals(menaDto.getZaokrouhleniHotovost(), retrievedMena.getZaokrouhleniHotovost(), "ZaokrouhleniHotovost should match");
        Assertions.assertEquals(menaDto.getZaokrouhleniKarta(), retrievedMena.getZaokrouhleniKarta(), "ZaokrouhleniKarta should match");
    }

}
