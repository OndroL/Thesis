package cz.inspire.common.repository;

import cz.inspire.common.entity.NastaveniEntity;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;

@Repository
public interface NastaveniRepository extends CrudRepository<NastaveniEntity, String> {
}
