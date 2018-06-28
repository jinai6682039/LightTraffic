package com.github.alexhanxs.lighttraffic.base;

import com.github.alexhanxs.lighttraffic.base.http.HeaderInterceptor;
import com.github.alexhanxs.lighttraffic.base.util.HttpsUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Alexhanxs on 2018/6/27.
 */

public class RetrofitHelper {

    //连接超时时间，默认格式：秒
    private static final int DEFAULT_TIMEOUT = 10;

    private volatile static RetrofitHelper sInstance;

    private Gson mGson;

    //是否debug环境
    private boolean isDebugable = true;

    private HashMap<String, Object> mServiceMap;
    private LinkedList<String> mServiceUrlQueue;

    private HeaderInterceptor mHeaderInterceptor;

    private RetrofitHelper() {
        mGson = new GsonBuilder()
                .setLenient()
                .create();

        mServiceMap = new LinkedHashMap<String, Object>();
        mServiceUrlQueue = new LinkedList<>();
    }

    public static RetrofitHelper getInstance() {
        if (sInstance == null) {
            synchronized (RetrofitHelper.class) {
                if (sInstance == null) {
                    sInstance = new RetrofitHelper();
                }
            }
        }
        return sInstance;
    }

    private OkHttpClient getClient(Map<String, String> headers) {

        mHeaderInterceptor = new HeaderInterceptor(headers);
        HttpsUtil.SSLParams sslParams = HttpsUtil.getSslSocketFactory();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(mHeaderInterceptor)
                .sslSocketFactory(sslParams.ssLSocketFactory, sslParams.trustManager)
                .build();

        return client;
    }

    public <T> T getService(String baseUrl, Class<T> service, Map<String, String> headers) {
        Object target = getServiceMap().get(baseUrl);

        if (target != null && target.getClass().isAssignableFrom(service)) {
            if (mHeaderInterceptor != null) {
                mHeaderInterceptor.setHeaders(headers);
            }
            moveToFirst(baseUrl);
            return (T) target;
        } else {
            return createService(baseUrl, service, headers);
        }
    }

    public <T> T createService(String baseUrl, Class<T> service, Map<String, String> headers) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getClient(headers))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .build();

        T t = retrofit.create(service);
        putService(t, baseUrl);

        return t;
    }

    private void moveToFirst(String baseUrl){

        for (int i = 0; i < getServiceUrlQueue().size(); i++) {
            if (mServiceUrlQueue.get(i).equals(baseUrl)) {
                if (i != 0) {   //如果不在第一个，提升到第一个
                    mServiceUrlQueue.remove(i);
                    mServiceUrlQueue.push(baseUrl);
                }
                break;
            }
        }
    }

    private <T> void putService(T t, String baseUrl){

        getServiceMap().put(baseUrl, t);
        getServiceUrlQueue().push(baseUrl);

        if (mServiceUrlQueue.size() > 5) {
            String removeUrl = mServiceUrlQueue.pollLast(); //移除队列中的最后一个url
            mServiceMap.remove(removeUrl);  //移除url对应的service
        }
    }

    private HashMap<String, Object> getServiceMap(){
        if (mServiceMap == null) {
            mServiceMap = new LinkedHashMap<String, Object>();
        }

        return mServiceMap;
    }

    private LinkedList<String> getServiceUrlQueue(){
        if (mServiceUrlQueue == null) {
            mServiceUrlQueue = new LinkedList<>();
        }
        return mServiceUrlQueue;
    }
}
