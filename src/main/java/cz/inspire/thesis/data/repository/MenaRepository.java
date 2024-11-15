package cz.inspire.thesis.data.repository;

import cz.inspire.thesis.data.model.MenaBean;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

@Repository
public interface MenaRepository extends EntityRepository<MenaBean,String> {
    @Query("select p from mena where p.kod = ?1")
    List<MenaBean> findByCode(String code);

    @Query("select p from mena where p.kodnum = ?1")
    List<MenaBean> findByCodeNum(int codeNum);

    List<MenaBean> findAll();
}
