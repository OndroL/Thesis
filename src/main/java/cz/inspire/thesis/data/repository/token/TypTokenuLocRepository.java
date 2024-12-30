package cz.inspire.thesis.data.repository.token;

import cz.inspire.thesis.data.model.token.TypTokenuEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

@Repository
public interface TypTokenuLocRepository extends EntityRepository<TypTokenuEntity, String> {
}
