package cz.inspire.thesis.data.repository;

import cz.inspire.thesis.data.model.SequenceEntity;
import cz.inspire.thesis.data.model.SkladSequenceEntity;
import org.apache.deltaspike.data.api.*;

import java.util.List;
import java.util.Optional;

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
    List<SkladSequenceEntity> findBySkladType(String skladId, int type);

    @Query("SELECT p FROM SequenceEntity WHERE p.type = ?1")
    List<SequenceEntity> findByType(int type, @FirstResult int offset,@MaxResults int limit);

    @Query("SELECT p FROM SequenceEntity p ORDER BY p.name")
    List<SequenceEntity> findAll();


    /**
     *Not exactly sure how StornoSequence exactly works and where it is stored.
     */
    @Query("SELECT p FROM SkladSequenceEntity p WHERE p.sequence = ?1")
    Optional<SkladSequenceEntity>findById(String name);

}
