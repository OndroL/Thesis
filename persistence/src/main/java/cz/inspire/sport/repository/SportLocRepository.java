package cz.inspire.sport.repository;

import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.sport.entity.SportLocEntity;

@Repository
public interface SportLocRepository extends BaseRepository<SportLocEntity, String> {
}
