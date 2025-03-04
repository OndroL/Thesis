package cz.inspire.common.repository;

import cz.inspire.common.entity.MenaEntity;
import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.repository.annotations.Query;

import java.util.List;

@Repository
public interface MenaRepository extends BaseRepository<MenaEntity,String> {
    @Query("SELECT p FROM MenaEntity p WHERE p.kod = :code")
    List<MenaEntity> findByCode(String code);

    @Query("SELECT p FROM MenaEntity p WHERE p.kodNum = :codeNum")
    List<MenaEntity> findByCodeNum(int codeNum);
}
