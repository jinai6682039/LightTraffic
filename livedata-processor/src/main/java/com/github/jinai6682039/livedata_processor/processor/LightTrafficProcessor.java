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
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

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

//        BinderSet binderSet = new BinderSet();
//
//        JavaFile javaFile = binderSet.generateJava(1);
//
//        try {
//            javaFile.writeTo(mFiler);
//        } catch (IOException e) {
//            throw new WriteJavaFileFailureException("test", e.getMessage());
//        }
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

        for (Element element : environment.getElementsAnnotatedWith(ObserverMethod.class)) {
            if (!SuperficialValidation.validateElement(element)) {
                continue;
            }

            parseObserverMethod(ObserverMethod.class, element);
        }
    }

    private void parseObserverMethod(Class<? extends Annotation> annotationClass, Element element) {
        if (!(element instanceof ExecutableElement) || element.getKind() != ElementKind.METHOD) {
            throw new IllegalStateException(
                    String.format("@%s annotation must be on a method.", annotationClass.getSimpleName()));
        }

        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        ExecutableElement executableElement = (ExecutableElement) element;
        Annotation annotation = element.getAnnotation(annotationClass);

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
            }

            BindObserverParam param = new BindObserverParam.Builder()
                    .setParamName(paramName)
                    .setParamAnnotationName(paramAnnotationName)
                    .setParamTypeString(paramTypeString)
                    .decodeParamType().build();

            BindObserverMethod method = new BindObserverMethod();
        }

    }

}
