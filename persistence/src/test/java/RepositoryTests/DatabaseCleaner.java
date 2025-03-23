package RepositoryTests;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class DatabaseCleaner {

    @Inject
    EntityManager entityManager;

    /**
     * Clears all records from the given entity's table using TRUNCATE or DELETE CASCADE.
     */
    @Transactional
    public void clearTable(Class<?> entityClass, boolean useTruncate) {
        String tableName = getTableName(entityClass).orElseThrow(() ->
                new IllegalArgumentException("Cannot determine table name for entity: " + entityClass.getSimpleName()));

        if (useTruncate) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName + " CASCADE").executeUpdate();
        } else {
            entityManager.createNativeQuery("DELETE FROM " + tableName + " CASCADE").executeUpdate();
        }
    }

    /**
     * Gets the table name of an entity by reflection.
     */
    private Optional<String> getTableName(Class<?> entityClass) {
        if (entityClass.isAnnotationPresent(jakarta.persistence.Table.class)) {
            return Optional.of(entityClass.getAnnotation(jakarta.persistence.Table.class).name());
        }
        return Optional.of(entityClass.getSimpleName()); // Default to class name if @Table is missing
    }
    /**
    * Clears Hibernate cache and refresh entity from DB.
    */
    public void clearHibernateCacheAndRefresh(Object entity) {
        entityManager.flush();
        entityManager.refresh(entity);
    }
}
