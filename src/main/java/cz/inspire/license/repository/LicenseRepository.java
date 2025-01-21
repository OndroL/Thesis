package cz.inspire.license.repository;

import cz.inspire.license.entity.LicenseEntity;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;

@Repository
public interface LicenseRepository extends CrudRepository<LicenseEntity, String> {
    @Query("""
            SELECT l FROM LicenseEntity l
            ORDER BY l.createdDate
            """)
    List<LicenseEntity> findAllOrdered();
}
