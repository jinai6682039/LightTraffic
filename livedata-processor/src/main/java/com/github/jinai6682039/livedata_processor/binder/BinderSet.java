package com.github.jinai6682039.livedata_processor.binder;

import com.github.jinai6682039.livedata_annotation.ObserverMethod;
import com.github.jinai6682039.livedata_processor.exception.ModelClassCantFindException;
import com.github.jinai6682039.livedata_processor.exception.WriteJavaFileFailureException;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PROTECTED;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * createTime: 2018/9/27
 * by hanxu17
 */
public class BinderSet {

    private static final String LIGHT_TRAFFICE_BASE_PREFIX
            = "com.github.alexhanxs.lighttraffic.base";
    private static final String LIGHT_TRAFFICE_LIVEDATA
            = "com.github.alexhanxs.lighttraffic.base.livedata";
    private static final String LIGHT_TRAFFICE_MAINTHREADOBSERVER
            = "com.github.alexhanxs.lighttraffic.base.observer";
    private static final String LIGHT_TRAFFICE_BASEVIEWMODEL
            = "com.github.alexhanxs.lighttraffic.base.viewmodel";

    private static final ClassName ListClass = ClassName.get(List.class);
    private static final ClassName MainObserverClass = ClassName
            .get(LIGHT_TRAFFICE_MAINTHREADOBSERVER, "MainThreadObserver");
    private static final ClassName BaseViewModelClass = ClassName.
            get(LIGHT_TRAFFICE_BASEVIEWMODEL, "BaseViewModel");
    private static final ClassName TypeLiveData = ClassName.get(LIGHT_TRAFFICE_LIVEDATA,
            "TypeLiveData");
    private static final ClassName WrapLiveData = ClassName.get(LIGHT_TRAFFICE_LIVEDATA,
            "WrapLiveData");

    private static final String mainObserverNamePre = "MainThreadObserver";
    private static final String liveDataNamePre = "TypeLiveData";
    private static final String modelName = "model";
    private static ClassName MODELCLASS = null;

    public JavaFile generateJava() {
        return JavaFile.builder(binderClassName.packageName(), generateType())
                .addFileComment("Generated code from LightTraffic. Do not modify!")
                .build();
    }

