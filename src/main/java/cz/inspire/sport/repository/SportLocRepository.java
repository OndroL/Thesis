package cz.inspire.sport.repository;

import cz.inspire.sport.entity.SportLocEntity;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;

@Repository
public interface SportLocRepository extends CrudRepository<SportLocEntity, String> {
}
