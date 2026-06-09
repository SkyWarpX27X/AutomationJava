package org.example;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import java.io.IOException;
import java.util.*;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_25)
@SupportedAnnotationTypes("org.example.ProductModel")
public class AnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(ProductModel.class)) {
            if (element.getKind() == ElementKind.INTERFACE) {
                ProductModel annotation = element.getAnnotation(ProductModel.class);
                if (annotation == null) return false;
                try {
                    generateCode(element, annotation.fields());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return true;
    }
    private void generateCode(Element element, ProductModel.Field[] fieldDefs) throws IOException {
        String generatedClassName = element.getSimpleName() + "Model";
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(generatedClassName)
                .addModifiers(Modifier.PUBLIC);
        for (ProductModel.Field field : fieldDefs) {
            generateField(classBuilder, field.name(), getFieldTypeName(field), false);
        }
        generateField(classBuilder, "id", TypeName.LONG, false);
        generateField(classBuilder, "quantity", TypeName.INT, true);
        JavaFile javaFile = JavaFile.builder(processingEnv.getElementUtils()
                .getPackageOf(element).getQualifiedName().toString(), classBuilder.build())
                .build();

        javaFile.writeTo(processingEnv.getFiler());
    }
    private TypeName getFieldTypeName(ProductModel.Field field) {
        try {
            return TypeName.get(field.type());
        } catch (MirroredTypeException mte) {
            return TypeName.get(mte.getTypeMirror());
        }
    }
    private void generateField(TypeSpec.Builder classBuilder, String name, TypeName type, boolean naturalNumber) {
        FieldSpec fieldSpec;
        if (naturalNumber) {
            fieldSpec = FieldSpec.builder(type, name)
                    .addModifiers(Modifier.PRIVATE)
                    .addAnnotation(NaturalNumber.class)
                    .build();
        } else {
            fieldSpec = FieldSpec.builder(type, name)
                    .addModifiers(Modifier.PRIVATE)
                    .build();
        }
        classBuilder.addField(fieldSpec);
        String capitalized = name.substring(0, 1).toUpperCase() + name.substring(1);
        MethodSpec getter = MethodSpec.methodBuilder("get" + capitalized)
                .addModifiers(Modifier.PUBLIC)
                .returns(type)
                .addStatement("return this.$N", name)
                .build();
        classBuilder.addMethod(getter);
        MethodSpec setter = MethodSpec.methodBuilder("set" + capitalized)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(type, name)
                .addStatement("this.$N = $N", name, name)
                .build();
        classBuilder.addMethod(setter);
    }
}
