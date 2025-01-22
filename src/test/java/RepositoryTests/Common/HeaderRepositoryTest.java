package RepositoryTests.Common;

import cz.inspire.common.entity.HeaderEntity;
import cz.inspire.common.repository.HeaderRepository;
import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.apache.logging.log4j.LogManager;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class HeaderRepositoryTest {

    @Inject
    private HeaderRepository headerRepository;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class)
                .addPackage(HeaderEntity.class.getPackage()) // Include your entity package
                .addPackage(HeaderRepository.class.getPackage()) // Include your repository package
                .addAsResource("META-INF/persistence.xml"); // Include persistence configuration
        System.out.println(archive.toString(true)); // Print the archive contents
        return archive;
    }

    @Test
    public void testFindValidAttributes() {
        assertNotNull("HeaderRepository should be initialized!", headerRepository);

        // Save some entities using repository functions
        headerRepository.save(new HeaderEntity("1", 10, 1));
        headerRepository.save(new HeaderEntity("2", 20, -5)); // Invalid location
        headerRepository.save(new HeaderEntity("3", 30, 15));

        // Query valid attributes
        List<HeaderEntity> validAttributes = headerRepository.findValidAttributes();

        // Assertions
        assertNotNull(validAttributes);
        assertEquals(2, validAttributes.size());
        assertEquals("1", validAttributes.get(0).getId());
        assertEquals("3", validAttributes.get(1).getId());
    }
}