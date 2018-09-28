package com.github.jinai6682039.livedata_processor.util;

import com.github.jinai6682039.livedata_annotation.ObserverParam;

import java.lang.annotation.Annotation;

import javax.lang.model.element.ExecutableElement;

/**
 * createTime: 2018/9/27
 * by hanxu17
 */
public class ProcessorUtil {
    public static String getAnnotationObserverParam(ExecutableElement element,
                                                           Class<? extends Annotation> clazz) {

        if (clazz.getName().equals(ObserverParam.class.getName())) {
            return element.getAnnotation(ObserverParam.class).value();
        } else {
            return null;
        }
    }

    public static boolean isTStringEmpty(String text) {
        return text == null || text.equals("");
    }
}
