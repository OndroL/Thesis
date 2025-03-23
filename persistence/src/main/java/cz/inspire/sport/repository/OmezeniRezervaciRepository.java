package cz.inspire.sport.repository;

import cz.inspire.repository.BaseRepository;
import cz.inspire.sport.entity.OmezeniRezervaciEntity;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.repository.annotations.Query;

import java.util.List;


@Repository
public interface OmezeniRezervaciRepository extends BaseRepository<OmezeniRezervaciEntity, String> {
    @Query("SELECT o FROM OmezeniRezervaciEntity o ORDER BY o.objektId")
    List<OmezeniRezervaciEntity> findAllOrdered();
}
