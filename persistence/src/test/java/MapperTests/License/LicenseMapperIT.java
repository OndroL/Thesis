package MapperTests.License;

import RepositoryTests.DatabaseCleaner;
import cz.inspire.license.dto.LicenseDto;
import cz.inspire.license.entity.LicenseEntity;
import cz.inspire.license.mapper.LicenseMapper;
import cz.inspire.license.service.LicenseService;
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

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LicenseMapperIT {

    @Inject
    LicenseMapper licenseMapper;

    @Inject
    LicenseService licenseService; // Replace with actual service/repository

    @Inject
    DatabaseCleaner databaseCleaner;

    private String licenseId;

    @BeforeAll
    @Transactional
    public void setup() {
        databaseCleaner.clearTable(LicenseEntity.class, true);
    }

    @Order(1)
    @Test
    @Transactional
    public void testPersistLicense() throws CreateException {
        LicenseDto dto = new LicenseDto();
        dto.setCenterId(101);
        dto.setCustomer("Test Customer");
        dto.setValid(true);
        dto.setCenterOnline(false);
        dto.setValidFromSet(true);
        dto.setValidFrom(new Date());
        dto.setValidToSet(true);
        dto.setValidTo(new Date());
        dto.setActivityLimit(10);
        dto.setSportCenterLimit(5);
        dto.setSportCustomersLimit(50);
        dto.setUsersLimit(3);
        dto.setCustomerGroupsLimit(2);
        dto.setPokladnaLimit(1);
        dto.setSkladLimit(1);
        dto.setOvladaniQuido(true);
        dto.setModules(123456L);
        dto.setMaxClients(999);
        dto.setHash("abcdef12345");
        dto.setCreatedDate(new Date());
        dto.setGeneratedDate(new Date());

        LicenseEntity entity = licenseMapper.toEntity(dto);
        licenseService.create(entity);
        licenseId = entity.getId();

        assertNotNull(licenseId);
        assertEquals(101, entity.getCenterId());
        assertTrue(entity.getValid());
        assertFalse(entity.getCenterOnline());
        assertNotNull(entity.getCreatedDate());
    }

    @Order(2)
    @Test
    @Transactional
    public void testRetrieveLicense() throws FinderException {
        LicenseEntity entity = licenseService.findByPrimaryKey(licenseId);
        assertNotNull(entity);

        LicenseDto dto = licenseMapper.toDto(entity);
        assertEquals(licenseId, dto.getId());
        assertEquals(101, dto.getCenterId());
        assertTrue(dto.getValid());
        assertFalse(dto.getCenterOnline());
        assertNotNull(dto.getCreatedDate());
    }
}
