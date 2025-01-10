package cz.inspire.license.repository;

import cz.inspire.license.entity.LicenseEntity;
import org.apache.deltaspike.data.api.*;

import java.util.List;

@Repository
public interface LicenseRepository extends EntityRepository<LicenseEntity, String> {
    @Query("""
            SELECT l FROM LicenseEntity l
            ORDER BY l.createdDate
            """)
    List<LicenseEntity> findAll();
}
