package cz.inspire.thesis.data.repository;

import cz.inspire.thesis.data.model.MenaBean;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

@Repository
public interface MenaRepository extends EntityRepository<MenaBean,String> {
    List<MenaBean> findByCode(String code);

    List<MenaBean> findByCodeNum(int codeNum);

    List<MenaBean> findAll();
}
