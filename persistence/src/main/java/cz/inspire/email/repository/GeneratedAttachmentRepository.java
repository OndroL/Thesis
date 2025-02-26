package cz.inspire.email.repository;

import cz.inspire.email.entity.GeneratedAttachmentEntity;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;

@Repository
public interface GeneratedAttachmentRepository extends CrudRepository<GeneratedAttachmentEntity,String> {
    @Query("SELECT p FROM GeneratedAttachmentEntity p WHERE (p.emailHistory.id = ?1) AND (email = ?2)")
    List<GeneratedAttachmentEntity> findByEmailAndHistory(String historyId, String email);

    @Query("SELECT p FROM GeneratedAttachmentEntity p WHERE p.emailHistory.id = ?1")
    List<GeneratedAttachmentEntity> findByHistory(String historyId);

    @Query("SELECT p FROM GeneratedAttachmentEntity p WHERE (p.emailHistory.id = ?1) AND (p.email = ?2) AND (p.printTemplate.id = ?3)")
    List<GeneratedAttachmentEntity> findByEmailAndHistoryAndTemplate(String historyId, String email, String templateId);
}
