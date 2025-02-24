package cz.inspire.utils.repository;

import lombok.Getter;

@Getter
public class QueryParameterMapping {
    private final Class<?> annotationType;
    private final String parameterName; // For QueryParam; null for Offset/Limit

    public QueryParameterMapping(Class<?> annotationType, String parameterName) {
        this.annotationType = annotationType;
        this.parameterName = parameterName;
    }

}
