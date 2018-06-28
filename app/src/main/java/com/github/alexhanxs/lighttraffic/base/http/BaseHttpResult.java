package com.github.alexhanxs.lighttraffic.base.http;

/**
 * Created by Alexhanxs on 2018/6/27.
 */

public class BaseHttpResult<T> {

    public int code;
    public String msg;
    public T data;
}
