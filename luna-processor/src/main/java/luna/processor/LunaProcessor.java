package luna.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import luna.annotation.State;

@SupportedAnnotationTypes("luna.annotation.State")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(Processor.class)
public class LunaProcessor extends AbstractProcessor {
    private Elements elementUtils;
    private Types typeUtils;
    private Map<TypeElement, List<VariableElement>> map;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        map = new HashMap<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(State.class);

        // 获取所有的类及对应需要保存的状态
        for (Element element : elements) {
            VariableElement variableElement = (VariableElement) element;
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();

            if (!map.containsKey(typeElement)) {
                map.put(typeElement, new ArrayList<VariableElement>());
            }
            List<VariableElement> list = map.get(typeElement);
            list.add(variableElement);
        }

        for (Map.Entry<TypeElement, List<VariableElement>> entry : map.entrySet()) {
            TypeElement typeElement = entry.getKey();
            List<VariableElement> variableElements = entry.getValue();

            // public void onSaveInstanceState(Bundle outStatue)
            MethodSpec.Builder onSaveInstanceStateMethodSpecBuilder = MethodSpec.methodBuilder("onSaveInstanceState")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(TypeName.VOID)
                    .addParameter(ClassName.get(typeElement.asType()), "activity")
                    .addParameter(ClassName.get("android.os", "Bundle"), "bundle");

            TypeElement parcelableTypeElement = elementUtils.getTypeElement("android.os.Parcelable");

            for (VariableElement variableElement : variableElements) {
                String variableName = variableElement.getSimpleName().toString();
                TypeName typeName = TypeName.get(variableElement.asType());
                if (typeName.isBoxedPrimitive()) typeName = typeName.unbox();
                if (typeName.isPrimitive()) {
                    onSaveInstanceStateMethodSpecBuilder.addStatement("bundle.put$L($S, activity.$L)", toCamelCase(typeName.toString()), typeName.toString(), variableName);
                } else if (typeName.equals(ClassName.get("java.lang", "String"))) {
                    onSaveInstanceStateMethodSpecBuilder.addStatement("bundle.putString($S, activity.$L)", variableName, variableName);
                } else if (typeUtils.isSubtype(variableElement.asType(), parcelableTypeElement.asType())) {
                    onSaveInstanceStateMethodSpecBuilder.addStatement("bundle.putParcelable($S, activity.$L)", variableName, variableName);
                }
            }

            // public void onRestoreInstanceState(MainActivity activity, Bundle bundle)
            MethodSpec.Builder onRestoreInstanceStateMethodSpecBuilder = MethodSpec.methodBuilder("onRestoreInstanceState")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(TypeName.VOID)
                    .addParameter(ClassName.get(typeElement.asType()), "activity")
                    .addParameter(ClassName.get("android.os", "Bundle"), "bundle");

            for (VariableElement variableElement : variableElements) {
                String variableName = variableElement.getSimpleName().toString();
                TypeName typeName = TypeName.get(variableElement.asType());
                if (typeName.isBoxedPrimitive()) typeName = typeName.unbox();
                if (typeName.isPrimitive()) {
                    onRestoreInstanceStateMethodSpecBuilder.addStatement("activity.$L = bundle.get$L($S)", variableName, toCamelCase(typeName.toString()), typeName.toString());
                } else if (typeName.equals(ClassName.get("java.lang", "String"))) {
                    onRestoreInstanceStateMethodSpecBuilder.addStatement("activity.$L = bundle.getString($S)", variableName, variableName);
                } else if (typeUtils.isSubtype(variableElement.asType(), parcelableTypeElement.asType())) {
                    onRestoreInstanceStateMethodSpecBuilder.addStatement("activity.$L = bundle.getParcelable($S)", variableName, variableName);
                }
            }

            // public class LunaMainActivity
            TypeSpec typeSpec = TypeSpec.classBuilder("Luna" + typeElement.getSimpleName())
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(onSaveInstanceStateMethodSpecBuilder.build())
                    .addMethod(onRestoreInstanceStateMethodSpecBuilder.build())
                    .build();
            JavaFile javaFile = JavaFile.builder(getPackageName(typeElement), typeSpec).build();

            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return true;
    }

    private String getPackageName(TypeElement typeElement) {
        return elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
    }

    private String toCamelCase(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

//        MethodSpec main = MethodSpec.methodBuilder("main")
//                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
//                .returns(void.class)
//                .addParameter(String[].class, "args")
//                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
//                .build();
//        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
//                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//                .addMethod(main)
//                .build();
//        JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
//                .build();
//        try {
//            javaFile.writeTo(processingEnv.getFiler());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
}
