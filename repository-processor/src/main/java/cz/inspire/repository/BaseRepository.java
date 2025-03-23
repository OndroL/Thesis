package cz.inspire.repository;

import java.io.Serializable;
import java.util.List;

public interface BaseRepository<E, PK extends Serializable> {
    E create(E entity);

    List<E> createAll(List<E> entities);

    E update(E entity);

    void delete(E entity);

    void deleteAll(List<E> entities);

    void deleteByPrimaryKey(PK id);

    E findByPrimaryKey(PK id);

    List<E> findAll();
}
