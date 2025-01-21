package cz.inspire.common.repository;

import cz.inspire.common.entity.MenaEntity;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;
import java.util.List;

@Repository
public interface MenaRepository extends CrudRepository<MenaEntity,String> {
    @Query("SELECT p FROM MenaEntity p WHERE p.kod = ?1")
    List<MenaEntity> findByCode(String code);

    @Query("SELECT p FROM MenaEntity p WHERE p.kodnum = ?1")
    List<MenaEntity> findByCodeNum(int codeNum);
}
