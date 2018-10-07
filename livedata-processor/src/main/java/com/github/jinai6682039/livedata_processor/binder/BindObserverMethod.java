package com.github.jinai6682039.livedata_processor.binder;

import java.util.ArrayList;
import java.util.List;

/**
 * createTime: 2018/9/28
 * by hanxu17
 */
public class BindObserverMethod {

    public static String MODIFY_PUBLIC = "public";
    public static String MODIFY_PRIVATE = "private";
    public static String MODIFY_PROTECTED = "protected";
    public static String MODIFY_FINAL = "final";
    public static String MODIFY_STATIC = "static";

    public String methodName;
    public List<BindObserverParam> orderParams = new ArrayList<>();
    public boolean isFinal;
    public boolean isStatic;
    public boolean isPublic;
    public boolean isProtected;
    public boolean isPrivate;

    public BindObserverMethod() {

    }

    public BindObserverMethod(Builder builder) {
        methodName = builder.methodName;
        orderParams.addAll(builder.orderParams);
        isFinal = builder.isFinal;
        isStatic = builder.isFinal;
        isPublic = builder.isPublic;
        isProtected = builder.isProtected;
        isPrivate = builder.isPrivate;
    }

    public static final class Builder {

        private String methodName;
        private List<BindObserverParam> orderParams = new ArrayList<>();
        private boolean isFinal;
        private boolean isStatic;
        private boolean isPublic;
        private boolean isProtected;
        private boolean isPrivate;

        public Builder setMethodName(String methodName) {
            this.methodName = methodName;
            return this;
        }

        public Builder addOrderObserverParams(BindObserverParam param) {
            this.orderParams.add(param);
            return this;
        }

        public Builder setFinal(boolean isFinal) {
            this.isFinal = isFinal;
            return this;
        }

        public Builder setPublic(boolean isPublic) {
            this.isPublic = isPublic;
            return this;
        }

        public Builder setProtected(boolean isProtected) {
            this.isProtected = isProtected;
            return this;
        }

        public Builder setPrivate(boolean isPrivate) {
            this.isPrivate = isPrivate;
            return this;
        }

        public Builder setStatic(boolean isStatic) {
            this.isStatic = isStatic;
            return this;
        }

        public Builder addModify(String modifiy) {
            if (modifiy.equals(MODIFY_PRIVATE)) {
                isPrivate = true;
            } else if (modifiy.equals(MODIFY_PROTECTED)) {
                isProtected = true;
            } else if (modifiy.equals(MODIFY_PUBLIC)) {
                isPublic = true;
            } else if (modifiy.equals(MODIFY_FINAL)) {
                isFinal = true;
            } else if (modifiy.equals(MODIFY_STATIC)) {
                isStatic = true;
            }
            return this;
        }

        public BindObserverMethod build() {
            return new BindObserverMethod(this);
        }
    }
}
