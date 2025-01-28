package cz.inspire.common.repository;

import cz.inspire.common.entity.NastaveniJsonEntity;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;

@Repository
public interface NastaveniJsonRepository extends CrudRepository<NastaveniJsonEntity, String> {
}
