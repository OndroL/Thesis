package cz.inspire.thesis.data.repository;

import cz.inspire.thesis.data.model.NastaveniJsonBean;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

@Repository
public interface NastaveniJsonRepository extends EntityRepository<NastaveniJsonBean, String> {
}
