package com.github.jinai6682039.livedata_processor.processor;

import com.github.jinai6682039.livedata_annotation.ObserverMethod;
import com.github.jinai6682039.livedata_annotation.ObserverModel;
import com.github.jinai6682039.livedata_annotation.ObserverParam;
import com.github.jinai6682039.livedata_processor.binder.BindObserverMethod;
import com.github.jinai6682039.livedata_processor.binder.BindObserverParam;
import com.github.jinai6682039.livedata_processor.binder.BinderSet;
import com.github.jinai6682039.livedata_processor.exception.WriteJavaFileFailureException;
import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.sun.source.util.Trees;


import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import retrofit2.http.GET;

import static com.google.auto.common.MoreElements.getPackage;

@AutoService(Processor.class)
public class LightTrafficProcessor extends AbstractProcessor {

    private Types mTypes;
    private Elements mElements;
    private Filer mFiler;
    private Messager mMessager;
    private Trees mTrees;

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        findParseClass(roundEnvironment);
        return false;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mTypes = processingEnvironment.getTypeUtils();
        mElements = processingEnvironment.getElementUtils();
        mFiler = processingEnvironment.getFiler();
        mMessager = processingEnvironment.getMessager();

        mTrees = Trees.instance(processingEnvironment);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    private Set<Class<? extends Annotation>> getSupportAnnotations() {

        Set<Class<? extends  Annotation>> annotations = new LinkedHashSet<>();

        annotations.add(ObserverModel.class);
        annotations.add(ObserverMethod.class);
        annotations.add(ObserverParam.class);

        return annotations;
    }

    @Override
    public Set<String> getSupportedOptions() {
        return Collections.singleton("livedata_processor");
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void findParseClass(RoundEnvironment environment) {

        for (Element element : environment.getElementsAnnotatedWith(ObserverModel.class)) {
            if (!SuperficialValidation.validateElement(element)) {
                continue;
            }

            BinderSet.Builder modelBuild = new BinderSet.Builder();
            modelBuild.setModelClassName(element.getSimpleName().toString());

            String generateClassName = element.getAnnotation(ObserverModel.class).value();
            if (generateClassName.equals("")) {
                generateClassName = element
                        .getSimpleName()
                        .toString()
                        .replace("Model", "ViewModel");
            }
            modelBuild.setGenerateClassName(generateClassName);
            modelBuild.setPackageName(getPackage(element).getQualifiedName().toString());

            for (Element method : element.getEnclosedElements()) {
                if (!SuperficialValidation.validateElement(element)) {
                    continue;
                }

                BindObserverMethod observerMethod = parseObserverMethod(ObserverMethod.class, method);
                if (observerMethod != null) {
                    modelBuild.addObserverMethod(observerMethod);
                }
            }

            BinderSet modelSet = modelBuild.build();

            try {
                modelSet.generateJava().writeTo(mFiler);
            } catch (IOException e) {
                throw new WriteJavaFileFailureException("test", e.getMessage());
            }
        }
    }

    private BindObserverMethod parseObserverMethod(Class<? extends Annotation> annotationClass, Element element) {

        Annotation annotation = element.getAnnotation(annotationClass);
        if (annotation == null || annotation.annotationType() != annotationClass) {
            return null;
        }

        if (!(element instanceof ExecutableElement) || element.getKind() != ElementKind.METHOD) {
            throw new IllegalStateException(
                    String.format("@%s annotation must be on a method.", annotationClass.getSimpleName()));
        }

        ExecutableElement executableElement = (ExecutableElement) element;

        BindObserverMethod.Builder builder = new BindObserverMethod.Builder();
        builder.setMethodName(element.getSimpleName().toString());

        for (Modifier modifier :element.getModifiers()) {
            builder.addModify(modifier != null ? modifier.toString() : "");
        }

        for (VariableElement p : executableElement.getParameters()) {
            String paramName = null;
            String paramAnnotationName = null;
            String paramTypeString = null;

            // 获取此参数的对象名
            paramName = p.getSimpleName().toString();

            // 获取static final对象的初始值，这里为null
            p.getConstantValue();

            // 获取此element的所有者的Element，也就是对应的方法
            p.getEnclosingElement();

            if (p.getAnnotation(ObserverParam.class) != null) {
                paramAnnotationName = p.getAnnotation(ObserverParam.class).value();
            }

            // ElementKind.PARAMETER
            p.getKind();

            // 获取完整的参数类型字符串
            if (p.asType() != null) {
                paramTypeString = p.asType().toString();

                if (paramTypeString.equals("boolean")) {
                    paramTypeString = "java.lang.Boolean";
                } else if (paramTypeString.equals("double")) {
                    paramTypeString = "java.lang.Double";
                } else if (paramTypeString.equals("float")) {
                    paramTypeString = "java.lang.Float";
                } else if (paramTypeString.equals("char")) {
                    paramTypeString = "java.lang.Character";
                } else if (paramTypeString.equals("byte")) {
                    paramTypeString = "java.lang.Byte";
                } else if (paramTypeString.equals("short")) {
                    paramTypeString = "java.lang.Short";
                } else if (paramTypeString.equals("long")) {
                    paramTypeString = "java.lang.Long";
                } else if (paramTypeString.equals("int")) {
                    paramTypeString = "java.lang.Integer";
                }
            }


            BindObserverParam param = new BindObserverParam.Builder()
                    .setParamName(paramName)
                    .setParamAnnotationName(paramAnnotationName)
                    .setAnnotationed(p.getAnnotation(ObserverParam.class) != null)
                    .setParamTypeString(paramTypeString)
                    .decodeParamType().build();

            builder.addOrderObserverParams(param);
        }

        return builder.build();
    }

}
