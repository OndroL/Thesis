package cz.inspire.thesis.data.repository;


import cz.inspire.thesis.data.model.Header;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

@Repository
public interface HeaderRepository extends EntityRepository<Header, String> {
    @Query("select * from noteheader where location >= 0 order by location;")
    List<Header> findValidAtributes();
}
