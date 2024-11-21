package cz.inspire.thesis.data.repository;

import cz.inspire.thesis.data.model.NastaveniEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

@Repository
public interface  NastaveniRepository extends EntityRepository<NastaveniEntity, String> {
}
