package com.github.jinai6682039.livedata_processor.exception;

/**
 * createTime: 2018/10/6
 * by hanxu17
 */
public class ModelClassCantFindException extends RuntimeException {
    public ModelClassCantFindException(String e) {
        super("Unable to find model class " + e);
    }
}