    public TypeSpec generateType() {

        TypeSpec.Builder result = TypeSpec.classBuilder(binderClassName.simpleName())
                .addModifiers(PUBLIC);

        result.superclass(BaseViewModelClass);

        result.addField(MODELCLASS, modelName, Modifier.PROTECTED);

        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(PUBLIC);

        constructor.addStatement("model = new " + modelClassName + "()");

        for (BindObserverMethod method : methods) {

            MethodSpec.Builder requestMethod = MethodSpec.methodBuilder(method.methodName);
            if (method.isStatic) {
                requestMethod.addModifiers(STATIC);
            }
            if (method.isPrivate) {
                requestMethod.addModifiers(PRIVATE);
            }
            if (method.isProtected) {
                requestMethod.addModifiers(PROTECTED);
            }
            if (method.isPublic) {
                requestMethod.addModifiers(PUBLIC);
            }
            if (method.isFinal) {
                requestMethod.addModifiers(FINAL);
            }

            String observerParamName = null;

            for (BindObserverParam param : method.orderParams) {
                if (param.isAnnotationed) {
                    // the param need to record into the MainThreadObserver

                    ClassName paramClass = ClassName.get(param.paramImportPrefix, param.paramRawType);
                    requestMethod.addParameter(paramClass, param.paramName);

                } else {
                    // the param is the MainThreadObserver

                    ParameterizedTypeName liveDataGenericsParam = null;
                    ParameterizedTypeName lastGenericsParam = null;
                    ClassName lastParamClassName = null;
                    for (int i = param.orderGenericsTypes.size() - 1; i >= 0; i--) {

                        BindObserverParam genericeParams = param.orderGenericsTypes.get(i);

                        ClassName paramClass = ClassName.get(genericeParams.paramImportPrefix,
                                genericeParams.paramRawType);

                        if (lastParamClassName == null) {
                            lastParamClassName = paramClass;
                        } else {
                            if (lastGenericsParam != null) {
                                lastGenericsParam = ParameterizedTypeName
                                        .get(paramClass, lastGenericsParam);
                            } else {
                                lastGenericsParam = ParameterizedTypeName
                                        .get(paramClass, lastParamClassName);
                            }
                        }
                    }

                    if (lastGenericsParam != null) {
                        liveDataGenericsParam = ParameterizedTypeName.get(WrapLiveData, lastGenericsParam);
                        liveDataGenericsParam = ParameterizedTypeName.get(TypeLiveData, liveDataGenericsParam);
                        lastGenericsParam = ParameterizedTypeName.get(MainObserverClass, lastGenericsParam);
                    } else {
                        liveDataGenericsParam = ParameterizedTypeName.get(WrapLiveData, lastParamClassName);
                        liveDataGenericsParam = ParameterizedTypeName.get(TypeLiveData, liveDataGenericsParam);
                        lastGenericsParam = ParameterizedTypeName.get(MainObserverClass, lastParamClassName);
                    }

                    result.addField(lastGenericsParam, method.methodName + mainObserverNamePre,
                            Modifier.PUBLIC);

                    result.addField(liveDataGenericsParam, method.methodName + liveDataNamePre,
                            Modifier.PUBLIC);

                    constructor
                            .addStatement(method.methodName + liveDataNamePre + " = new "
                                    + liveDataNamePre + "<>()");

                    constructor
                            .addStatement(method.methodName + mainObserverNamePre + " = new "
                                    + mainObserverNamePre + "<>(" + method.methodName +
                                    liveDataNamePre + ")");

                    observerParamName = method.methodName + mainObserverNamePre;

                    String methodFont = method.methodName.charAt(0) + "";
                    String methodRemain = method.methodName.substring(1);

                    MethodSpec.Builder liveDataGetMethod = MethodSpec
                            .methodBuilder("get" + methodFont.toUpperCase() + methodRemain
                                    + liveDataNamePre)
                            .addModifiers(PUBLIC)
                            .returns(liveDataGenericsParam.box())
                            .addStatement("return " + method.methodName + liveDataNamePre);

                    result.addMethod(liveDataGetMethod.build());
                }
            }

            StringBuilder sb = new StringBuilder();
            sb.append(modelName)
                    .append(".")
                    .append(method.methodName)
                    .append("(");

            for (BindObserverParam param : method.orderParams) {

                if (param.isAnnotationed) {
                    sb.append(param.paramName);
                    requestMethod.addStatement(observerParamName + ".addRquestParam(\""
                            + param.paramAnnotationName + "\", " + param.paramName + ")");
                } else {
                    sb.append(observerParamName);
                }

                if (method.orderParams.indexOf(param) != method.orderParams.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append(")");

            requestMethod.addStatement(sb.toString());
            result.addMethod(requestMethod.build());
        }

        result.addMethod(constructor.build());
        return result.build();
    }

    private String modelClassName;
    private String generateClassName;
    private String packageName;
    private List<BindObserverMethod> methods = new ArrayList<>();
    private ClassName binderClassName;

    public BinderSet() {

    }

    public BinderSet(Builder builder) {
        modelClassName = builder.modelClassName;
        generateClassName = builder.generateClassName;
        packageName = builder.packageName;
        methods.addAll(builder.methods);

        MODELCLASS = ClassName.get(packageName, modelClassName);

        if (packageName != null) {
            int last = packageName.lastIndexOf(".");

            packageName = packageName.substring(0, last);
            StringBuilder sb = new StringBuilder(packageName);
            sb.append(".").append("viewmodel");

            packageName = sb.toString();
        }

        binderClassName = ClassName.get(packageName, generateClassName);
    }

    public static class Builder {

        private String modelClassName;
        private String generateClassName;
        private String packageName;
        private List<BindObserverMethod> methods = new ArrayList<>();

        public Builder setModelClassName(String modelClassName) {
            this.modelClassName = modelClassName;
            return this;
        }

        public Builder setGenerateClassName(String generateClassName) {
            this.generateClassName = generateClassName;
            return this;
        }

        public Builder setPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Builder addObserverMethod(BindObserverMethod method) {
            methods.add(method);
            return this;
        }

        public BinderSet build() {
            return new BinderSet(this);
        }
    }
}
