package RepositoryTests.Template;

import cz.inspire.EntityManagerProducer;
import cz.inspire.template.entity.PrintTemplateEntity;
import cz.inspire.template.repository.PrintTemplateRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PrintTemplateRepositoryTest {

    private PrintTemplateRepository printTemplateRepository;

    @Before
    public void setUp() {
        // Boot the CDI container
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();

        // Create the EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

        // Access EntityManagerProducer and set the EntityManagerFactory
        EntityManagerProducer producer = BeanProvider.getContextualReference(EntityManagerProducer.class);
        producer.setEntityManagerFactory(emf);

        // Access PrintTemplateRepository from CDI
        printTemplateRepository = BeanProvider.getContextualReference(PrintTemplateRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM PrintTemplateEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testSaveAndFindById() {
        assertNotNull("PrintTemplateRepository should be initialized!", printTemplateRepository);

        // Create and save a PrintTemplateEntity
        PrintTemplateEntity template = new PrintTemplateEntity("1", "Sample Content", 1, "TemplateName", "FileName");
        printTemplateRepository.save(template);

        // Test findById
        Optional<PrintTemplateEntity> retrievedTemplate = printTemplateRepository.findById("1");
        assertNotNull("Retrieved template should not be null", retrievedTemplate);
        assertEquals("Sample Content", retrievedTemplate.get().getContent());
    }

    @Test
    public void testSaveAndUpdate() {
        assertNotNull("PrintTemplateRepository should be initialized!", printTemplateRepository);

        // Create and save a PrintTemplateEntity
        PrintTemplateEntity template = new PrintTemplateEntity("2", "Initial Content", 2, "InitialName", "InitialFile");
        printTemplateRepository.save(template);

        // Update the entity
        template.setContent("Updated Content");
        printTemplateRepository.save(template);

        // Verify the update
        Optional<PrintTemplateEntity> updatedTemplate = printTemplateRepository.findById("2");
        assertNotNull("Updated template should not be null", updatedTemplate);
        assertEquals("Updated Content", updatedTemplate.get().getContent());
    }
}
