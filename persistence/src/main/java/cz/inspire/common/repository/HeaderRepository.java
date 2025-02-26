package cz.inspire.common.repository;

import cz.inspire.common.entity.HeaderEntity;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;
import java.util.List;

@Repository 
public interface HeaderRepository extends CrudRepository<HeaderEntity, String> {

    @Query("SELECT p FROM HeaderEntity p WHERE (p.location >= 0) ORDER BY p.location")
    List<HeaderEntity> findValidAttributes();

}
