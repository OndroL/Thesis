package FacadeTests.Sequence;

import cz.inspire.EntityManagerProducer;
import cz.inspire.sequence.dto.SequenceDto;
import cz.inspire.sequence.entity.SequenceEntity;
import cz.inspire.sequence.facade.SequenceFacade;
import cz.inspire.sequence.repository.SequenceRepository;
import cz.inspire.sequence.service.SequenceService;
import jakarta.ejb.CreateException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TransactionalBehaviorTest {

    private SequenceService sequenceService;
    private SequenceFacade sequenceFacade;
    private SequenceRepository sequenceRepository;

    @Before
    public void setUp() {
        // Boot the CDI container
        CdiContainer container = CdiContainerLoader.getCdiContainer();
        container.boot();

        // Create EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

        // Initialize EntityManagerProducer
        EntityManagerProducer producer = BeanProvider.getContextualReference(EntityManagerProducer.class);
        producer.setEntityManagerFactory(emf);

        // Access beans via CDI
        sequenceService = BeanProvider.getContextualReference(SequenceService.class);
        sequenceFacade = BeanProvider.getContextualReference(SequenceFacade.class);
        sequenceRepository = BeanProvider.getContextualReference(SequenceRepository.class);

        // Clear database
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM SequenceEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    public void testServiceTransaction() throws CreateException {
        // Create a new SequenceEntity
        SequenceEntity entity = new SequenceEntity("testService", "pattern", 1, "last", 1, null);

        // Call the create method in the service
        sequenceService.create(entity);

        // Verify that the entity is persisted
        Optional<SequenceEntity> retrievedEntity = sequenceRepository.findById("testService");
        assertTrue("Entity should be persisted by service", retrievedEntity.isPresent());
    }

    @Test
    public void testFacadeTransaction() throws Exception {
        // Create a new SequenceDto and call the facade
        SequenceEntity stornoEntity = new SequenceEntity("stornoSeq", "pattern", 1, "last", 1, null);
        sequenceService.create(stornoEntity);

        SequenceDto dto = new SequenceDto("testFacade", "pattern", 1, "last", 2, "stornoSeq");
        sequenceFacade.create(dto);

        // Verify that the entity is persisted
        Optional<SequenceEntity> retrievedEntity = sequenceRepository.findById("testFacade");
        assertTrue("Entity should be persisted by facade", retrievedEntity.isPresent());

        // Verify the relationship
        SequenceEntity savedEntity = retrievedEntity.get();
        assertTrue("StornoSeq relationship should be established",
                savedEntity.getStornoSeq() != null && "stornoSeq".equals(savedEntity.getStornoSeq().getName()));
    }

    @Test
    public void testTransactionRollback() {
        try {
            // Simulate an error in the service
            SequenceEntity entity = new SequenceEntity(null, "pattern", 1, "last", 1, null);
            sequenceService.create(entity); // Should throw an exception
        } catch (Exception e) {
            // Expected exception
        }

        // Verify that the entity is not persisted due to rollback
        Optional<SequenceEntity> retrievedEntity = sequenceRepository.findById(null);
        assertFalse("Entity should not be persisted due to rollback", retrievedEntity.isPresent());
    }
}
