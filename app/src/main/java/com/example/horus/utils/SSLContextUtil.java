package com.example.horus.utils;

import android.util.Log;

import com.example.baselib.utils.LogUtil;
import com.example.horus.BuildConfig;
import com.example.horus.app.MyApp;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashSet;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;



/**
 * Created by lognyun on 2017/6/29.
 * SSL
 */

public class SSLContextUtil {

    private static String TAG = SSLContextUtil.class.getSimpleName();

    private static HashSet<String> hostSet = new HashSet<>();

    static {
            hostSet.add("www.zerophil.com");
            hostSet.add("zerophil.com");
    }

    /**
     * 拿到https证书, SSLContext
     * @return
     */
    public static SSLContext getSSLContext() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            InputStream inputStream = MyApp.getInstance().getAssets().open("cert.crt");

            CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
            Certificate cer = cerFactory.generateCertificate(inputStream);

            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(null, null);
            keyStore.setCertificateEntry("trust", cer);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, null);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sslContext;
    }

//    /**
//     * 如果不需要https证书
//     * @return
//     */
//    public static SSLContext getDefaultSSLContext() {
//        SSLContext sslContext = null;
//        try {
//            sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, new TrustManager[]{victimizedManager}, new SecureRandom());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return sslContext;
//    }

    public static X509TrustManager getX509TrustManager() {
        return trustManagers;
    }

    private static X509TrustManager trustManagers = new X509TrustManager() {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            LogUtil.d("X509TrustManager","X509TrustManager authType:" + authType);
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            LogUtil.d("X509TrustManager","X509TrustManager authType:" + authType);
            if (chain != null)

                for (X509Certificate cert : chain) {
//                    try {
//                        // Make sure that it hasn't expired.
//                        cert.checkValidity();
//                    } catch (CertificateExpiredException e) {
//                        //此异常是证书已经过期异常，在手机调到证书生效时间之后会捕捉到此异常
//                        LogUtil.d(TAG, "checkServerTrusted: CertificateExpiredException:" + e.getLocalizedMessage());
//                    } catch (CertificateNotYetValidException e) {
//                        //此异常是证书未生效异常，在手机调到证书生效时间之前会捕捉到此异常
//                        LogUtil.d(TAG, "checkServerTrusted: CertificateNotYetValidException:" + e.getLocalizedMessage());
//                    }

                    try {
                        // Verify the certificate's public key chain.
                        cert.verify(cert.getPublicKey());
                    } catch (Exception e) {
                        Log.i(TAG, "checkServerTrusted: CertificateExpiredException:" + e.getLocalizedMessage());
                        throw new CertificateException(e.getLocalizedMessage());
                    }
//                    catch (CertificateNotYetValidException e) {
//                        //此异常是证书未生效异常，在手机调到证书生效时间之前会捕捉到此异常
//                        LogUtil.d(TAG, "checkServerTrusted: CertificateNotYetValidException:" + e.getLocalizedMessage());
//                    } catch (CertificateException ex) {
//                        //其他异常正常报错
//                        LogUtil.d(TAG, "Throw checkClientTrusted: " + ex.getLocalizedMessage());
//                        throw ex;
//                    } catch (NoSuchAlgorithmException e) {
//                        LogUtil.d(TAG, "checkServerTrusted: NoSuchAlgorithmException:" + e.getLocalizedMessage());
//                    } catch (InvalidKeyException e) {
//                        LogUtil.d(TAG, "checkServerTrusted: InvalidKeyException:" + e.getLocalizedMessage());
//                    } catch (NoSuchProviderException e) {
//                        LogUtil.d(TAG, "checkServerTrusted: NoSuchProviderException:" + e.getLocalizedMessage());
//                    } catch (SignatureException e) {
//                        LogUtil.d(TAG, "checkServerTrusted: SignatureException:" + e.getMessage());
//                    }
                }

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };

    public static final HostnameVerifier HOSTNAME_VERIFIER = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            Log.i(TAG,"hostname:" + hostname);
            return hostSet.contains(hostname);
        }
    };


//    public static X509TrustManager trustManager =
//            new X509TrustManager() {
//
//                public X509Certificate[] getAcceptedIssuers() {
//
//                    X509Certificate[] myTrustedAnchors = new X509Certificate[0];
//
//                    return myTrustedAnchors;
//                }
//
//                @Override
//                public void checkClientTrusted(X509Certificate[] certs, String authType) {
//                }
//
//                @Override
//                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//                    if(chain == null || chain.length == 0)throw new IllegalArgumentException("Certificate is null or empty");
//                    if(authType == null || authType.length() == 0) throw new IllegalArgumentException("Authtype is null or empty");
//                    if(!authType.equalsIgnoreCase("ECDHE_RSA") &&
//                            !authType.equalsIgnoreCase("ECDHE_ECDSA") &&
//                            !authType.equalsIgnoreCase("RSA") &&
//                            !authType.equalsIgnoreCase("ECDSA")) throw new CertificateException("Certificate is not trust");
//                    try {
//                        chain[0].checkValidity();
//                    } catch (Exception e) {
//                        throw new CertificateException("Certificate is not valid or trusted");
//                    }
//                }
//
//    };
}
