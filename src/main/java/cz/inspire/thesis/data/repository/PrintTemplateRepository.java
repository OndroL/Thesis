package cz.inspire.thesis.data.repository;

import cz.inspire.thesis.data.model.PrintTemplateEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

@Repository
public interface  PrintTemplateRepository extends EntityRepository<PrintTemplateEntity, String> {
}