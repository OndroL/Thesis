package cz.inspire.repository;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import cz.inspire.repository.annotations.Limit;
import cz.inspire.repository.annotations.Repository;
import cz.inspire.repository.annotations.Offset;
import cz.inspire.repository.annotations.Query;

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
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SupportedAnnotationTypes("cz.inspire.repository.annotations.Repository")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class RepositoryProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Types typeUtils;

    public RepositoryProcessor() {}

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Repository.class)) {
            if (element.getKind() == ElementKind.INTERFACE) {
                generateRepositoryImplementation((TypeElement) element);
            }
        }
        return true;
    }

    private void generateRepositoryImplementation(TypeElement repoInterface) {
        String pkg = elementUtils.getPackageOf(repoInterface).getQualifiedName().toString();
        String interfaceName = repoInterface.getSimpleName().toString();
        String className = "_" + interfaceName;

        ClassName repoInterfaceName = ClassName.get(pkg, interfaceName);
        ClassName emClass = ClassName.get("jakarta.persistence", "EntityManager");

        FieldSpec emField = FieldSpec.builder(emClass, "em", Modifier.PRIVATE)
                .addAnnotation(ClassName.get("jakarta.inject", "Inject"))
                .build();

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ClassName.get("jakarta.enterprise.context", "ApplicationScoped"))
                .addSuperinterface(repoInterfaceName)
                .addField(emField);

        Set<String> processed = new HashSet<>();
        // Process declared methods.
        for (Element e : repoInterface.getEnclosedElements()) {
            if (e.getKind() == ElementKind.METHOD) {
                ExecutableElement method = (ExecutableElement) e;
                processed.add(getSignature(method));
                MethodSpec ms = generateMethod(method, repoInterface);
                if (ms != null) {
                    classBuilder.addMethod(ms);
                }
            }
        }
        // Process inherited abstract methods.
        List<? extends Element> allMembers = elementUtils.getAllMembers(repoInterface);
        for (Element e : allMembers) {
            if (e.getKind() == ElementKind.METHOD) {
                ExecutableElement method = (ExecutableElement) e;
                if (method.getEnclosingElement().toString().equals("java.lang.Object"))
                    continue;
                String sig = getSignature(method);
                if (processed.contains(sig))
                    continue;
                if (method.getModifiers().contains(Modifier.ABSTRACT)) {
                    MethodSpec ms = generateMethod(method, repoInterface);
                    if (ms != null) {
                        classBuilder.addMethod(ms);
                        processed.add(sig);
                    }
                }
            }
        }

        TypeSpec implClass = classBuilder.build();
        JavaFile javaFile = JavaFile.builder(pkg, implClass).build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException ioe) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "IO Error: " + ioe.getMessage());
        }
    }

    // Build a signature key: method name + parameter types.
    private String getSignature(ExecutableElement method) {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getSimpleName()).append("(");
        for (VariableElement p : method.getParameters()) {
            sb.append(p.asType().toString()).append(",");
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * Generate a method implementation. If the method is annotated with @Query,
     * generate a JPQL-based implementation (with appropriate casts); otherwise,
     * generate a CRUD method.
     */
    private MethodSpec generateMethod(ExecutableElement method, TypeElement repoInterface) {
        DeclaredType repoType = (DeclaredType) repoInterface.asType();
        // Get mapping from BaseRepository's type parameters.
        TypeElement baseRepo = elementUtils.getTypeElement("cz.inspire.repository.BaseRepository");
        Map<? extends TypeParameterElement, ? extends TypeMirror> typeMap = new HashMap<>();
        if (baseRepo != null) {
            typeMap = getTypeArguments(repoInterface, baseRepo);
        }
        // Substitute method return type.
        TypeMirror subReturn = substituteType(method.getReturnType(), typeMap);

        MethodSpec.Builder builder;
        try {
            builder = MethodSpec.overriding(method, repoType, typeUtils);
        } catch (Exception e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,
                    "Override failed for " + method.getSimpleName() + ": " + e.getMessage());
            return null;
        }
        builder.returns(TypeName.get(subReturn));

        Query query = method.getAnnotation(Query.class);
        if (query != null) {
            // Query-based method.
            String jpql = query.value();

            // Extract expected parameter names from the query using a regex.
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(":(\\w+)");
            java.util.regex.Matcher matcher = pattern.matcher(jpql);
            Set<String> expectedParams = new java.util.HashSet<>();
            while (matcher.find()) {
                expectedParams.add(matcher.group(1));
            }

            // Build a set of parameter names provided in the method.
            Set<String> providedParams = new java.util.HashSet<>();
            for (VariableElement param : method.getParameters()) {
                if (param.getAnnotation(Limit.class) != null || param.getAnnotation(Offset.class) != null) {
                    continue;
                }
                providedParams.add(param.getSimpleName().toString());
            }

            if (!expectedParams.equals(providedParams)) {
                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.ERROR,
                        "Query parameter mismatch in method " + method.getSimpleName() +
                                ". Expected: " + expectedParams +
                                ", Found: " + providedParams,
                        method);
                return null; // Skip generation for this method.
            }

            TypeMirror entityType = null;
            // If retType is List<T> or Optional<T>, extract T.
            TypeElement listEl = elementUtils.getTypeElement("java.util.List");
            TypeElement optionalEl = elementUtils.getTypeElement("java.util.Optional");
            if (listEl != null && typeUtils.isAssignable(typeUtils.erasure(subReturn), typeUtils.erasure(listEl.asType()))) {
                if (subReturn instanceof DeclaredType) {
                    List<? extends TypeMirror> args = ((DeclaredType) subReturn).getTypeArguments();
                    if (!args.isEmpty()) {
                        entityType = substituteType(args.getFirst(), typeMap);
                    }
                }
            } else if (optionalEl != null && typeUtils.isAssignable(typeUtils.erasure(subReturn), typeUtils.erasure(optionalEl.asType()))) {
                if (subReturn instanceof DeclaredType) {
                    List<? extends TypeMirror> args = ((DeclaredType) subReturn).getTypeArguments();
                    if (!args.isEmpty()) {
                        entityType = substituteType(args.getFirst(), typeMap);
                    }
                }
            } else {
                entityType = subReturn;
            }
            if (entityType == null) {
                entityType = elementUtils.getTypeElement("java.lang.Object").asType();
            }
            TypeElement entityEl = (TypeElement) typeUtils.asElement(entityType);
            ClassName inferred = ClassName.get(entityEl);
            builder.addStatement("$T query = em.createQuery($S, $T.class)",
                    ClassName.get("jakarta.persistence", "TypedQuery"),
                    jpql,
                    inferred);
            for (VariableElement param : method.getParameters()) {
                String name = param.getSimpleName().toString();
                if (param.getAnnotation(Offset.class) != null) {
                    builder.addStatement("query.setFirstResult($N)", name);
                } else if (param.getAnnotation(Limit.class) != null) {
                    builder.addStatement("query.setMaxResults($N)", name);
                } else {
                    builder.addStatement("query.setParameter($S, $N)", name, name);
                }
            }
            String retStr = TypeName.get(subReturn).toString();
            // If return type is List, no cast is needed.
            if (retStr.startsWith("java.util.List")) {
                builder.addStatement("return query.getResultList()");
            }
            // For Optional return type, generate a cast on the single result then wrap in Optional.
            else if (retStr.startsWith("java.util.Optional")) {
                builder.addStatement("return java.util.Optional.ofNullable(($T) query.getSingleResult())", inferred);
            }
            // Otherwise, generate a cast to the declared return type.
            else {
                builder.addStatement("return ($T) query.getSingleResult()", inferred);
            }
        } else {
            // CRUD methods.
            String mName = method.getSimpleName().toString();
            switch (mName) {
                case "create": {
                    VariableElement param = method.getParameters().getFirst();
                    String pName = param.getSimpleName().toString();
                    builder.addStatement("em.persist($N)", pName)
                            .addStatement("em.flush()")
                            .addStatement("return $N", pName);
                    break;
                }
                case "update": {
                    VariableElement param = method.getParameters().getFirst();
                    String pName = param.getSimpleName().toString();
                    builder.addStatement("$T merged = em.merge($N)",
                                    TypeName.get(substituteType(param.asType(), typeMap)), pName)
                            .addStatement("em.flush()")
                            .addStatement("return merged");
                    break;
                }
                case "delete": {
                    VariableElement param = method.getParameters().getFirst();
                    String pName = param.getSimpleName().toString();
                    // Assume that every entity class has a method "getId()" to obtain its identifier.
                    builder.addStatement("$T managed = em.getReference($T.class, $N.getId())",
                                    TypeName.get(substituteType(param.asType(), typeMap)),
                                    TypeName.get(substituteType(param.asType(), typeMap)),
                                    pName)
                            .addStatement("em.remove(managed)")
                            .addStatement("em.flush()");
                    break;
                }
                case "findById": {
                    VariableElement param = method.getParameters().getFirst();
                    String pName = param.getSimpleName().toString();
                    TypeElement entityEl = (TypeElement) typeUtils.asElement(substituteType(method.getReturnType(), typeMap));
                    ClassName inferred = ClassName.get(entityEl);
                    builder.addStatement("return em.find($T.class, $N)", inferred, pName);
                    break;
                }
                default:
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,
                            "Skipping unrecognized method " + mName);
                    return null;
            }
        }
        return builder.build();
    }

    // Recursively substitute type variables using the provided mapping.
    private TypeMirror substituteType(TypeMirror type, Map<? extends TypeParameterElement, ? extends TypeMirror> mapping) {
        if (type.getKind().isPrimitive()) {
            return type;
        }
        if (type instanceof TypeVariable tv) {
            TypeMirror replacement = mapping.get(tv.asElement());
            return replacement != null ? replacement : type;
        }
        if (type instanceof DeclaredType dt) {
            List<? extends TypeMirror> args = dt.getTypeArguments();
            if (args.isEmpty()) {
                return type;
            }
            TypeMirror[] subs = new TypeMirror[args.size()];
            for (int i = 0; i < args.size(); i++) {
                subs[i] = substituteType(args.get(i), mapping);
            }
            return typeUtils.getDeclaredType((TypeElement) dt.asElement(), subs);
        }
        return type;
    }

    // Custom helper to obtain a mapping from BaseRepository's type parameters to concrete types.
    private Map<TypeParameterElement, TypeMirror> getTypeArguments(TypeElement sub, TypeElement sup) {
        Map<TypeParameterElement, TypeMirror> mapping = new HashMap<>();
        if (typeUtils.isSameType(typeUtils.erasure(sub.asType()), typeUtils.erasure(sup.asType()))) {
            DeclaredType declared = (DeclaredType) sub.asType();
            List<? extends TypeMirror> actuals = declared.getTypeArguments();
            List<? extends TypeParameterElement> formals = sup.getTypeParameters();
            for (int i = 0; i < formals.size(); i++) {
                mapping.put(formals.get(i), actuals.get(i));
            }
            return mapping;
        }
        for (TypeMirror t : typeUtils.directSupertypes(sub.asType())) {
            Element e = typeUtils.asElement(t);
            if (!(e instanceof TypeElement superElem)) continue;
            Map<TypeParameterElement, TypeMirror> result = getTypeArguments(superElem, sup);
            if (!result.isEmpty()) {
                if (t instanceof DeclaredType dt) {
                    List<? extends TypeMirror> actuals = dt.getTypeArguments();
                    List<? extends TypeParameterElement> formals = superElem.getTypeParameters();
                    Map<TypeParameterElement, TypeMirror> current = new HashMap<>();
                    for (int i = 0; i < formals.size(); i++) {
                        current.put(formals.get(i), actuals.get(i));
                    }
                    Map<TypeParameterElement, TypeMirror> substituted = new HashMap<>();
                    for (Map.Entry<TypeParameterElement, TypeMirror> entry : result.entrySet()) {
                        substituted.put(entry.getKey(), substituteType(entry.getValue(), current));
                    }
                    return substituted;
                }
                return result;
            }
        }
        return mapping;
    }
}
