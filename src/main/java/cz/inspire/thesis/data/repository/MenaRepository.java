package cz.inspire.thesis.data.repository;

import cz.inspire.thesis.data.model.MenaEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

@Repository
public interface MenaRepository extends EntityRepository<MenaEntity,String> {
    @Query("SELECT p FROM MenaEntity p WHERE p.kod = ?1")
    List<MenaEntity> findByCode(String code);

    @Query("SELECT p FROM MenaEntity p WHERE p.kodnum = ?1")
    List<MenaEntity> findByCodeNum(int codeNum);

    List<MenaEntity> findAll();
}
