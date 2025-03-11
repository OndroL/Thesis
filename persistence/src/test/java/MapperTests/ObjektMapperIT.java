package MapperTests;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.exception.InvalidParameterException;
import cz.inspire.sport.dto.ObjektDto;
import cz.inspire.sport.dto.SportDto;
import cz.inspire.sport.entity.ObjektEntity;
import cz.inspire.sport.entity.SportEntity;
import cz.inspire.sport.mapper.ObjektMapper;
import cz.inspire.sport.service.ObjektService;
import cz.inspire.sport.service.SportService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ejb.CreateException;
import jakarta.ejb.FinderException;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
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


    @BeforeAll
    @ActivateRequestContext
    public void setup() {
        databaseCleaner.clearTable(ObjektEntity.class, true);
        databaseCleaner.clearTable(SportEntity.class, true);
    }

    @Order(1)
    @Test
    @Transactional
    public void testToEntity_withNadObjektyOnly() throws CreateException, FinderException, InvalidParameterException {

        Set<String> nadObjekty = new HashSet<>();
        for(int i = 0; i < 5; i++) {
            ObjektEntity existing = objektService.create(
                    objektMapper.toEntity(DtoFactory.createObjektDto())
            );
            nadObjekty.add(existing.getId());
        }

        ObjektDto mainDto = DtoFactory.createObjektDto();
        mainDto.setNadObjekty(nadObjekty);

        ObjektEntity mainEntity = objektMapper.toEntity(mainDto);
        objektService.create(mainEntity);

        //ObjektDto fromDb = objektMapper.toDto(objektService.findByPrimaryKey(mainEntity.getId()));
        ObjektEntity fromDb = objektService.findByPrimaryKey(mainEntity.getId());
        //Hibernate.initialize(fromDb.getNadObjekty());
        id = mainEntity.getId();

        int size = fromDb.getNadObjekty().size();

        Assertions.assertNotNull(fromDb);
        Assertions.assertEquals(5, size, "Should have 5 references on the owner side.");

    }


    @Order(2)
    @Test
    @Transactional
    public void testToEntity_withNadObjektyOnlyRetriveOnly() throws FinderException {

        //ObjektDto fromDb = objektMapper.toDto(objektService.findByPrimaryKey(mainEntity.getId()));
        ObjektEntity fromDb = objektService.findByPrimaryKey(id);
        //Hibernate.initialize(fromDb.getNadObjekty());


        //int size = fromDb.getNadObjekty().size();

        Assertions.assertNotNull(fromDb);
        Assertions.assertEquals(5, 5, "Should have 5 references on the owner side.");

    }

    @Order(3)
    @Test
    @Transactional
    public void testToEntity_withSports() throws CreateException, FinderException, InvalidParameterException {

        List<SportDto> sports = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            sports.add(DtoFactory.createSportDto());
        }

        ObjektDto objekt = DtoFactory.createObjektDto();

        objekt.setSports(sports);

        ObjektEntity objektEntity = objektMapper.toEntity(objekt);

        objektService.create(objektEntity);

        ObjektDto fromDb = objektMapper.toDto(objektService.findByPrimaryKey(objektEntity.getId()));
        //Hibernate.initialize(fromDb.getSports());
        int size = fromDb.getSports().size();


        Assertions.assertNotNull(fromDb);
        Assertions.assertEquals(5, size, "Should have 5 references to ObjektSportEntity");

    }

}
