package cz.inspire.utils.repository;

import jakarta.persistence.EntityManager;
import java.lang.reflect.Proxy;

public class RepositoryFactory {
    public static <T> T createRepository(Class<T> repositoryInterface, EntityManager em) {
        RepositoryInvocationHandler<T> handler = new RepositoryInvocationHandler<>(repositoryInterface, em);
        return repositoryInterface.cast(Proxy.newProxyInstance(
                repositoryInterface.getClassLoader(),
                new Class[] { repositoryInterface },
                handler
        ));
    }
}
