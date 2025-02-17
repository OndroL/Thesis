package cz.inspire.sport.repository;

import cz.inspire.sport.entity.OmezeniRezervaciEntity;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;


@Repository
public interface OmezeniRezervaciRepository extends CrudRepository<OmezeniRezervaciEntity, String> {
    @Query("SELECT o FROM OmezeniRezervaciEntity o ORDER BY o.objektId")
    List<OmezeniRezervaciEntity> findAllOrdered();
}
