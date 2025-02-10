package cz.inspire.sport.repository;

import cz.inspire.sport.entity.OmezeniRezervaciEntity;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;


@Repository
public interface OmezeniRezervaciRepository extends CrudRepository<OmezeniRezervaciEntity, String> {
}
