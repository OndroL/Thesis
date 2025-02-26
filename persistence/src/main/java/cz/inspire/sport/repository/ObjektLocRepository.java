package cz.inspire.sport.repository;

import cz.inspire.sport.entity.ObjektLocEntity;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;

@Repository
public interface ObjektLocRepository extends CrudRepository<ObjektLocEntity, String> {
}
