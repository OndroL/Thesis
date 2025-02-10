package cz.inspire.email.repository;

import cz.inspire.email.entity.EmailHistoryEntity;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;
import jakarta.data.Limit;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmailHistoryRepository extends CrudRepository<EmailHistoryEntity,String> {
    @Query("SELECT p FROM EmailHistoryEntity p ORDER BY p.date DESC")
    List<EmailHistoryEntity> findAllOrdered(); // Renamed because of conflict with BaseRepository Stream findAll() func.

    @Query("SELECT p FROM EmailHistoryEntity p ORDER BY p.date DESC")
    List<EmailHistoryEntity> findAll(Limit limit);

    @Query("SELECT p FROM EmailHistoryEntity p WHERE (p.date >= ?1) AND (p.date <= ?2) AND (p.sent = true) ORDER BY p.date DESC")
    List<EmailHistoryEntity> findByDate(Timestamp dateFrom, Timestamp dateTo, Limit limit);

    @Query("SELECT p FROM EmailHistoryEntity p WHERE p.id = ?1")
    Optional<EmailHistoryEntity> findById(String emailHistoryId);
}
