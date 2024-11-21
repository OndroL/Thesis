package cz.inspire.thesis.data.service;

import cz.inspire.thesis.data.model.Header;
import cz.inspire.thesis.data.repository.HeaderRepository;
import cz.inspire.thesis.exceptions.CreateException;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class HeaderServiceTest {

    @Inject
    private HeaderService headerService;

    @Inject
    private HeaderRepository headerRepository;

    @BeforeEach
    public void setUp() {
        // Boot CDI container
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();

        // Create the EntityManagerFactory and set it in the producer
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        BeanProvider.getContextualReference(cz.inspire.thesis.data.EntityManagerProducer.class)
                .setEntityManagerFactory(emf);

        // Inject dependencies
        headerService = BeanProvider.getContextualReference(HeaderService.class);
        headerRepository = BeanProvider.getContextualReference(HeaderRepository.class);
    }

    @Test
    public void testEjbCreate() throws CreateException {
        String id = headerService.ejbCreate("testId", 10, 1);

        // Verify that the entity was saved
        Header savedHeader = headerRepository.findBy(id);
        assertNotNull(savedHeader, "Header should be saved in the database");
        assertEquals("testId", savedHeader.getId());
        assertEquals(10, savedHeader.getField());
        assertEquals(-5, savedHeader.getLocation());
    }

    @Test
    public void testFindValidAttributes() throws CreateException {
        // Prepopulate the database
        headerService.ejbCreate("id1", 5, 0);  // Valid
        headerService.ejbCreate("id2", 8, -1); // Invalid (location < 0)
        headerService.ejbCreate("id3", 12, 2); // Valid

        // Test the service method
        List<Header> validHeaders = headerService.findValidAtributes();
        assertNotNull(validHeaders, "Valid attributes list should not be null");
        assertEquals(2, validHeaders.size(), "Only valid headers should be returned");

        // Verify contents
        assertTrue(validHeaders.stream().anyMatch(h -> h.getId().equals("id1")));
        assertTrue(validHeaders.stream().anyMatch(h -> h.getId().equals("id3")));
    }
}
