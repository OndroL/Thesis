package cz.inspire.thesis.data;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnit;


@ApplicationScoped
public class EntityManagerProducer {

    @PersistenceUnit(unitName = "default")
    private EntityManagerFactory emf;

    @Produces
    public EntityManager createEntityManager() {
        if (emf == null) {
            throw new IllegalStateException("EntityManagerFactory is not initialized!");
        }
        return emf.createEntityManager();
    }

    public void close(@Disposes EntityManager em) {
        if (em.isOpen()) {
            em.close();
        }
    }

    // Setter for testing
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }
}