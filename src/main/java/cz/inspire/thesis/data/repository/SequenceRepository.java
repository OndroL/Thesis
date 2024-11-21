package cz.inspire.thesis.data.repository;

import cz.inspire.thesis.data.model.SequenceEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

@Repository
public interface SequenceRepository extends EntityRepository<SequenceEntity,String> {
    /**
     * This query waiting for conversion of Sklad_SEQ to be properly tested and implemented
     */
    @Query("SELECT p FROM Sklad_Seq p WHERE (p.sklad = ?1) AND (p.sekvence = ?2)")
    List<SequenceEntity> findBySkladType(String skladId, int type);

    @Query("SELECT p FROM Seq WHERE p.type = ?1 LIMIT ?3 OFFSET ?2")
    List<SequenceEntity> findByType(int type, int offset, int limit);
}
