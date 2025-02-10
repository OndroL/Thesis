package cz.inspire.sport.repository;

import cz.inspire.sport.entity.SportKategorieLocEntity;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;


@Repository
public interface SportKategorieLocRepository extends CrudRepository<SportKategorieLocEntity, String> {
}
