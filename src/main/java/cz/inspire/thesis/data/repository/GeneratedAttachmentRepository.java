package cz.inspire.thesis.data.repository;

import cz.inspire.thesis.data.model.GeneratedAttachmentEntity;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import java.util.List;

@Repository
public interface GeneratedAttachmentRepository extends EntityRepository<GeneratedAttachmentEntity,String> {
    @Query("SELECT p FROM GeneratedAttachmentEntity p WHERE (p.emailHistory.id = ?1) AND (email = ?2)")
    List<GeneratedAttachmentEntity> findByEmailAndHistory(String historyId, String email);

    @Query("SELECT p FROM GeneratedAttachmentEntity p WHERE p.emailHistory.id = ?1")
    List<GeneratedAttachmentEntity> findByHistory(String historyId);

    @Query("SELECT p FROM GeneratedAttachmentEntity p WHERE (p.emailHistory.id = ?1) AND (p.email = ?2) AND (p.printTemplate.id = ?3)")
    List<GeneratedAttachmentEntity> findByEmailAndHistoryAndTemplate(String historyId, String email, String templateId);


}