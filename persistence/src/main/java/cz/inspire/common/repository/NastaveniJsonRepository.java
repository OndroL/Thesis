package cz.inspire.common.repository;

import cz.inspire.common.entity.NastaveniJsonEntity;
import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Repository;

@Repository
public interface NastaveniJsonRepository extends BaseRepository<NastaveniJsonEntity, String> {
}
