package cz.inspire.thesis.data.repository;

import cz.inspire.thesis.data.model.PrintTemplateEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.Optional;

@Repository
public interface  PrintTemplateRepository extends EntityRepository<PrintTemplateEntity, String> {
    @Query
    Optional<PrintTemplateEntity> findById(String templateId);
}