package RepositoryTests;

import cz.inspire.thesis.data.EntityManagerProducer;
import cz.inspire.thesis.data.model.SequenceEntity;
import cz.inspire.thesis.data.repository.SequenceRepository;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class SequenceRepositoryTest {

    private SequenceRepository sequenceRepository;

    @Before
    public void setUp() {
        // Boot the CDI container
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();

        // Create the EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

        // Set the EntityManagerFactory for the CDI context
        EntityManagerProducer producer = BeanProvider.getContextualReference(EntityManagerProducer.class);
        producer.setEntityManagerFactory(emf);

        // Access SequenceRepository from CDI
        sequenceRepository = BeanProvider.getContextualReference(SequenceRepository.class);

        // Clear the database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM SequenceEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testSaveAndFind() {
        assertNotNull("SequenceRepository should be initialized!", sequenceRepository);

        // Save a new sequence
        SequenceEntity sequence = new SequenceEntity("seq1", "pattern1", 1, "last1", 1, null);
        sequenceRepository.save(sequence);

        // Retrieve the saved sequence
        List<SequenceEntity> sequences = sequenceRepository.findByType(1);
        assertEquals(1, sequences.size());
        assertEquals("seq1", sequences.get(0).getName());
    }

    @Test
    public void testStornoSeqRelationship() {
        assertNotNull("SequenceRepository should be initialized!", sequenceRepository);

        // Create and save a base SequenceEntity
        SequenceEntity baseSequence = new SequenceEntity();
        baseSequence.setName("base-sequence");
        baseSequence.setPattern("BASE");
        baseSequence.setMinvalue(1);
        baseSequence.setLast("001");
        baseSequence.setType(1);
        sequenceRepository.save(baseSequence);

        // Create and save a SequenceEntity with stornoSeq reference
        SequenceEntity stornoSequence = new SequenceEntity();
        stornoSequence.setName("storno-sequence");
        stornoSequence.setPattern("STORNO");
        stornoSequence.setMinvalue(10);
        stornoSequence.setLast("010");
        stornoSequence.setType(2);
        stornoSequence.setStornoSeq(baseSequence);
        sequenceRepository.save(stornoSequence);

        // Retrieve the saved entity and verify relationships
        Optional<SequenceEntity> retrievedStornoSequence = sequenceRepository.findById("storno-sequence");
        assertTrue("storno-sequence should be found!", retrievedStornoSequence.isPresent());

        SequenceEntity result = retrievedStornoSequence.get();
        assertNotNull("stornoSeq relationship should be initialized!", result.getStornoSeq());
        assertEquals("base-sequence", result.getStornoSeq().getName());
        assertEquals("BASE", result.getStornoSeq().getPattern());
    }

    @Test
    public void testFindAll() {
        assertNotNull("SequenceRepository should be initialized!", sequenceRepository);

        // Insert multiple SequenceEntity records
        SequenceEntity seq1 = new SequenceEntity();
        seq1.setName("A-sequence");
        seq1.setPattern("PATTERN-A");
        seq1.setMinvalue(1);
        seq1.setLast("001");
        seq1.setType(1);
        sequenceRepository.save(seq1);

        SequenceEntity seq2 = new SequenceEntity();
        seq2.setName("B-sequence");
        seq2.setPattern("PATTERN-B");
        seq2.setMinvalue(10);
        seq2.setLast("010");
        seq2.setType(2);
        sequenceRepository.save(seq2);

        SequenceEntity seq3 = new SequenceEntity();
        seq3.setName("C-sequence");
        seq3.setPattern("PATTERN-C");
        seq3.setMinvalue(20);
        seq3.setLast("020");
        seq3.setType(3);
        sequenceRepository.save(seq3);

        // Use the findAll method to retrieve all records
        List<SequenceEntity> allSequences = sequenceRepository.findAll();

        // Assertions
        assertNotNull("findAll should not return null!", allSequences);
        assertEquals("findAll should return 3 records!", 3, allSequences.size());

        // Verify order by name
        assertEquals("A-sequence", allSequences.get(0).getName());
        assertEquals("B-sequence", allSequences.get(1).getName());
        assertEquals("C-sequence", allSequences.get(2).getName());
    }
}
