package cz.inspire.sport.repository;

import cz.inspire.sport.entity.ArealLocEntity;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;

@Repository
public interface ArealLocRepository extends CrudRepository<ArealLocEntity, String> {
}
