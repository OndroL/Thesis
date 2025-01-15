package cz.inspire.sequence.repository;

import cz.inspire.sequence.entity.SequenceEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;
import org.apache.deltaspike.data.api.FirstResult;
import org.apache.deltaspike.data.api.MaxResults;

import java.util.List;
import java.util.Optional;

/**
 * TODO : Test all Queries from SequenceBean
 */
@Repository
public interface SequenceRepository extends EntityRepository<SequenceEntity,String> {
    @Query("""
        SELECT s FROM SequenceEntity s
        ORDER BY s.name
        """)
    List<SequenceEntity> findAll();

    @Query("""
        SELECT s FROM SequenceEntity s
        WHERE s.name IN (
            SELECT ss.sekvence FROM SkladSequenceEntity ss
            WHERE ss.sklad = ?1 AND ss.type = ?2
        )
        """)
    Optional<SequenceEntity> findBySkladType(String skladId, int type);

    @Query("""
        SELECT s FROM SequenceEntity s
        WHERE s.name IN (
            SELECT ps.sekvence FROM PokladnaUcetSequenceEntity ps
            WHERE ps.pokladna = ?1 AND ps.type = ?2
        )
        """)
    Optional<SequenceEntity> findByPokladnaType(String pokladnaId, int type);

    @Query("""
        SELECT s FROM SequenceEntity s
        WHERE s.type = ?1
        ORDER BY s.name
        """)
    List<SequenceEntity> findByType(int type, @FirstResult int offset, @MaxResults int limit);

    /**
     * This query is here only for test purposes of stornoSeq relationship
     */
    @Query("""
        SELECT s FROM SequenceEntity s
        WHERE s.name = ?1
        """)
    Optional<SequenceEntity> findById(String id);
}

