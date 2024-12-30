package cz.inspire.thesis.data.repository.token;

import cz.inspire.thesis.data.model.token.TypTokenuEntity;
import org.apache.deltaspike.data.api.*;

import java.util.List;

@Repository
public interface TypTokenuRepository extends EntityRepository<TypTokenuEntity, String> {
    @Query("""
            SELECT a FROM TypTokenuEntity a
            JOIN a.localeData loc
            WHERE loc.jazyk = ?1
            ORDER BY loc.nazev
            """)
    List<TypTokenuEntity> findAll(String jazyk, @FirstResult int offset, @MaxResults int count);
}
