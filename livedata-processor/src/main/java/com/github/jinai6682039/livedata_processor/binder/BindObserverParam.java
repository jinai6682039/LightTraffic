package com.github.jinai6682039.livedata_processor.binder;

import com.github.jinai6682039.livedata_processor.util.ProcessorUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * createTime: 2018/9/28
 * by hanxu17
 */
public class BindObserverParam {

    public String paramName;
    public String paramAnnotationName;
    public String paramTypeString;

    /**
     * example: if {@link Builder#paramTypeString} is java.lang.String
     * this value will be java.lang
     */
    public String paramImportPrefix = "";

    /**
     * example: if {@link Builder#paramTypeString} is java.lang.String
     * this value will be String
     */
    public String paramRawType = "";

    public boolean isGenericsType = false;
    public List<BindObserverParam> orderGenericsTypes = new ArrayList<>();

    public BindObserverParam() {

    }

    public BindObserverParam(Builder builder) {
        this.paramName = builder.paramName;
        this.paramAnnotationName = builder.paramAnnotationName;
        this.paramTypeString = builder.paramTypeString;
        this.paramImportPrefix = builder.paramImportPrefix;
        this.paramRawType = builder.paramRawType;
        this.orderGenericsTypes = builder.orderGenericsTypes;
        this.isGenericsType = builder.isGenericsType;
    }

    public static final class Builder {

        private String paramName = "";
        private String paramAnnotationName = "";
        private String paramTypeString = "";

        private boolean isGenericsType = false;
        private List<BindObserverParam> orderGenericsTypes = new ArrayList<>();

        /**
         * example: if {@link Builder#paramTypeString} is java.lang.String
         * this value will be java.lang
         */
        private String paramImportPrefix = "";

        /**
         * example: if {@link Builder#paramTypeString} is java.lang.String
         * this value will be String
         */
        private String paramRawType = "";

        public Builder() {
        }

        public Builder setParamName(String paramName) {
            this.paramName = paramName;
            return this;
        }

        public Builder setParamAnnotationName(String paramAnnotationName) {
            this.paramAnnotationName = paramAnnotationName;
            return this;
        }

        public Builder setParamTypeString(String paramTypeString) {
            this.paramTypeString = paramTypeString;
            return this;
        }

        public Builder setIsGenericsType() {
            this.isGenericsType = true;
            return this;
        }

        public Builder decodeParamType() {
            /**
             * find the generics type in the {@link paramTypeString}
             */
            if (paramTypeString.contains("<")) {
                String[] types = paramTypeString.split("<");
                int genericsHierarchy = types.length - 1;

                types[genericsHierarchy] = types[genericsHierarchy].replace(">", "");

                // get generice type in order
                for (int i = 0; i < genericsHierarchy; i++) {

                    orderGenericsTypes.add(new Builder()
                            .setParamTypeString(types[i + 1])
                            .setIsGenericsType()
                            .decodeParamType()
                            .build());
                }

                // get RawType
                int indexOfRawType = types[0].lastIndexOf(".");
                this.paramImportPrefix = types[0].substring(0, indexOfRawType);
                this.paramRawType = types[0].substring(indexOfRawType + 1);

            } else {
                int indexOfRawType = paramTypeString.lastIndexOf(".");
                this.paramImportPrefix = paramTypeString.substring(0, indexOfRawType);
                this.paramRawType = paramTypeString.substring(indexOfRawType + 1);
            }
            return this;
        }

        public BindObserverParam build() {
            return new BindObserverParam(this);
        }
    }
}
