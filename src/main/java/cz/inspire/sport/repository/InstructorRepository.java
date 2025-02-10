package cz.inspire.sport.repository;

import cz.inspire.sport.entity.InstructorEntity;
import jakarta.data.Limit;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;

@Repository
public interface InstructorRepository extends CrudRepository<InstructorEntity, String> {

    @Query("SELECT i FROM InstructorEntity i ORDER BY i.index ASC")
    List<InstructorEntity> findAllOrdered();

    @Query("SELECT i FROM InstructorEntity i WHERE i.deleted = ?1 ORDER BY i.index ASC")
    List<InstructorEntity> findAll(Limit limit, boolean deleted);

    @Query("""
        SELECT i FROM InstructorEntity i
        JOIN i.activities a
        WHERE a.id = ?1 AND i.deleted = ?2
        ORDER BY i.index ASC
    """)
    List<InstructorEntity> findAllByActivity(String activityId, Limit limit, boolean deleted);

    @Query("SELECT COUNT(i.id) FROM InstructorEntity i WHERE i.deleted = ?1")
    Long countInstructors(boolean deleted);

    @Query("""
        SELECT COUNT(i.id) FROM InstructorEntity i
        JOIN i.activities a
        WHERE a.id = ?1 AND i.deleted = ?2
    """)
    Long countInstructorsByActivity(String activityId, boolean deleted);
}

