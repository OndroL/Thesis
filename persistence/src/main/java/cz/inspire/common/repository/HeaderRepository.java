package cz.inspire.common.repository;

import cz.inspire.common.entity.HeaderEntity;
import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.repository.annotations.Query;

import java.util.List;

@Repository 
public interface HeaderRepository extends BaseRepository<HeaderEntity, String> {

    @Query("SELECT p FROM HeaderEntity p WHERE (p.location >= 0) ORDER BY p.location")
    List<HeaderEntity> findValidAttributes();

}
