package cz.inspire.utils.repository;

import cz.inspire.utils.repository.annotations.Limit;
import cz.inspire.utils.repository.annotations.Offset;
import cz.inspire.utils.repository.annotations.QueryParam;
import lombok.Getter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Getter
public class QueryMethodMetadata {
    private final QueryParameterMapping[] parameterMappings;

    public QueryMethodMetadata(QueryParameterMapping[] parameterMappings) {
        this.parameterMappings = parameterMappings;
    }

    public static QueryMethodMetadata fromMethod(Method method) {
        Parameter[] parameters = method.getParameters();
        QueryParameterMapping[] mappings = new QueryParameterMapping[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (parameter.isAnnotationPresent(QueryParam.class)) {
                QueryParam qp = parameter.getAnnotation(QueryParam.class);
                mappings[i] = new QueryParameterMapping(QueryParam.class, qp.value());
            } else if (parameter.isAnnotationPresent(Offset.class)) {
                mappings[i] = new QueryParameterMapping(Offset.class, null);
            } else if (parameter.isAnnotationPresent(Limit.class)) {
                mappings[i] = new QueryParameterMapping(Limit.class, null);
            } else {
                mappings[i] = null; // or throw an error if desired
            }
        }
        return new QueryMethodMetadata(mappings);
    }
}
