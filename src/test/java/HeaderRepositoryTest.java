package cz.inspire.thesis.data.repository;
import cz.inspire.thesis.data.model.Header;
import jakarta.enterprise.inject.spi.CDI;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
public class HeaderRepositoryTest {

    private HeaderRepository headerRepository;

    @Before
    public void setUp() {
        // Boot the CDI container
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();

        // Access the HeaderRepository from CDI
        headerRepository = CDI.current().select(HeaderRepository.class).get();
    }

    @Test
    public void testFindValidAttributes() {
        assertNotNull("HeaderRepository should be initialized!", headerRepository);

        // Test saving and querying a header
        headerRepository.save(new Header("1", 10, 1));
        List<Header> validHeaders = headerRepository.findValidAtributes();

        assertEquals(1, validHeaders.size());
    }
}