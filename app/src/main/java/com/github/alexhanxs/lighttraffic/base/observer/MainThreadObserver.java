package com.github.alexhanxs.lighttraffic.base.observer;

import com.github.alexhanxs.lighttraffic.base.livedata.WrapLiveData;
import com.github.alexhanxs.lighttraffic.base.exception.RequestException;
import com.github.alexhanxs.lighttraffic.base.livedata.TypeLiveData;
import com.github.alexhanxs.lighttraffic.base.util.NetUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

import static com.github.alexhanxs.lighttraffic.base.livedata.WrapLiveData.*;

/**
 * 通常情况下有四种状态要修改View
 * Request_Error
 * Request_Loading
 * Request_Finish
 * Request_Success
 * Created by Alexhanxs on 2018/6/27.
 */

public class MainThreadObserver<T> implements Observer<T> {

    public Disposable disposable;

    private TypeLiveData<WrapLiveData<T>> dataTypeLiveData;

    public MainThreadObserver(TypeLiveData<WrapLiveData<T>> dataTypeLiveData) {
        this.dataTypeLiveData = dataTypeLiveData;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.disposable = d;

        if (NetUtil.isConnected()) {
            dataTypeLiveData.setValue(makeLoadingLiveData(LOADING));

//            Log.d(MainThreadObserver.class.getSimpleName())
        } else {
            dataTypeLiveData.setValue(makeErrorLiveData(-10012,
                    "网络状态不好，请稍后再试"));

            disposable.dispose();
        }
    }

    @Override
    public void onNext(T t) {
        dataTypeLiveData.setValue(makeLoadingLiveData(LOAD_SUCCESS));

        dataTypeLiveData.setValue(new WrapLiveData<>(t));

        dataTypeLiveData.setValue(makeFinishLiveData(LOAD_FINISH_SUCCESS));
    }

    @Override
    public void onError(Throwable e) {
        dataTypeLiveData.setValue(makeLoadingLiveData(LOAD_ERROR));

        if (e instanceof NullPointerException) {
            onNext(null);
        } else if (e instanceof SocketTimeoutException) {
            //请求超时
            dataTypeLiveData.setValue(makeErrorLiveData(-10010,
                    "请求超时，请稍后重试"));
        } else if (e instanceof UnknownHostException || e instanceof HttpException) {
            dataTypeLiveData.setValue(makeErrorLiveData(-10011,
                    "系统走神了，请稍后重试"));
        } else if (e instanceof ConnectException) {     //链接出错
            dataTypeLiveData.setValue(makeErrorLiveData(-10012,
                    "网络状态不好，请稍后再试"));
        } else if (e instanceof RequestException) {
            RequestException requestException = (RequestException) e;
            dataTypeLiveData.setValue(makeErrorLiveData(requestException));
        } else {
            RequestException requestException = new RequestException(e.getMessage(), -10013);
            dataTypeLiveData.setValue(makeErrorLiveData(requestException));
        }

        dataTypeLiveData.setValue(makeFinishLiveData(LOAD_FINISH_ERROR));
    }

    @Override
    public void onComplete() {

    }
}
