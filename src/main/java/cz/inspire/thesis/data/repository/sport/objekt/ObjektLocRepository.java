package cz.inspire.thesis.data.repository.sport.objekt;

import cz.inspire.thesis.data.model.sport.objekt.ObjektLocEntity;
import org.apache.deltaspike.data.api.*;

@Repository
public interface ObjektLocRepository extends EntityRepository<ObjektLocEntity, String> {
}
