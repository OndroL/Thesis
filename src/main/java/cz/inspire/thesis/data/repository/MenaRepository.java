package cz.inspire.thesis.data.repository;

import cz.inspire.thesis.data.model.Mena;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

@Repository
public interface MenaRepository extends EntityRepository<Mena,String> {
    @Query("select p from mena where p.kod = ?1")
    List<Mena> findByCode(String code);

    @Query("select p from mena where p.kodnum = ?1")
    List<Mena> findByCodeNum(int codeNum);

    List<Mena> findAll();
}
