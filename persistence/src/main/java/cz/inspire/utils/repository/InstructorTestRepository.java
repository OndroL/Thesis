package cz.inspire.utils.repository;

import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Limit;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.repository.annotations.Offset;
import cz.inspire.repository.annotations.Query;
import cz.inspire.sport.entity.InstructorEntity;

import java.util.List;

@Repository
public interface InstructorTestRepository extends BaseRepository<InstructorEntity, String> {
    @Query("SELECT i FROM InstructorEntity i WHERE i.deleted = :deleted ORDER BY i.index ASC")
    List<InstructorEntity> findAllByDeleted(boolean deleted, @Offset int offset, @Limit int limit);

    @Query("""
    SELECT i FROM InstructorEntity i
    JOIN i.activities a
    WHERE a.id = :activityId
     AND i.deleted = :deleted
    ORDER BY i.index ASC
    """)
    List<InstructorEntity> findAllByActivity(String activityId, boolean deleted, @Offset int offset, @Limit int limit);

    @Query("""
    SELECT i FROM InstructorEntity i
    ORDER BY i.index
    """)
    List<InstructorEntity> findAll();
}
