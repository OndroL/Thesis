package cz.inspire.utils.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;

public class RepositoryExtension implements Extension {

    private final Set<Class<?>> repositoryInterfaces = new HashSet<>();

    // Add @Observes so that this method is triggered for each annotated type.
    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> pat) {
        Class<?> clazz = pat.getAnnotatedType().getJavaClass();
        // Only consider interfaces that extend BaseRepository.
        if (clazz.isInterface() && BaseRepository.class.isAssignableFrom(clazz)) {
            repositoryInterfaces.add(clazz);
        }
    }

    void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
        // Obtain an EntityManager instance from CDI so we can create a proxy.
        Bean<?> emBean = bm.resolve(bm.getBeans(EntityManager.class));
        CreationalContext<?> cc = bm.createCreationalContext(emBean);
        EntityManager em = (EntityManager) bm.getReference(emBean, EntityManager.class, cc);

        for (Class<?> repoInterface : repositoryInterfaces) {
            // Create a proxy instance and get its concrete class.
            Object proxyInstance = RepositoryFactory.createRepository(repoInterface, em);
            Class<?> proxyClass = proxyInstance.getClass();

            abd.addBean()
                    // Expose the repository types (including the interface) so injection works.
                    .addTransitiveTypeClosure(repoInterface)
                    // Set the bean class to the concrete proxy class.
                    .beanClass(proxyClass)
                    .scope(ApplicationScoped.class)
                    .id(repoInterface.getName())
                    .produceWith(instance -> {
                        // Create a fresh proxy for each injection.
                        return RepositoryFactory.createRepository(repoInterface, em);
                    })
                    .disposeWith((instance, ctx) -> {
                        // No disposal actions required.
                    });
        }
    }
}
