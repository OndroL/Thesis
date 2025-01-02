package cz.inspire.thesis.data.repository.sport.sport;

import cz.inspire.thesis.data.model.sport.sport.SportLocEntity;
import org.apache.deltaspike.data.api.*;

@Repository
public interface SportLocRepository extends EntityRepository<SportLocEntity, String> {
}
