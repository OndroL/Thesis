package cz.inspire.thesis.data.repository;

import cz.inspire.thesis.data.model.HeaderEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

@Repository
public interface HeaderRepository extends EntityRepository<HeaderEntity, String> {

    @Query("SELECT h FROM HeaderEntity h WHERE h.location >= 0 ORDER BY h.location")
    List<HeaderEntity> findValidAtributes();

}
