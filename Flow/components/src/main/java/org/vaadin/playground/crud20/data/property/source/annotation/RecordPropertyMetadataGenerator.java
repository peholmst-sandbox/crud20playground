package org.vaadin.playground.crud20.data.property.source.annotation;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("org.vaadin.playground.crud20.data.property.source.annotation.GeneratePropertyMetadata")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@AutoService(Processor.class)
public class RecordPropertyMetadataGenerator extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (var annotation : annotations) {
            var elements = roundEnv.getElementsAnnotatedWith(annotation);
            for (var element : elements) {
                if (element.getKind() == ElementKind.RECORD) {
                    var recordName = ((TypeElement) element).getQualifiedName();
                    var recordComponents = element.getEnclosedElements().stream().filter(e -> e.getKind() == ElementKind.RECORD_COMPONENT).map(e -> new RecordComponent(e.getSimpleName(), e.asType())).toList();
                    if (recordComponents.isEmpty()) {
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Record " + recordName + " has no components, ignoring", element);
                        continue;
                    }
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Generating metadata for " + recordName + " with components " + recordComponents);
                    try {
                        writeRecordMetadataFile(recordName, recordComponents);
                    } catch (IOException ex) {
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Failed to write metadata file: " + ex.getMessage(), element);
                    }
                } else if (element.getKind() == ElementKind.CLASS) {
                    var className = ((TypeElement) element).getQualifiedName();
                    var gettersAndSetters = element.getEnclosedElements()
                            .stream()
                            .filter(e -> e.getKind() == ElementKind.METHOD).map(e -> (ExecutableElement) e)
                            .filter(this::isGetterOrSetter)
                            .sorted(Comparator.comparing(e -> e.getSimpleName().toString()))
                            .toList();
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Generating metadata for " + className + " with getters and setters " + gettersAndSetters);
                    try {
                        writeBeanMetadataFile(className, extractBeanProperties(gettersAndSetters));
                    } catch (IOException ex) {
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Failed to write metadata file: " + ex.getMessage(), element);
                    }
                } else {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@GeneratePropertyMetadata can only be put on records and classes", element);
                }
            }
        }
        return true;
    }

    private void writeRecordMetadataFile(Name recordName, List<RecordComponent> recordComponents) throws IOException {
        var metadataClassName = recordName.toString() + "Metadata";
        var metadataClassNameParts = extractTypeName(metadataClassName);

        var metadataFile = processingEnv.getFiler().createSourceFile(metadataClassName);
        try (var out = new PrintWriter(metadataFile.openWriter())) {
            if (metadataClassNameParts.packageName != null) {
                out.print("package ");
                out.print(metadataClassNameParts.packageName);
                out.println(";");
                out.println();
            }

            out.println("import org.vaadin.playground.crud20.data.property.source.RecordPropertyDefinition;");
            out.println();

            out.print("public final class ");
            out.print(metadataClassNameParts.simpleName);
            out.println(" {");
            out.println();

            // Private constructor
            {
                out.print("    private ");
                out.print(metadataClassNameParts.simpleName);
                out.println("() {");
                out.println("        // NOP");
                out.println("    }");
                out.println();
            }

            // Definitions
            {
                for (var component : recordComponents) {
                    var type = getObjectType(component.type);
                    out.print("    public static final RecordPropertyDefinition<");
                    out.print(recordName);
                    out.print(", ");
                    out.print(type);
                    out.print("> ");
                    out.print(component.name);
                    out.print(" = new RecordPropertyDefinition<>(");
                    out.print(recordName);
                    out.print(".class, \"");
                    out.print(component.name);
                    out.print("\", ");
                    out.print(type);
                    out.println(".class);");
                }
            }

            out.println("}");
        }
    }

    private void writeBeanMetadataFile(Name className, List<BeanProperty> beanProperties) throws IOException {
        var metadataClassName = className.toString() + "Metadata";
        var metadataClassNameParts = extractTypeName(metadataClassName);

        var metadataFile = processingEnv.getFiler().createSourceFile(metadataClassName);
        try (var out = new PrintWriter(metadataFile.openWriter())) {
            if (metadataClassNameParts.packageName != null) {
                out.print("package ");
                out.print(metadataClassNameParts.packageName);
                out.println(";");
                out.println();
            }

            out.println("import org.vaadin.playground.crud20.data.property.source.ReadOnlyBeanPropertyDefinition;");
            out.println("import org.vaadin.playground.crud20.data.property.source.WritableBeanPropertyDefinition;");
            out.println();

            out.print("public final class ");
            out.print(metadataClassNameParts.simpleName);
            out.println(" {");
            out.println();

            // Private constructor
            {
                out.print("    private ");
                out.print(metadataClassNameParts.simpleName);
                out.println("() {");
                out.println("        // NOP");
                out.println("    }");
                out.println();
            }

            // Definitions
            {
                for (var beanProperty : beanProperties) {
                    var definitionType = beanProperty.setter == null ? "ReadOnlyBeanPropertyDefinition" : "WritableBeanPropertyDefinition";
                    var type = getObjectType(beanProperty.type);
                    out.print("    public static final ");
                    out.print(definitionType);
                    out.print("<");
                    out.print(className);
                    out.print(", ");
                    out.print(type);
                    out.print("> ");
                    out.print(beanProperty.name);
                    out.print(" = new ");
                    out.print(definitionType);
                    out.print("<>(");
                    out.print(className);
                    out.print(".class, \"");
                    out.print(beanProperty.name);
                    out.print("\", ");
                    out.print(type);
                    out.print(".class, \"");
                    out.print(beanProperty.getter.getSimpleName());
                    out.print("\", ");
                    if (beanProperty.setter != null) {
                        out.print("\"");
                        out.print(beanProperty.setter.getSimpleName());
                        out.print("\"");
                    }
                    out.println(");");
                }
            }
            out.println("}");
        }
    }

    private TypeMirror getObjectType(TypeMirror type) {
        if (type.getKind().isPrimitive()) {
            return processingEnv.getTypeUtils().boxedClass(processingEnv.getTypeUtils().getPrimitiveType(type.getKind())).asType();
        }
        return type;
    }

    private boolean isGetterOrSetter(ExecutableElement element) {
        var simpleName = element.getSimpleName().toString();
        if (simpleName.startsWith("get") || simpleName.startsWith("is")) {
            return element.getParameters().isEmpty() && !element.getReturnType().getKind().equals(TypeKind.VOID);
        } else if (simpleName.startsWith("set")) {
            return element.getParameters().size() == 1 && element.getReturnType().getKind().equals(TypeKind.VOID);
        } else {
            return false;
        }
    }

    private record RecordComponent(Name name, TypeMirror type) {
    }

    private List<BeanProperty> extractBeanProperties(List<ExecutableElement> gettersAndSetters) {
        var map = new HashMap<String, BeanProperty>();
        for (var element : gettersAndSetters) {
            var simpleName = element.getSimpleName().toString();
            var propertyName = fixPropertyNameCase(simpleName.substring(3));
            // gettersAndSetters is ordered so getters always show up before setters
            if (simpleName.startsWith("get")) {
                map.put(propertyName, new BeanProperty(propertyName, element.getReturnType(), element, null));
            } else if (simpleName.startsWith("is")) {
                map.put(propertyName, new BeanProperty(propertyName, element.getReturnType(), element, null));
            } else if (simpleName.startsWith("set")) {
                var property = map.get(propertyName);
                if (property == null) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Setter without getter: " + element, element);
                } else {
                    map.put(propertyName, new BeanProperty(propertyName, property.type, property.getter, element));
                }
            }
        }
        return map.values().stream().sorted(Comparator.comparing(BeanProperty::name)).toList();
    }

    private String fixPropertyNameCase(String name) {
        if (name.length() >= 2 && Character.isUpperCase(name.charAt(0)) && Character.isUpperCase(name.charAt(1))) {
            return name;
        }
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    private record BeanProperty(String name, TypeMirror type, ExecutableElement getter, ExecutableElement setter) {
    }

    private static TypeName extractTypeName(String fullName) {
        var lastDot = fullName.lastIndexOf('.');
        String packageName = null;
        if (lastDot > 0) {
            packageName = fullName.substring(0, lastDot);
        }
        var simpleName = fullName.substring(lastDot + 1);
        return new TypeName(simpleName, packageName);
    }

    private record TypeName(String simpleName, String packageName) {
    }
}
