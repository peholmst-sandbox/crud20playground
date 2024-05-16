package org.vaadin.playground.crud20.data.property.source.annotation;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.PrintWriter;
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
                        writeMetadataFile(recordName, recordComponents);
                    } catch (IOException ex) {
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Failed to write metadata file: " + ex.getMessage(), element);
                    }
                } else {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@GeneratePropertyMetadata can only be put on records", element);
                }
            }
        }
        return true;
    }

    private void writeMetadataFile(Name recordName, List<RecordComponent> recordComponents) throws IOException {
        var metadataClassName = recordName.toString() + "Metadata";
        var lastDot = metadataClassName.lastIndexOf('.');
        String packageName = null;
        if (lastDot > 0) {
            packageName = metadataClassName.substring(0, lastDot);
        }
        var metadataSimpleClassName = metadataClassName.substring(lastDot + 1);

        var metadataFile = processingEnv.getFiler().createSourceFile(metadataClassName);
        try (var out = new PrintWriter(metadataFile.openWriter())) {
            if (packageName != null) {
                out.print("package ");
                out.print(packageName);
                out.println(";");
                out.println();
            }

            out.println("import org.vaadin.playground.crud20.data.property.source.RecordPropertyDefinition;");
            out.println();

            out.print("public final class ");
            out.print(metadataSimpleClassName);
            out.println(" {");
            out.println();

            // Private constructor
            {
                out.print("    private ");
                out.print(metadataSimpleClassName);
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

    private TypeMirror getObjectType(TypeMirror type) {
        if (type.getKind().isPrimitive()) {
            return processingEnv.getTypeUtils().boxedClass(processingEnv.getTypeUtils().getPrimitiveType(type.getKind())).asType();
        }
        return type;
    }

    private record RecordComponent(Name name, TypeMirror type) {
    }
}
