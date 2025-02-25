package cz.inspire.utils.repository;

import java.io.Serializable;
import java.util.Optional;


public interface BaseRepository<E, PK extends Serializable> {
    E create(E entity);

    E update(E entity);

    void delete(E entity);

    Optional<E> findById(PK id);

    E findByPrimaryKey(PK id);
}
