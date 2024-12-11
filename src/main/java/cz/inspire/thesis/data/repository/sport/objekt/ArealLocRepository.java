package cz.inspire.thesis.data.repository.sport.objekt;

import cz.inspire.thesis.data.model.sport.objekt.ArealLocEntity;
import org.apache.deltaspike.data.api.*;

import java.util.List;

@Repository
public interface ArealLocRepository extends EntityRepository<ArealLocEntity, String> {
}
