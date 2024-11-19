package cz.inspire.thesis.data.repository;

import cz.inspire.thesis.data.model.NastaveniJson;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

@Repository
public interface NastaveniJsonRepository extends EntityRepository<NastaveniJson, String> {
}
