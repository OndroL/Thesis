package cz.inspire.thesis.data;

import com.oracle.svm.core.annotate.Inject;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;

@ApplicationScoped
public class EntityManagerProducer
{
    @Inject
    @PersistenceUnit
    private EntityManagerFactory emf;

    @Default
    @Produces // you can also make this @RequestScoped
    public EntityManager create()
    {
        return emf.createEntityManager();
    }

    public void close(@Disposes @Default EntityManager em)
    {
        if (em.isOpen())
        {
            em.close();
        }
    }
    @PreDestroy
    public void closeFactory() {
        if(emf.isOpen()) {
            emf.close();
        }
    }
}