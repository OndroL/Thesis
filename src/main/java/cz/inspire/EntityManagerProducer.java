package cz.inspire;

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
    @ApplicationScoped
    public EntityManager createEntityManager() {
        if (emf == null) {
            throw new IllegalStateException("EntityManagerFactory is not initialized!");
        }
        return emf.createEntityManager();
    }

    public void close(@Disposes EntityManager em) {
        if (em.isOpen()) {
            try {
                em.close();
            } catch (Exception e) {
                System.err.println("Error while closing EntityManager: " + e.getMessage());
            }
        }
    }

    // Setter for testing
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }
}