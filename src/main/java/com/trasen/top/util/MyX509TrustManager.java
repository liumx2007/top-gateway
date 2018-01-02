package com.trasen.top.util;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by zhangxiahui on 17/6/14.
 */
public class MyX509TrustManager implements X509TrustManager{
    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {

    }
    @Override
    public void checkServerTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {

    }
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
