package cz.inspire.template.repository;

import cz.inspire.repository.BaseRepository;
import cz.inspire.template.entity.PrintTemplateEntity;
import cz.inspire.repository.annotations.Repository;

@Repository
public interface  PrintTemplateRepository extends BaseRepository<PrintTemplateEntity, String> {
}
