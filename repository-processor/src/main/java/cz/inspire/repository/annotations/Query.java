package cz.inspire.repository.annotations;

import org.intellij.lang.annotations.Language;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Query {
    // Annotation for value parameter which tells IntelliJ to enable click-through navigation,
    // syntax highlighting, and error detection in SQL string.
    @Language("JPQL") // This only works for IntelliJ
    String value();
}
