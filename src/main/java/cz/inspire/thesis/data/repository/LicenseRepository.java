package cz.inspire.thesis.data.repository;

import cz.inspire.thesis.data.model.LicenseEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

@Repository
public interface LicenseRepository extends EntityRepository<LicenseEntity, String> {
}
