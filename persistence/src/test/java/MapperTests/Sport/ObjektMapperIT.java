package MapperTests.Sport;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.exception.InvalidParameterException;
import cz.inspire.exception.SystemException;
import cz.inspire.sport.dto.ObjektDto;
import cz.inspire.sport.dto.PodminkaRezervaceDto;
import cz.inspire.sport.dto.SportDto;
import cz.inspire.sport.entity.ObjektEntity;
import cz.inspire.sport.entity.PodminkaRezervaceEntity;
import cz.inspire.sport.entity.SportEntity;
import cz.inspire.sport.entity.SportInstructorEntity;
import cz.inspire.sport.mapper.ObjektMapper;
import cz.inspire.sport.mapper.SportMapper;
import cz.inspire.sport.service.ObjektService;
import cz.inspire.sport.service.SportService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ObjektMapperIT {

    private String id;

    @Inject
    ObjektMapper objektMapper;

    @Inject
    ObjektService objektService;

    @Inject
    SportService sportService;

    @Inject
    DatabaseCleaner databaseCleaner;

    @Inject
    SportMapper sportMapper;

    @BeforeAll
    @Transactional
    public void setup() {
        databaseCleaner.clearTable(ObjektEntity.class, true);
        databaseCleaner.clearTable(SportEntity.class, true);
        databaseCleaner.clearTable(PodminkaRezervaceEntity.class, true);
        databaseCleaner.clearTable(SportInstructorEntity.class, true);

    }

    @Order(1)
    @Test
    @Transactional
    public void testToEntity_withNadObjektyOnly() throws CreateException, FinderException, InvalidParameterException {
        Set<String> nadObjekty = new HashSet<>();
        // Create 5 existing ObjektEntity records and collect their IDs.
        for (int i = 0; i < 5; i++) {
            ObjektEntity existing = objektService.create(objektMapper.toEntity(DtoFactory.createObjektDto()));
            nadObjekty.add(existing.getId());
        }

        ObjektDto mainDto = DtoFactory.createObjektDto();
        mainDto.setNadObjekty(nadObjekty);

        ObjektEntity mainEntity = objektMapper.toEntity(mainDto);
        objektService.create(mainEntity);
        id = mainEntity.getId();

        ObjektEntity fromDb = objektService.findByPrimaryKey(id);
        int size = fromDb.getNadObjekty().size();

        assertNotNull(fromDb);
        assertEquals(5, size, "Should have 5 references on the owner side.");
    }

    @Order(2)
    @Test
    @Transactional
    public void testToEntity_withNadObjektyOnlyRetrieveOnly() throws FinderException {
        ObjektEntity fromDb = objektService.findByPrimaryKey(id);
        assertNotNull(fromDb);
        // Here we assume that the nadObjekty are mapped; we simply verify count.
        assertEquals(5, fromDb.getNadObjekty().size(), "Should have 5 references on the owner side.");
    }

    @Order(3)
    @Test
    @Transactional
    public void testToEntity_withSports() throws CreateException, FinderException, InvalidParameterException {
        List<SportDto> sports = new ArrayList<>();

        // Create 5 SportEntity records and update SportDto with generated IDs.
        for (int i = 0; i < 5; i++) {
            SportDto sportDto = DtoFactory.createSportDto();
            SportEntity sportEntity = sportService.create(sportMapper.toEntity(sportDto));
            sportDto.setId(sportEntity.getId());
            sports.add(sportDto);
        }

        ObjektDto objekt = DtoFactory.createObjektDto();
        objekt.setSports(sports);

        ObjektEntity objektEntity = objektMapper.toEntity(objekt);
        objektService.create(objektEntity);

        ObjektDto fromDb = objektMapper.toDto(objektService.findByPrimaryKey(objektEntity.getId()));
        int size = fromDb.getSports().size();

        assertNotNull(fromDb);
        assertEquals(5, size, "Should have 5 references to ObjektSportEntity");
    }

    @Order(4)
    @Test
    @Transactional
    public void testUpdateMapping_withSports() throws CreateException, FinderException, InvalidParameterException, SystemException {
        // Create an ObjektEntity with 3 sports.
        List<SportDto> initialSports = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SportDto sportDto = DtoFactory.createSportDto();
            SportEntity sportEntity = sportService.create(sportMapper.toEntity(sportDto));
            sportDto.setId(sportEntity.getId());
            initialSports.add(sportDto);
        }
        ObjektDto objektDto = DtoFactory.createObjektDto();
        objektDto.setSports(initialSports);

        ObjektEntity createdEntity = objektMapper.toEntity(objektDto);
        objektService.create(createdEntity);
        String objId = createdEntity.getId();
        assertNotNull(objId);

        // Now update: replace sports with 2 new sports.
        List<SportDto> newSports = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            SportDto sportDto = DtoFactory.createSportDto();
            SportEntity sportEntity = sportService.create(sportMapper.toEntity(sportDto));
            sportDto.setId(sportEntity.getId());
            newSports.add(sportDto);
        }
        ObjektDto updateDto = DtoFactory.createObjektDto();
        updateDto.setId(objId);
        updateDto.setSports(newSports);

        ObjektEntity updatedEntity = objektMapper.toEntity(updateDto);
        objektService.update(updatedEntity);

        ObjektDto fromDb = objektMapper.toDto(objektService.findByPrimaryKey(objId));
        int newSize = fromDb.getSports().size();
        assertEquals(2, newSize, "After update, should have 2 sports referenced");
    }

    @Order(5)
    @Test
    @Transactional
    public void testMapPodminkaRezervace() throws CreateException, FinderException, InvalidParameterException {
        List<PodminkaRezervaceDto> conditions = new ArrayList<>();
        // Create 3 PodminkaRezervaceDto entries.
        for (int i = 0; i < 3; i++) {
            PodminkaRezervaceDto podm = DtoFactory.createPodminkaRezervaceDto();
            podm.setName("Condition " + (i + 1));
            conditions.add(podm);
        }
        ObjektDto objektDto = DtoFactory.createObjektDto();
        objektDto.setPodminkyRezervaci(conditions);

        ObjektEntity objektEntity = objektMapper.toEntity(objektDto);
        // Ensure the podminkyRezervaci list is initialized.
        if (objektEntity.getPodminkyRezervaci() == null) {
            objektEntity.setPodminkyRezervaci(new ArrayList<>());
        }
        objektService.create(objektEntity);

        ObjektDto fromDb = objektMapper.toDto(objektService.findByPrimaryKey(objektEntity.getId()));
        List<PodminkaRezervaceDto> mappedConditions = fromDb.getPodminkyRezervaci();
        assertNotNull(mappedConditions);
        assertEquals(3, mappedConditions.size(), "Should have 3 reservation conditions mapped");

        // Check that priorities are set starting from 0.
        for (int i = 0; i < mappedConditions.size(); i++) {
            assertEquals(i, mappedConditions.get(i).getPriorita(), "Priority should be " + i);
        }
    }
}
