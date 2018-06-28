package com.github.alexhanxs.lighttraffic.base.http;

import android.text.TextUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by Alexhanxs on 2018/6/27.
 */

public class HeaderInterceptor implements Interceptor {

    private Map<String, String> headers;

    public HeaderInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        Request request = chain.request();

        Map<String, String> requestHeaders = new HashMap<>(headers);

        StringBuilder sign = new StringBuilder();
        if (requestHeaders.containsKey("User-Id")) {
            sign.append(requestHeaders.get("User-Id"));
        }
        if (requestHeaders.containsKey("Token")) {
            sign.append(requestHeaders.get("Token"));
        }
        if (!requestHeaders.containsKey("Timestamp")) {
            requestHeaders.put("Timestamp", String.valueOf(System.currentTimeMillis()));
        }

        sign.append(requestHeaders.get("Timestamp"));

        if (!TextUtils.isEmpty(request.method())) {
            sign.append(request.method());
        }

        StringBuilder requestPath = new StringBuilder();
        for (int i = 0; i < request.url().pathSegments().size(); i++) {
            requestPath.append("/");
            requestPath.append(request.url().pathSegments().get(i));
        }
        if (!TextUtils.isEmpty(request.url().query())) {
            requestPath.append("?");
            requestPath.append(request.url().encodedQuery());
        }


        sign.append(requestPath);

        if (request.body() != null) {
            sign.append(bodyToString(request));
        }

        if (requestHeaders.size() > 0) {
            Set<String> keys = requestHeaders.keySet();
            for (String headerKey : keys) {
                builder.addHeader(headerKey, requestHeaders.get(headerKey)).build();
            }
        }

        return chain.proceed(builder.build());
    }

    /**
     * 将requestBody转换为String
     *
     * @param request
     * @return
     */
    private String bodyToString(Request request) {
        if (request == null) {
            return "";
        }
        Buffer buffer = new Buffer();
        try {
            request.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (IOException e) {
            return "";
        }
    }
}
