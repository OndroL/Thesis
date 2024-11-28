package cz.inspire.thesis.data.repository;

import cz.inspire.thesis.data.model.SequenceEntity;
import org.apache.deltaspike.data.api.*;

import java.util.List;
/**
 * TODO : Add all Queries from SequenceBean
 * Missing finders : findBySkladType, findByPokladnaType,
 */
@Repository
public interface SequenceRepository extends EntityRepository<SequenceEntity,String> {
    /**
     * This query waiting for conversion of Sklad_SEQ to be properly tested and implemented
     */
    @Query("SELECT p FROM SkladSequenceEntity p WHERE (p.sklad = ?1) AND (p.sekvence = ?2)")
    List<SequenceEntity> findBySkladType(String skladId, int type);

    @Query("SELECT p FROM SequenceEntity WHERE p.type = ?1")
    List<SequenceEntity> findByType(int type, @FirstResult int offset,@MaxResults int limit);

    @Query("SELECT p FROM SequenceEntity p ORDER BY p.name")
    List<SequenceEntity> findAll();

}
