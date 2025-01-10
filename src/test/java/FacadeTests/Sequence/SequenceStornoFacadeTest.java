package FacadeTests.Sequence;

import cz.inspire.EntityManagerProducer;
import cz.inspire.sequence.dto.SequenceDto;
import cz.inspire.sequence.entity.SequenceEntity;
import cz.inspire.sequence.facade.SequenceFacade;
import cz.inspire.sequence.repository.SequenceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class SequenceStornoFacadeTest {

    private SequenceFacade sequenceFacade;
    private SequenceRepository sequenceRepository;

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

        // Access SequenceFacade and SequenceRepository from CDI
        sequenceFacade = BeanProvider.getContextualReference(SequenceFacade.class);
        sequenceRepository = BeanProvider.getContextualReference(SequenceRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM SequenceEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testCreateWithStornoSeq() throws Exception {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);

        // Begin a transaction to persist stornoSeq
        em.getTransaction().begin();
        SequenceEntity stornoSeq = new SequenceEntity("stornoSeq1", "pattern", 1, "last", 2, null);
        em.persist(stornoSeq);
        em.getTransaction().commit();

        // Verify that stornoSeq is saved
        Optional<SequenceEntity> stornoCheck = sequenceRepository.findById("stornoSeq1");
        assertTrue("Storno sequence should exist", stornoCheck.isPresent());

        // Begin a new transaction for facade
        em.getTransaction().begin();
        SequenceDto dto = new SequenceDto("111111", "pattern", 1, "last", 2, "stornoSeq1");
        String generatedName = sequenceFacade.create(dto);
        em.getTransaction().commit();

        // Verify the saved main sequence
        Optional<SequenceEntity> savedEntityOpt = sequenceRepository.findById("111111");
        assertTrue("Saved entity should exist", savedEntityOpt.isPresent());

        SequenceEntity savedEntity = savedEntityOpt.get();
        assertEquals("pattern", savedEntity.getPattern());
        assertNotNull(savedEntity.getStornoSeq());
        assertEquals("stornoSeq1", savedEntity.getStornoSeq().getName());
    }

    @Test
    public void testCreateWithoutStornoSeq() throws Exception {
        // Create DTO without stornoSeqName
        SequenceDto dto = new SequenceDto(null, "pattern", 1, "last", 2, null);

        // Call create method
        String generatedName = sequenceFacade.create(dto);

        // Verify saved entity
        Optional<SequenceEntity> savedEntityOpt = sequenceRepository.findById(generatedName);
        assertTrue(savedEntityOpt.isPresent());

        SequenceEntity savedEntity = savedEntityOpt.get();
        assertEquals("pattern", savedEntity.getPattern());
        assertNull(savedEntity.getStornoSeq());
    }
}