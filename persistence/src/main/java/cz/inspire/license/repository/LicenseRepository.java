package cz.inspire.license.repository;

import cz.inspire.license.entity.LicenseEntity;
import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.repository.annotations.Query;

import java.util.List;

@Repository
public interface LicenseRepository extends BaseRepository<LicenseEntity, String> {
    @Query("""
            SELECT l FROM LicenseEntity l
            ORDER BY l.createdDate
            """)
    List<LicenseEntity> findAllOrdered();
}
