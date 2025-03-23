package cz.inspire.sequence.repository;

import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Limit;
import cz.inspire.repository.annotations.Offset;
import cz.inspire.sequence.entity.SequenceEntity;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.repository.annotations.Query;

import java.util.List;

/**
 * TODO : Test all Queries from SequenceBean
 */
@Repository
public interface SequenceRepository extends BaseRepository<SequenceEntity,String> {
    @Query("""
        SELECT s FROM SequenceEntity s
        ORDER BY s.name
        """)
    List<SequenceEntity> findAllOrdered();

//    @Query("""
//        SELECT s FROM SequenceEntity s
//        WHERE s.name IN (
//            SELECT ss.sekvence FROM SkladSequenceEntity ss
//            WHERE ss.sklad = ?1 AND ss.type = ?2
//        )
//        """)
//    Optional<SequenceEntity> findBySkladType(String skladId, int type);
//
//    @Query("""
//        SELECT s FROM SequenceEntity s
//        WHERE s.name IN (
//            SELECT ps.sekvence FROM PokladnaUcetSequenceEntity ps
//            WHERE ps.pokladna = ?1 AND ps.type = ?2
//        )
//        """)
//    Optional<SequenceEntity> findByPokladnaType(String pokladnaId, int type);

    @Query("""
        SELECT s FROM SequenceEntity s
        WHERE s.type = :type
        ORDER BY s.name
        """)
    List<SequenceEntity> findByType(int type, @Limit int count, @Offset int offset);
}

