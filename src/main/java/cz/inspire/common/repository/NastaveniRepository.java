package cz.inspire.common.repository;

import cz.inspire.common.entity.NastaveniEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

@Repository
public interface  NastaveniRepository extends EntityRepository<NastaveniEntity, String> {
}
