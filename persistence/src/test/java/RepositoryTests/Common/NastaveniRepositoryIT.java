package RepositoryTests.Common;

import com.fasterxml.jackson.databind.JsonNode;
import cz.inspire.common.dto.MenaDto;
import cz.inspire.common.entity.NastaveniEntity;
import cz.inspire.common.mapper.NastaveniMapper;
import cz.inspire.common.repository.NastaveniRepository;
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
        List<NastaveniEntity> allEntities = new ArrayList<>(nastaveniRepository.findAll());
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
        nastaveniRepository.create(entity);

        NastaveniEntity retrieved = nastaveniRepository.findById("setting-1");
        Assertions.assertNotNull(retrieved, "Entity should be present in repository.");
        Assertions.assertEquals("This is a string",
                NastaveniMapper.jsonNodeToSerializable(retrieved.getValue()),
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

        nastaveniRepository.create(entityString);
        nastaveniRepository.create(entityInteger);
        nastaveniRepository.create(entityBoolean);

        NastaveniEntity retrievedStr = nastaveniRepository.findById("str-setting");
        NastaveniEntity retrievedInt = nastaveniRepository.findById("int-setting");
        NastaveniEntity retrievedBool = nastaveniRepository.findById("bool-setting");

        Assertions.assertEquals("Test String",
                NastaveniMapper.jsonNodeToSerializable(retrievedStr.getValue()),
                "String value should match.");
        Assertions.assertEquals(12345,
                NastaveniMapper.jsonNodeToSerializable(retrievedInt.getValue()),
                "Integer value should match.");
        Assertions.assertEquals(true,
                NastaveniMapper.jsonNodeToSerializable(retrievedBool.getValue()),
                "Boolean value should match.");
    }

    /**
     * Tests updating an existing entity.
     */
    @Test
    public void testUpdateEntity() {
        JsonNode initialValue = NastaveniMapper.serializableToJsonNode(10);
        NastaveniEntity entity = new NastaveniEntity("setting-2", initialValue);
        nastaveniRepository.create(entity);

        // Update the value
        JsonNode updatedValue = NastaveniMapper.serializableToJsonNode("Updated Value");
        entity.setValue(updatedValue);
        nastaveniRepository.create(entity);

        NastaveniEntity updated = nastaveniRepository.findById("setting-2");
        Assertions.assertNull(updated, "Entity should still exist after update.");
        Assertions.assertEquals("Updated Value",
                NastaveniMapper.jsonNodeToSerializable(updated.getValue()),
                "Updated value should match.");
    }

    /**
     * Tests deleting an entity.
     */
    @Test
    public void testDeleteEntity() {
        JsonNode jsonNode = NastaveniMapper.serializableToJsonNode(false);
        NastaveniEntity entity = new NastaveniEntity("setting-3", jsonNode);
        nastaveniRepository.create(entity);

        nastaveniRepository.deleteById("setting-3");
        NastaveniEntity deleted = nastaveniRepository.findById("setting-3");
        Assertions.assertNull(deleted, "Entity should be deleted from repository.");
    }

    /**
     * Tests that retrieving a non-existent entity returns empty.
     */
    @Test
    public void testFindNonExistentEntity() {
        NastaveniEntity retrieved = nastaveniRepository.findById("non-existent");
        Assertions.assertNull(retrieved, "Should return empty for non-existent entity.");
    }

    /**
     * Tests that stores class as value and retrieves it.
     */
    @Test
    public void testStoreAndRetrieveMenaEntity() {
        MenaDto menaDto = new MenaDto("ID-001", "EUR", "Euro", 978, 0, 0, new ArrayList<BigDecimal>());

        JsonNode jsonNode = NastaveniMapper.serializableToJsonNode(menaDto);

        NastaveniEntity nastaveniEntity = new NastaveniEntity("mena-setting", jsonNode);
        nastaveniRepository.create(nastaveniEntity);

        // Retrieve it from the database
        NastaveniEntity retrievedEntity = nastaveniRepository.findById("mena-setting");
        Assertions.assertNull(retrievedEntity, "Entity should be present in repository.");

        // Convert JsonNode back to MenaEntity
        Serializable retrievedObject = NastaveniMapper.jsonNodeToSerializable(retrievedEntity.getValue());
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
