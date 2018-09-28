package com.github.jinai6682039.livedata_processor.binder;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * createTime: 2018/9/27
 * by hanxu17
 */
public class BinderSet {

    public static final String LIGHT_TRAFFICE_BASE_PREFIX = "com.github.alexhanxs.lighttraffic.base";
    public static final String LIGHT_TRAFFICE_TYPE_LIVEDATA = "com.github.alexhanxs.lighttraffic.base";
    public static final String LIGHT_TRAFFICE_WRAP_LIVEDATA = "com.github.alexhanxs.lighttraffic.base";
    public static final String LIGHT_TRAFFICE_MAINTHREADOBSERVER = "com.github.alexhanxs.lighttraffic.base";
    public static final String LIGHT_TRAFFICE_BASEVIEWMODEL = "com.github.alexhanxs.lighttraffic.base";

    public static final ClassName LIST = ClassName.get(List.class);


    public JavaFile generateJava(int sdk) {
        return JavaFile.builder("test", generateType(sdk))
                .addFileComment("Generated code from PermissionAllocation. Do not modify!")
                .build();
    }

    public TypeSpec generateType(int sdk) {

        TypeSpec.Builder result = TypeSpec.classBuilder("test")
                .addModifiers(PUBLIC);

//        if (isFinal) {
//            result.addModifiers(FINAL);
//        }
//
//        if (parentBinding != null) {
//            result.superclass(parentBinding.binderClassName);
//        } else {
//            result.addSuperinterface(PERMISSION_ALLOCATER);
//        }
//
//        if (isActivity || isFragment) {
//            result.addField(targetTypeName, "target", PRIVATE);
//        }
//
//        if (isActivity || isFragment) {
//            result.addMethod(createBinderConstructorForActivity());
//        }
//
//        result.addMethod(initAllocaterMethod());
//
//        for (MethodBinder binder : permissionAlloctionMethods) {
//            result.addMethod(createPermissionAllocaterMethod(binder));
//        }
//
//        for (MethodBinder binder : permissionAlloctionDenyMethods) {
//            result.addMethod(createPermissionAllocterDenyMethod(binder));
//        }
        return result.build();
    }

}
