package cz.inspire.utils.repository;

import cz.inspire.sport.entity.InstructorEntity;
import cz.inspire.utils.repository.annotations.Limit;
import cz.inspire.utils.repository.annotations.Offset;
import cz.inspire.utils.repository.annotations.Query;
import cz.inspire.utils.repository.annotations.QueryParam;
import java.util.List;

public interface InstructorTestRepository extends BaseRepository {
    @Query("SELECT i FROM InstructorEntity i WHERE i.deleted = :deleted ORDER BY i.index ASC")
    List<InstructorEntity> findAllByDeleted(@QueryParam("deleted") boolean deleted,
                                            @Offset int offset,
                                            @Limit int limit);

    @Query("SELECT i FROM InstructorEntity i JOIN i.activities a WHERE a.id = :activityId AND i.deleted = :deleted ORDER BY i.index ASC")
    List<InstructorEntity> findAllByActivity(@QueryParam("activityId") String activityId,
                                             @QueryParam("deleted") boolean deleted,
                                             @Offset int offset,
                                             @Limit int limit);
}
