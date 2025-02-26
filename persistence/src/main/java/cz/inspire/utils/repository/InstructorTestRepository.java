package cz.inspire.utils.repository;

import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.MyRepository;
import cz.inspire.repository.annotations.Offset;
import cz.inspire.repository.annotations.Query;
import cz.inspire.repository.annotations.QueryParam;
import cz.inspire.sport.entity.InstructorEntity;

import java.util.List;

@MyRepository
public interface InstructorTestRepository extends BaseRepository<InstructorEntity, String> {
    @Query("SELECT i FROM InstructorEntity i WHERE i.deleted = :deleted ORDER BY i.index ASC")
    List<InstructorEntity> findAllByDeleted(@QueryParam("deleted") boolean deleted,
                                            @Offset int offset,
                                            int limit);

    @Query("SELECT i FROM InstructorEntity i JOIN i.activities a WHERE a.id = :activityId AND i.deleted = :deleted ORDER BY i.index ASC")
    List<InstructorEntity> findAllByActivity(@QueryParam("activityId") String activityId,
                                             @QueryParam("deleted") boolean deleted,
                                             @Offset int offset,
                                             int limit);
}
