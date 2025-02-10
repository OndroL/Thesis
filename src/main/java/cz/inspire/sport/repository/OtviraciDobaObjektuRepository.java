package cz.inspire.sport.repository;

import cz.inspire.sport.entity.OtviraciDobaObjektuEntity;
import cz.inspire.sport.entity.OtviraciDobaObjektuPK;
import jakarta.data.Limit;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

//@Repository
//public interface OtviraciDobaObjektuRepository extends CrudRepository<OtviraciDobaObjektuEntity, OtviraciDobaObjektuPK> {
//    @Query("""
//        SELECT o FROM OtviraciDobaObjektuEntity o
//        WHERE o.id.objektId = :objektId
//        ORDER BY o.id.platnostOd DESC
//    """)
//    List<OtviraciDobaObjektuEntity> findByObjekt(String objektId);
//
//    @Query("""
//        SELECT o FROM OtviraciDobaObjektuEntity o
//        WHERE o.id.objektId = :objektId
//        ORDER BY o.id.platnostOd DESC
//    """)
//    List<OtviraciDobaObjektuEntity> findByObjekt(String objektId, Limit limit);
//
//    @Query("""
//        SELECT o FROM OtviraciDobaObjektuEntity o
//        WHERE o.id.objektId = :objektId AND o.id.platnostOd <= :day
//        ORDER BY o.id.platnostOd DESC
//    """)
//    OtviraciDobaObjektuEntity findCurrent(String objektId, Timestamp day);
//
//    @Query("""
//        SELECT o FROM OtviraciDobaObjektuEntity o
//        WHERE o.id.objektId = :objektId AND o.id.platnostOd >= :day
//        ORDER BY o.id.platnostOd DESC
//    """)
//    List<OtviraciDobaObjektuEntity> findAfter(String objektId, Timestamp day);
//
//    @Query("""
//        SELECT o.id.platnostOd FROM OtviraciDobaObjektuEntity o
//        WHERE o.id.objektId = :objektId AND o.id.platnostOd <= :day
//        ORDER BY o.id.platnostOd DESC
//    """)
//    List<Date> getCurrentIdsByObjectAndDay(String objektId, Timestamp day);
//
//
//    @Query("""
//        SELECT o FROM OtviraciDobaObjektuEntity o
//        WHERE o.id = :id
//    """)
//    Optional<OtviraciDobaObjektuEntity> findById(OtviraciDobaObjektuPK id);
//}
