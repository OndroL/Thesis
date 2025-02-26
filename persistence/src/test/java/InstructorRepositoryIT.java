
import cz.inspire.sport.entity.ActivityEntity;
import cz.inspire.sport.entity.InstructorEntity;
import cz.inspire.utils.repository.InstructorTestRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InstructorRepositoryIT {

    @Inject
    InstructorTestRepository instructorRepository;

    @Inject
    EntityManager em;

    @Test
    public void testFindAllByDeleted() {
        // Create two instructors marked as deleted and one not.
        InstructorEntity instructor1 = new InstructorEntity();
        instructor1.setFirstName("John");
        instructor1.setLastName("Doe");
        instructor1.setIndex(1);
        instructor1.setDeleted(true);
        em.persist(instructor1);

        InstructorEntity instructor2 = new InstructorEntity();
        instructor2.setFirstName("Jane");
        instructor2.setLastName("Smith");
        instructor2.setIndex(2);
        instructor2.setDeleted(false);
        em.persist(instructor2);

        InstructorEntity instructor3 = new InstructorEntity();
        instructor3.setFirstName("Mike");
        instructor3.setLastName("Johnson");
        instructor3.setIndex(3);
        instructor3.setDeleted(true);
        em.persist(instructor3);

        em.flush();

        // Query for instructors with deleted = true
        List<InstructorEntity> deletedInstructors = instructorRepository.findAllByDeleted(true, 0, 10);
        assertNotNull(deletedInstructors);
        assertEquals(2, deletedInstructors.size(), "Expecting 2 deleted instructors");

        // Query for instructors with deleted = false
        List<InstructorEntity> nonDeletedInstructors = instructorRepository.findAllByDeleted(false, 0, 10);
        assertNotNull(nonDeletedInstructors);
        assertEquals(1, nonDeletedInstructors.size(), "Expecting 1 non-deleted instructor");
    }

    @Test
    public void testFindAllByActivity() {
        // Create a minimal ActivityEntity. Adjust this as necessary based on your ActivityEntity definition.
        ActivityEntity activity = new ActivityEntity();
        activity.setId("ACT-1");
        // Set other required fields on activity if needed.
        em.persist(activity);

        // Create an instructor and associate it with the activity.
        InstructorEntity instructor = new InstructorEntity();
        instructor.setFirstName("Alice");
        instructor.setLastName("Wonderland");
        instructor.setIndex(1);
        instructor.setDeleted(false);
        Set<ActivityEntity> activities = new HashSet<>();
        activities.add(activity);
        instructor.setActivities(activities);
        em.persist(instructor);

        em.flush();

        // Query for instructors associated with activity "ACT-1" and not deleted.
        List<InstructorEntity> instructors = instructorRepository.findAllByActivity("ACT-1", false, 0, 10);
        assertNotNull(instructors);
        assertEquals(1, instructors.size(), "Expecting 1 instructor associated with activity ACT-1");

        // Verify that the returned instructor indeed has the activity with id "ACT-1"
        InstructorEntity found = instructors.get(0);
        boolean hasActivity = found.getActivities().stream()
                .anyMatch(a -> "ACT-1".equals(a.getId()));
        assertTrue(hasActivity, "Returned instructor should be linked to activity ACT-1");
    }
}
