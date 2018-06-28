package com.github.alexhanxs.lighttraffic.base.http;


import com.github.alexhanxs.lighttraffic.base.exception.RequestException;

import io.reactivex.functions.Function;

/**
 * Created by Alexhanxs on 2018/6/27.
 */

public class HttpResultFunc<T> implements Function<BaseHttpResult<T>, T> {

    @Override
    public T apply(BaseHttpResult<T> tBaseHttpResult) throws Exception {
        if (tBaseHttpResult.code != 200) {
            throw new RequestException(tBaseHttpResult.msg, tBaseHttpResult.code);
        } else {
            return tBaseHttpResult.data;
        }
    }
}
