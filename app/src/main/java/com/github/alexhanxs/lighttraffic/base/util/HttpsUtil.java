package com.github.alexhanxs.lighttraffic.base.util;

import android.util.Log;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Alexhanxs on 2018/6/28.
 */

public class HttpsUtil {
    public static class SSLParams {
        public SSLSocketFactory ssLSocketFactory;
        public X509TrustManager trustManager;
    }

    public static SSLParams getSslSocketFactory() {
        SSLParams sslParams = new SSLParams();

        X509TrustManager trustManager = getTrustManager();
        sslParams.ssLSocketFactory = getSSLSocketFactory(trustManager);
        sslParams.trustManager = trustManager;
        return sslParams;
    }


    /**
     * set SSLSocketFactory
     * {@link HostnameVerifier}
     */
    protected static SSLSocketFactory getSSLSocketFactory(X509TrustManager trustManager) {

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            Log.e("", "", e);
        }

        return null;
    }

    protected static X509TrustManager getTrustManager() {
        return new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                //
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                //
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{
                        //
                };
            }
        };
    }


    /**
     * set HostnameVerifier
     * {@link HostnameVerifier}
     */
    protected static HostnameVerifier getHostnameVerifier() {

        return new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }
}
