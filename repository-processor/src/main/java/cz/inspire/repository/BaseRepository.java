package cz.inspire.repository;

import java.io.Serializable;

public interface BaseRepository<E, PK extends Serializable> {
    E create(E entity);

    E update(E entity);

    void delete(E entity);

    E findById(PK id);
}
