package cz.inspire.email.repository;

import cz.inspire.email.entity.GeneratedAttachmentEntity;
import cz.inspire.repository.BaseRepository;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.repository.annotations.Query;

import java.util.List;

@Repository
public interface GeneratedAttachmentRepository extends BaseRepository<GeneratedAttachmentEntity,String> {
    @Query("SELECT p FROM GeneratedAttachmentEntity p WHERE (p.emailHistory.id = :historyId) AND (email = :email)")
    List<GeneratedAttachmentEntity> findByEmailAndHistory(String historyId, String email);

    @Query("SELECT p FROM GeneratedAttachmentEntity p WHERE p.emailHistory.id = :historyId")
    List<GeneratedAttachmentEntity> findByHistory(String historyId);

    @Query("SELECT p FROM GeneratedAttachmentEntity p WHERE (p.emailHistory.id = :historyId) AND (p.email = :email) AND (p.printTemplate.id = :templateId)")
    List<GeneratedAttachmentEntity> findByEmailAndHistoryAndTemplate(String historyId, String email, String templateId);
}
