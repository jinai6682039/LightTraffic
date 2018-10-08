package com.github.alexhanxs.lighttraffic.base.livedata;

import com.github.alexhanxs.lighttraffic.base.exception.RequestException;

import java.util.HashMap;

/**
 * Created by Alexhanxs on 2018/6/27.
 */

public class WrapLiveData<T> {

    public final static int TYPE_DATA_ERROR = 0x01;
    public final static int TYPE_DATA_LOADING = 0x02;
    public final static int TYPE_DATA_SUCCESS = 0x03;
    public final static int TYPE_DATA_FINISH = 0x04;

    public final static boolean LOADING = true;
    public final static boolean LOAD_SUCCESS = false;
    public final static boolean LOAD_ERROR = false;

    public final static boolean LOAD_FINISH_SUCCESS = true;
    public final static boolean LOAD_FINISH_ERROR = false;

    public static WrapLiveData finishLiveData = new WrapLiveData(TYPE_DATA_FINISH);

    private HashMap<String, Object> requestParams = new HashMap<>();

    public T data;
    public int type = 0;
    public boolean showLoading;
    public RequestException requestException;

    public WrapLiveData(int type) {
        this.type = type;
    }

    public WrapLiveData(T data) {
        this.type = TYPE_DATA_SUCCESS;
        this.data = data;
    }

    public WrapLiveData(int type, boolean showLoading) {
        this.type = type;
        this.showLoading = showLoading;
    }

    public WrapLiveData(int type, int errorCode, String errorMessage) {
        this.type = type;
        requestException = new RequestException(errorMessage, errorCode);
    }

    public static WrapLiveData makeErrorLiveData(int errorCode, String errorMessage) {
        return new WrapLiveData(TYPE_DATA_ERROR, errorCode, errorMessage);
    }

    public static WrapLiveData makeErrorLiveData(RequestException requestException) {
        return new WrapLiveData(TYPE_DATA_ERROR, requestException.getErrorCode(), requestException.getMessage());
    }

    public static WrapLiveData makeLoadingLiveData(boolean showLoading) {
        return new WrapLiveData(TYPE_DATA_LOADING, showLoading);
    }

    public static WrapLiveData makeFinishLiveData(boolean loadSuccess) {
        return new WrapLiveData(TYPE_DATA_FINISH, loadSuccess);
    }

    public void setRequestParams(HashMap<String, Object> requestParams) {
        this.requestParams = requestParams;
    }

    public Object getRequestParam(String key) {
        return requestParams.get(key);
    }
}
