package cz.inspire.common.repository;

import cz.inspire.common.entity.NastaveniEntity;
import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Repository;

@Repository
public interface NastaveniRepository extends BaseRepository<NastaveniEntity, String> {
}
