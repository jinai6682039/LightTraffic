package com.github.alexhanxs.lighttraffic.base;

import com.github.alexhanxs.lighttraffic.base.http.BaseHttpResult;
import com.github.alexhanxs.lighttraffic.base.http.HttpResultFunc;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alexhanxs on 2018/6/27.
 */

public class SubscribeHelper {

    protected <T> void doSubscribe(Observable<BaseHttpResult<T>> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new HttpResultFunc<T>())
                .subscribe(observer);
    }

    protected <T> void doSimpleSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
