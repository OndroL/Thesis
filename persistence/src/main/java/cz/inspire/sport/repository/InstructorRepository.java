package cz.inspire.sport.repository;

import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Offset;
import cz.inspire.sport.entity.InstructorEntity;
import cz.inspire.repository.annotations.Limit;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.repository.annotations.Query;

import java.util.List;

@Repository
public interface InstructorRepository extends BaseRepository<InstructorEntity, String> {

    @Query("SELECT i FROM InstructorEntity i ORDER BY i.index ASC")
    List<InstructorEntity> findAll();

    @Query("SELECT i FROM InstructorEntity i WHERE i.deleted = :deleted ORDER BY i.index ASC")
    List<InstructorEntity> findAll(@Limit int count, @Offset int offset, boolean deleted);

    @Query("""
        SELECT i FROM InstructorEntity i
        JOIN i.activities a
        WHERE a.id = :activityId AND i.deleted = :deleted
        ORDER BY i.index ASC
    """)
    List<InstructorEntity> findAllByActivity(String activityId, @Limit int count, @Offset int offset, boolean deleted);

    @Query("SELECT COUNT(i.id) FROM InstructorEntity i WHERE i.deleted = :deleted")
    Long countInstructors(boolean deleted);

    @Query("""
        SELECT COUNT(i.id) FROM InstructorEntity i
        JOIN i.activities a
        WHERE a.id = :activityId AND i.deleted = :deleted
    """)
    Long countInstructorsByActivity(String activityId, boolean deleted);
}

