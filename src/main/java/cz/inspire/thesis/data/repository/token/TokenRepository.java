package cz.inspire.thesis.data.repository.token;

import cz.inspire.thesis.data.model.token.TokenEntity;
import org.apache.deltaspike.data.api.*;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends EntityRepository<TokenEntity, String> {

    List<TokenEntity> findAll();

    @Query("""
            SELECT a FROM TokenEntity a
            ORDER BY a.popis, a.id
            """)
    List<TokenEntity> findAll(@FirstResult int offset, @MaxResults int count);


    /// This query need Join on TypTokenuEntity to perform this operation with typId
    /// Which is a small difference with old Bean
    @Query("""
            SELECT a FROM TokenEntity a
            Join a.typTokenu t
            WHERE t.id = ?1
            ORDER BY a.popis, a.id
            """)
    List<TokenEntity> findByTyp(String typId, @FirstResult int offset, @MaxResults int count);


    /// This query is waiting for ZakaznikTokenEntity to be implemented
    @Query("""
            SELECT a FROM TokenEntity a
            JOIN ZakaznikTokenEntity zk ON a.id = zk.token
            WHERE zk.casdo IS NULL AND zk.zakaznik = ?1
            ORDER BY a.popis, a.id
            """)
    List<TokenEntity> findByZakaznik(String zakaznikId, @FirstResult int offset, @MaxResults int count);

    @Query("""
            SELECT a FROM TokenEntity a
            WHERE a.id LIKE ?1
            """)
    Optional<TokenEntity> findBySuffix(String suffix);
}
