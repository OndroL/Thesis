package cz.inspire.utils.repository;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import cz.inspire.utils.repository.annotations.Limit;
import cz.inspire.utils.repository.annotations.MyRepository;
import cz.inspire.utils.repository.annotations.Offset;
import cz.inspire.utils.repository.annotations.Query;
import cz.inspire.utils.repository.annotations.QueryParam;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("cz.inspire.utils.repository.annotations.MyRepository")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class RepositoryProcessor extends AbstractProcessor {

    private Elements elementUtils;

    public RepositoryProcessor() {}

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // Iterate over all elements in the current round.
        for (Element element : roundEnv.getElementsAnnotatedWith(MyRepository.class)) {
            if (element.getKind() == ElementKind.INTERFACE) {
                TypeElement typeElement = (TypeElement) element;
                generateRepositoryImplementation(typeElement);
            }
        }
        return true;
    }

    private void generateRepositoryImplementation(TypeElement repoInterface) {
        String packageName = elementUtils.getPackageOf(repoInterface).getQualifiedName().toString();
        String interfaceName = repoInterface.getSimpleName().toString();
        String className = "_" + interfaceName; // e.g. _InstructorTestRepository

        ClassName repoInterfaceName = ClassName.get(packageName, interfaceName);
        ClassName entityManagerClass = ClassName.get("jakarta.persistence", "EntityManager");

        // Create a field for EntityManager, annotated for CDI injection.
        FieldSpec emField = FieldSpec.builder(entityManagerClass, "em", Modifier.PRIVATE)
                .addAnnotation(ClassName.get("jakarta.inject", "Inject"))
                .build();

        // Build the implementation class, and annotate with @ApplicationScoped.
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ClassName.get("jakarta.enterprise.context", "ApplicationScoped"))
                .addSuperinterface(repoInterfaceName)
                .addField(emField);

        // Process each method in the repository interface.
        for (Element enclosed : repoInterface.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.METHOD) {
                ExecutableElement method = (ExecutableElement) enclosed;
                Query queryAnnotation = method.getAnnotation(Query.class);
                MethodSpec methodSpec = generateMethodImplementation(method, queryAnnotation);
                if (methodSpec != null) {
                    classBuilder.addMethod(methodSpec);
                }
            }
        }

        TypeSpec implClass = classBuilder.build();
        // Optionally place the generated class in a sub-package, for example ".generated"
        String generatedPackage = packageName + ".generated";

        JavaFile javaFile = JavaFile.builder(generatedPackage, implClass)
                .build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "Failed to write generated class: " + e.getMessage());
        }
    }

    private MethodSpec generateMethodImplementation(ExecutableElement method, Query queryAnnotation) {
        if (queryAnnotation != null) {
            // Advanced @Query implementation (as before)...
            String jpql = queryAnnotation.value();

            // Determine the entity type based on the return type.
            TypeMirror returnType = method.getReturnType();
            TypeMirror entityTypeMirror = null;

            // Get helper elements for List and Optional.
            TypeElement listTypeElement = elementUtils.getTypeElement("java.util.List");
            TypeElement optionalTypeElement = elementUtils.getTypeElement("java.util.Optional");

            // If the return type is a List<T>, extract T.
            if (listTypeElement != null &&
                    processingEnv.getTypeUtils().isAssignable(
                            processingEnv.getTypeUtils().erasure(returnType),
                            processingEnv.getTypeUtils().erasure(listTypeElement.asType()))) {
                if (returnType instanceof DeclaredType) {
                    List<? extends TypeMirror> typeArguments = ((DeclaredType) returnType).getTypeArguments();
                    if (!typeArguments.isEmpty()) {
                        entityTypeMirror = typeArguments.get(0);
                    }
                }
            }
            // Else if the return type is Optional<T>, extract T.
            else if (optionalTypeElement != null &&
                    processingEnv.getTypeUtils().isAssignable(
                            processingEnv.getTypeUtils().erasure(returnType),
                            processingEnv.getTypeUtils().erasure(optionalTypeElement.asType()))) {
                if (returnType instanceof DeclaredType) {
                    List<? extends TypeMirror> typeArguments = ((DeclaredType) returnType).getTypeArguments();
                    if (!typeArguments.isEmpty()) {
                        entityTypeMirror = typeArguments.get(0);
                    }
                }
            } else {
                // Otherwise, assume the return type itself is the entity type.
                entityTypeMirror = returnType;
            }

            // Fallback: if we couldn’t determine, default to Object.
            if (entityTypeMirror == null) {
                entityTypeMirror = elementUtils.getTypeElement("java.lang.Object").asType();
            }

            // Convert the entity type mirror into a TypeElement and then a ClassName.
            TypeElement entityElement = (TypeElement) processingEnv.getTypeUtils().asElement(entityTypeMirror);
            ClassName inferredType = ClassName.get(entityElement);

            // Build the method implementation for a @Query method.
            MethodSpec.Builder methodBuilder = MethodSpec.overriding(method);
            methodBuilder.addStatement("$T query = em.createQuery($S, $T.class)",
                    ClassName.get("jakarta.persistence", "TypedQuery"),
                    jpql,
                    inferredType);

            // Bind parameters.
            for (VariableElement param : method.getParameters()) {
                String paramName = param.getSimpleName().toString();
                if (param.getAnnotation(QueryParam.class) != null) {
                    QueryParam qp = param.getAnnotation(QueryParam.class);
                    methodBuilder.addStatement("query.setParameter($S, $N)", qp.value(), paramName);
                } else if (param.getAnnotation(Offset.class) != null) {
                    methodBuilder.addStatement("query.setFirstResult($N)", paramName);
                } else if (param.getAnnotation(Limit.class) != null) {
                    methodBuilder.addStatement("query.setMaxResults($N)", paramName);
                }
            }

            // Generate return statement based on return type.
            String returnTypeStr = method.getReturnType().toString();
            if (returnTypeStr.startsWith("java.util.List")) {
                methodBuilder.addStatement("return query.getResultList()");
            } else if (returnTypeStr.startsWith("java.util.Optional")) {
                methodBuilder.addStatement("return java.util.Optional.ofNullable(query.getSingleResult())");
            } else {
                methodBuilder.addStatement("return query.getSingleResult()");
            }
            return methodBuilder.build();
        } else {
            // Not a @Query method: check if it's one of the CRUD methods.
            String methodName = method.getSimpleName().toString();
            switch (methodName) {
                case "create": {
                    VariableElement param = method.getParameters().get(0);
                    String paramName = param.getSimpleName().toString();
                    return MethodSpec.overriding(method)
                            .addStatement("em.persist($N)", paramName)
                            .addStatement("em.flush()")
                            .addStatement("return $N", paramName)
                            .build();
                }
                case "update": {
                    VariableElement param = method.getParameters().get(0);
                    String paramName = param.getSimpleName().toString();
                    return MethodSpec.overriding(method)
                            .addStatement("$T merged = em.merge($N)", param.asType(), paramName)
                            .addStatement("em.flush()")
                            .addStatement("return merged")
                            .build();
                }
                case "delete": {
                    VariableElement param = method.getParameters().get(0);
                    String paramName = param.getSimpleName().toString();
                    return MethodSpec.overriding(method)
                            .addStatement("em.remove(em.merge($N))", paramName)
                            .addStatement("em.flush()")
                            .build();
                }
                case "findById": {
                    // Assume return type is Optional<E>
                    TypeMirror returnType = method.getReturnType();
                    TypeMirror entityTypeMirror = null;
                    TypeElement optionalType = elementUtils.getTypeElement("java.util.Optional");
                    if (optionalType != null &&
                            processingEnv.getTypeUtils().isAssignable(
                                    processingEnv.getTypeUtils().erasure(returnType),
                                    processingEnv.getTypeUtils().erasure(optionalType.asType()))) {
                        if (returnType instanceof DeclaredType) {
                            List<? extends TypeMirror> typeArguments = ((DeclaredType)returnType).getTypeArguments();
                            if (!typeArguments.isEmpty()) {
                                entityTypeMirror = typeArguments.get(0);
                            }
                        }
                    }
                    if (entityTypeMirror == null) {
                        entityTypeMirror = elementUtils.getTypeElement("java.lang.Object").asType();
                    }
                    TypeElement entityElement = (TypeElement) processingEnv.getTypeUtils().asElement(entityTypeMirror);
                    ClassName inferredType = ClassName.get(entityElement);
                    String idParam = method.getParameters().get(0).getSimpleName().toString();

                    return MethodSpec.overriding(method)
                            .beginControlFlow("if ($N == null)", idParam)
                            .addStatement("return java.util.Optional.empty()")
                            .endControlFlow()
                            .addStatement("return java.util.Optional.ofNullable(em.find($T.class, $N))", inferredType, idParam)
                            .build();
                }
                case "findByPrimaryKey": {
                    // Assume return type is E.
                    TypeMirror returnType = method.getReturnType();
                    TypeMirror entityTypeMirror = returnType;
                    if (entityTypeMirror == null) {
                        entityTypeMirror = elementUtils.getTypeElement("java.lang.Object").asType();
                    }
                    TypeElement entityElement = (TypeElement) processingEnv.getTypeUtils().asElement(entityTypeMirror);
                    ClassName inferredType = ClassName.get(entityElement);
                    String idParam = method.getParameters().get(0).getSimpleName().toString();

                    return MethodSpec.overriding(method)
                            .addStatement("$T result = em.find($T.class, $N)", inferredType, inferredType, idParam)
                            .beginControlFlow("if (result == null)")
                            .addStatement("throw new jakarta.persistence.FinderException($S)", "Entity not found")
                            .endControlFlow()
                            .addStatement("return result")
                            .build();
                }
                default:
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,
                            "Skipping method " + methodName + " as it is not recognized");
                    return null;
            }
        }
    }


}
