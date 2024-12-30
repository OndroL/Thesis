package cz.inspire.thesis.data.repository.uzivatel;

import cz.inspire.thesis.data.model.uzivatel.SkupinaEntity;
import org.apache.deltaspike.data.api.*;

import java.util.List;

@Repository
public interface SkupinaRepository extends EntityRepository<SkupinaEntity, String> {
    @Query("""
            SELECT a FROM SkupinaEntity a
            ORDER BY a.nazev
            """)
    List<SkupinaEntity> findAll();

    @Query("""
            SELECT a FROM SkupinaEntity a
            ORDER BY a.nazev
            """)
    List<SkupinaEntity> findAll(@FirstResult int offset, @MaxResults int count);
}
