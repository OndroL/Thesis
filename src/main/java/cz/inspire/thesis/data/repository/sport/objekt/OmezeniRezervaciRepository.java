package cz.inspire.thesis.data.repository.sport.objekt;

import cz.inspire.thesis.data.model.sport.objekt.OmezeniRezervaciEntity;
import org.apache.deltaspike.data.api.*;

import java.util.List;

@Repository
public interface OmezeniRezervaciRepository extends EntityRepository<OmezeniRezervaciEntity, String> {

    @Query("""
        SELECT o FROM OmezeniRezervaciEntity o
    """)
    List<OmezeniRezervaciEntity> findAll();
}
