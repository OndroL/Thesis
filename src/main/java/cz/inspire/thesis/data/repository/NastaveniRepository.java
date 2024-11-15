package cz.inspire.thesis.data.repository;

import cz.inspire.thesis.data.model.NastaveniBean;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

@Repository
public interface  NastaveniRepository extends EntityRepository<NastaveniBean, String> {

}
