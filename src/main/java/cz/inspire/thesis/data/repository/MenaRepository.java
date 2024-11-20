package cz.inspire.thesis.data.repository;

import cz.inspire.thesis.data.model.Mena;
import jakarta.enterprise.context.Dependent;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

@Repository
@Dependent
public interface MenaRepository extends EntityRepository<Mena,String> {
    @Query("SELECT p FROM Mena p WHERE p.kod = ?1")
    List<Mena> findByCode(String code);

    @Query("SELECT p FROM Mena p WHERE p.kodnum = ?1")
    List<Mena> findByCodeNum(int codeNum);

    List<Mena> findAll();
}
