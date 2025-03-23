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
     * Tests saving and retrieving a NastaveniEntity by generated key.
     */
    @Test
    public void testSaveAndFindByKey() {
        JsonNode jsonNode = NastaveniMapper.serializableToJsonNode("This is a string");
        // Create entity with null key so that the key is generated.
        NastaveniEntity entity = new NastaveniEntity(null, jsonNode);
        entity = nastaveniRepository.create(entity);
        String generatedKey = entity.getKey();

        NastaveniEntity retrieved = nastaveniRepository.findByPrimaryKey(generatedKey);
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

        NastaveniEntity entityString = new NastaveniEntity(null, jsonNodeString);
        NastaveniEntity entityInteger = new NastaveniEntity(null, jsonNodeInteger);
        NastaveniEntity entityBoolean = new NastaveniEntity(null, jsonNodeBoolean);

        entityString = nastaveniRepository.create(entityString);
        entityInteger = nastaveniRepository.create(entityInteger);
        entityBoolean = nastaveniRepository.create(entityBoolean);

        NastaveniEntity retrievedStr = nastaveniRepository.findByPrimaryKey(entityString.getKey());
        NastaveniEntity retrievedInt = nastaveniRepository.findByPrimaryKey(entityInteger.getKey());
        NastaveniEntity retrievedBool = nastaveniRepository.findByPrimaryKey(entityBoolean.getKey());

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
        NastaveniEntity entity = new NastaveniEntity(null, initialValue);
        entity = nastaveniRepository.create(entity);
        String generatedKey = entity.getKey();

        // Update the value
        JsonNode updatedValue = NastaveniMapper.serializableToJsonNode("Updated Value");
        entity.setValue(updatedValue);
        // Update the entity (using create() as update mechanism in your repository)
        entity = nastaveniRepository.create(entity);

        NastaveniEntity updated = nastaveniRepository.findByPrimaryKey(generatedKey);
        Assertions.assertNotNull(updated, "Entity should still exist after update.");
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
        NastaveniEntity entity = new NastaveniEntity(null, jsonNode);
        entity = nastaveniRepository.create(entity);
        String generatedKey = entity.getKey();

        nastaveniRepository.deleteByPrimaryKey(generatedKey);
        NastaveniEntity deleted = nastaveniRepository.findByPrimaryKey(generatedKey);
        Assertions.assertNull(deleted, "Entity should be deleted from repository.");
    }

    /**
     * Tests that retrieving a non-existent entity returns empty.
     */
    @Test
    public void testFindNonExistentEntity() {
        NastaveniEntity retrieved = nastaveniRepository.findByPrimaryKey("non-existent");
        Assertions.assertNull(retrieved, "Should return empty for non-existent entity.");
    }

    /**
     * Tests storing a class as value and retrieving it.
     */
    @Test
    public void testStoreAndRetrieveMenaEntity() {
        MenaDto menaDto = new MenaDto("ID-001", "EUR", "Euro", 978, 0, 0, new ArrayList<>());

        JsonNode jsonNode = NastaveniMapper.serializableToJsonNode(menaDto);

        NastaveniEntity nastaveniEntity = new NastaveniEntity(null, jsonNode);
        nastaveniEntity = nastaveniRepository.create(nastaveniEntity);
        String generatedKey = nastaveniEntity.getKey();

        // Retrieve it from the database
        NastaveniEntity retrievedEntity = nastaveniRepository.findByPrimaryKey(generatedKey);
        Assertions.assertNotNull(retrievedEntity, "Entity should be present in repository.");

        // Convert JsonNode back to MenaDto
        Serializable retrievedObject = NastaveniMapper.jsonNodeToSerializable(retrievedEntity.getValue());
        Assertions.assertInstanceOf(MenaDto.class, retrievedObject, "Expected retrieved object to be a MenaDto");

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
