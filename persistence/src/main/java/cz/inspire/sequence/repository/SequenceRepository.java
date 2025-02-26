package cz.inspire.sequence.repository;

import cz.inspire.sequence.entity.SequenceEntity;
import jakarta.data.Limit;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;

/**
 * TODO : Test all Queries from SequenceBean
 */
@Repository
public interface SequenceRepository extends CrudRepository<SequenceEntity,String> {
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
        WHERE s.type = ?1
        ORDER BY s.name
        """)
    List<SequenceEntity> findByType(int type, Limit limit);
}

