package com.github.alexhanxs.lighttraffic.base.exception;

/**
 * Created by Alexhanxs on 2018/6/27.
 */

public class RequestException extends RuntimeException {

    private int mErrorCode;

    public RequestException(String message, int mErrorCode) {
        super(message);
        this.mErrorCode = mErrorCode;
    }

    public int getErrorCode() {
        return mErrorCode;
    }
}
