package cz.inspire.thesis.data.repository;

import cz.inspire.thesis.data.model.Header;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

@Repository
public interface HeaderRepository extends EntityRepository<Header, String> {

    @Query("SELECT h FROM Header h WHERE h.location >= 0 ORDER BY h.location")
    List<Header> findValidAtributes();

}
