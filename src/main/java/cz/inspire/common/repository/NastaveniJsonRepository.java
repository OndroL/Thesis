package cz.inspire.common.repository;

import cz.inspire.common.entity.NastaveniJsonEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

@Repository
public interface NastaveniJsonRepository extends EntityRepository<NastaveniJsonEntity, String> {
}
