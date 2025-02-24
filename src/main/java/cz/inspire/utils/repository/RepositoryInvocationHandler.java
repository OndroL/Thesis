package cz.inspire.utils.repository;

import cz.inspire.utils.repository.annotations.Limit;
import cz.inspire.utils.repository.annotations.Offset;
import cz.inspire.utils.repository.annotations.Query;
import cz.inspire.utils.repository.annotations.QueryParam;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RepositoryInvocationHandler<T> implements InvocationHandler {
    private final Class<T> repositoryInterface;
    private final EntityManager em;
    // Cache method metadata so that reflection is only performed once per method.
    private final Map<Method, QueryMethodMetadata> metadataCache = new ConcurrentHashMap<>();

    public RepositoryInvocationHandler(Class<T> repositoryInterface, EntityManager em) {
        this.repositoryInterface = repositoryInterface;
        this.em = em;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Query queryAnnotation = method.getAnnotation(Query.class);
        if (queryAnnotation != null) {
            String jpql = queryAnnotation.value();
            QueryMethodMetadata metadata = metadataCache.computeIfAbsent(method, QueryMethodMetadata::fromMethod);

            // For this example, assume the result type is List of some entity.
            // In a real implementation, you might infer the result type from generics.
            TypedQuery<?> typedQuery = em.createQuery(jpql, Object.class);

            // Bind parameters based on annotations.
            QueryParameterMapping[] mappings = metadata.getParameterMappings();
            for (int i = 0; i < mappings.length; i++) {
                QueryParameterMapping mapping = mappings[i];
                if (mapping == null) continue; // skip non-annotated parameters
                if (mapping.getAnnotationType() == QueryParam.class) {
                    // Set named parameter.
                    typedQuery.setParameter(mapping.getParameterName(), args[i]);
                } else if (mapping.getAnnotationType() == Offset.class) {
                    typedQuery.setFirstResult((Integer) args[i]);
                } else if (mapping.getAnnotationType() == Limit.class) {
                    typedQuery.setMaxResults((Integer) args[i]);
                }
            }
            
            // Determine the method's return type. If it returns a List, call getResultList.
            // Otherwise, assume a single result.
            if (List.class.isAssignableFrom(method.getReturnType())) {
                return typedQuery.getResultList();
            } else {
                return typedQuery.getSingleResult();
            }
        }
        throw new UnsupportedOperationException("Method not implemented: " + method.getName());
    }
}
