package cz.inspire.template.repository;

import cz.inspire.template.entity.PrintTemplateEntity;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;

@Repository
public interface  PrintTemplateRepository extends CrudRepository<PrintTemplateEntity, String> {
}
