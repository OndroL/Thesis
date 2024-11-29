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
    @Query("SELECT s FROM SequenceEntity s ORDER BY s.name")
    List<SequenceEntity> findAll();
    @Query("SELECT s FROM SequenceEntity s WHERE s.type = ?1 ORDER BY s.name")
    List<SequenceEntity> findByType(int type);

    @Query("SELECT s FROM SequenceEntity s WHERE s.name IN " +
            "(SELECT ss.sekvence FROM SkladSequenceEntity ss WHERE ss.sklad = ?1 AND ss.type = ?2)")
    List<SequenceEntity> findBySkladType(String skladId, int type);

    @Query("SELECT s FROM SequenceEntity s WHERE s.name IN " +
            "(SELECT ps.sekvence FROM PokladnaUcetSequenceEntity ps WHERE ps.pokladna = ?1 AND ps.type = ?2)")
    List<SequenceEntity> findByPokladnaType(String pokladnaId, int type);

    /**
     * This query is here only for test purposes of stornoSeq relationship
     */
    @Query("SELECT s FROM SequenceEntity s WHERE s.name = ?1")
    Optional<SequenceEntity> findById(String id);
}
