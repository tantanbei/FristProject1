package com.whoplate.paipable.http;

import com.whoplate.paipable.util.Closer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

public class SSLSocketFactoryEx extends SSLSocketFactory {

    //http ssl , set no verify
    public static final TrustManager[] TRUST_ALL_CERTS_MANAGER;

    static {
        TRUST_ALL_CERTS_MANAGER = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

            }

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }};
    }

    private SSLContext m_ctx;
    private String[] m_ciphers;
    private String[] m_protocols;

    public SSLSocketFactoryEx() throws NoSuchAlgorithmException, KeyManagementException {
        initSSLSocketFactoryEx(null, null, null);
    }

    public SSLSocketFactoryEx(KeyManager[] km, SecureRandom random) throws NoSuchAlgorithmException, KeyManagementException {
        initSSLSocketFactoryEx(km, TRUST_ALL_CERTS_MANAGER, random);
    }

    public SSLSocketFactoryEx(SSLContext ctx) throws NoSuchAlgorithmException, KeyManagementException {
        initSSLSocketFactoryEx(ctx);
    }

    public String[] getDefaultCipherSuites() {
        return m_ciphers;
    }

    public String[] getSupportedCipherSuites() {
        return m_ciphers;
    }

    public String[] getDefaultProtocols() {
        return m_protocols;
    }

    public String[] getSupportedProtocols() {
        return m_protocols;
    }

    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        SSLSocketFactory factory = m_ctx.getSocketFactory();
        SSLSocket ss = (SSLSocket) factory.createSocket(s, host, port, autoClose);

        ss.setEnabledProtocols(m_protocols);
        ss.setEnabledCipherSuites(m_ciphers);

        return ss;
    }

    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        SSLSocketFactory factory = m_ctx.getSocketFactory();
        SSLSocket ss = (SSLSocket) factory.createSocket(address, port, localAddress, localPort);

        ss.setEnabledProtocols(m_protocols);
        ss.setEnabledCipherSuites(m_ciphers);

        return ss;
    }

    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
        SSLSocketFactory factory = m_ctx.getSocketFactory();
        SSLSocket ss = (SSLSocket) factory.createSocket(host, port, localHost, localPort);

        ss.setEnabledProtocols(m_protocols);
        ss.setEnabledCipherSuites(m_ciphers);

        return ss;
    }

    public Socket createSocket(InetAddress host, int port) throws IOException {
        SSLSocketFactory factory = m_ctx.getSocketFactory();
        SSLSocket ss = (SSLSocket) factory.createSocket(host, port);

        ss.setEnabledProtocols(m_protocols);
        ss.setEnabledCipherSuites(m_ciphers);

        return ss;
    }

    public Socket createSocket(String host, int port) throws IOException {
        SSLSocketFactory factory = m_ctx.getSocketFactory();
        SSLSocket ss = (SSLSocket) factory.createSocket(host, port);

        ss.setEnabledProtocols(m_protocols);
        ss.setEnabledCipherSuites(m_ciphers);

        return ss;
    }

    private void initSSLSocketFactoryEx(KeyManager[] km, TrustManager[] tm, SecureRandom random)
            throws NoSuchAlgorithmException, KeyManagementException {
        m_ctx = SSLContext.getInstance("TLS");
        m_ctx.init(km, tm, random);

        m_protocols = GetProtocolList();
        m_ciphers = GetCipherList();
    }

    private void initSSLSocketFactoryEx(SSLContext ctx)
            throws NoSuchAlgorithmException, KeyManagementException {
        m_ctx = ctx;

        m_protocols = GetProtocolList();
        m_ciphers = GetCipherList();
    }

    protected String[] GetProtocolList() {
        String[] preferredProtocols = {"TLSv1", "TLSv1.1", "TLSv1.2", "TLSv1.3"};
        String[] availableProtocols = null;

        SSLSocket socket = null;

        try {
            SSLSocketFactory factory = m_ctx.getSocketFactory();
            socket = (SSLSocket) factory.createSocket();

            availableProtocols = socket.getSupportedProtocols();
            Arrays.sort(availableProtocols);
        } catch (Exception e) {
            return new String[]{"TLSv1"};
        } finally {
            if (socket != null) {
                Closer.Close(socket);
            }
        }

        List<String> aa = new ArrayList<>();
        for (String preferredProtocol : preferredProtocols) {
            int idx = Arrays.binarySearch(availableProtocols, preferredProtocol);
            if (idx >= 0)
                aa.add(preferredProtocol);
        }

        return aa.toArray(new String[aa.size()]);
    }

    protected String[] GetCipherList() {
        String[] preferredCiphers = {

                // *_CHACHA20_POLY1305 are 3x to 4x faster than existing cipher suites.
                //   http://googleonlinesecurity.blogspot.com/2014/04/speeding-up-and-strengthening-https.html
                // Use them if available. Normative names can be found at (TLS spec depends on IPSec spec):
                //   http://tools.ietf.org/html/draft-nir-ipsecme-chacha20-poly1305-01
                //   http://tools.ietf.org/html/draft-mavrogiannopoulos-chacha-tls-02
                "TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305",
                "TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305",
                "TLS_ECDHE_ECDSA_WITH_CHACHA20_SHA",
                "TLS_ECDHE_RSA_WITH_CHACHA20_SHA",

                "TLS_DHE_RSA_WITH_CHACHA20_POLY1305",
                "TLS_RSA_WITH_CHACHA20_POLY1305",
                "TLS_DHE_RSA_WITH_CHACHA20_SHA",
                "TLS_RSA_WITH_CHACHA20_SHA",

                // Done with bleeding edge, back to TLS v1.2 and below
//                "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384",
//                "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384",
                "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256",
                "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",

//                "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384",
//                "TLS_DHE_DSS_WITH_AES_256_GCM_SHA384",
                "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
                "TLS_DHE_DSS_WITH_AES_128_GCM_SHA256",

                // TLS v1.0 (with some SSLv3 interop)
//                "TLS_DHE_RSA_WITH_AES_256_CBC_SHA384",
//                "TLS_DHE_DSS_WITH_AES_256_CBC_SHA256",
                "TLS_DHE_RSA_WITH_AES_128_CBC_SHA",
                "TLS_DHE_DSS_WITH_AES_128_CBC_SHA",

                "TLS_DHE_RSA_WITH_3DES_EDE_CBC_SHA",
                "TLS_DHE_DSS_WITH_3DES_EDE_CBC_SHA",
                "SSL_DH_RSA_WITH_3DES_EDE_CBC_SHA",
                "SSL_DH_DSS_WITH_3DES_EDE_CBC_SHA",

                // RSA key transport sucks, but they are needed as a fallback.
                // For example, microsoft.com fails under all versions of TLS
                // if they are not included. If only TLS 1.0 is available at
                // the client, then google.com will fail too. TLS v1.3 is
                // trying to deprecate them, so it will be interesteng to see
                // what happens.
//                "TLS_RSA_WITH_AES_256_CBC_SHA256",
//                "TLS_RSA_WITH_AES_256_CBC_SHA",
                "TLS_RSA_WITH_AES_128_CBC_SHA256",
                "TLS_RSA_WITH_AES_128_CBC_SHA",
                "TLS_RSA_WITH_RC4_128_MD5",
                "TLS_RSA_WITH_RC4_128_SHA"

        };

        String[] availableCiphers;

        try {
            SSLSocketFactory factory = m_ctx.getSocketFactory();
            availableCiphers = factory.getSupportedCipherSuites();
            Arrays.sort(availableCiphers);
        } catch (Exception e) {
            return new String[]{
                    "TLS_DHE_DSS_WITH_AES_128_CBC_SHA",
                    "TLS_DHE_DSS_WITH_AES_256_CBC_SHA",
                    "TLS_DHE_RSA_WITH_AES_128_CBC_SHA",
                    "TLS_DHE_RSA_WITH_AES_256_CBC_SHA",
                    "TLS_RSA_WITH_AES_256_CBC_SHA256",
                    "TLS_RSA_WITH_AES_256_CBC_SHA",
                    "TLS_RSA_WITH_AES_128_CBC_SHA256",
                    "TLS_RSA_WITH_AES_128_CBC_SHA",
                    "TLS_RSA_WITH_RC4_128_MD5",
                    "TLS_RSA_WITH_RC4_128_SHA"
            };
        }

        List<String> aa = new ArrayList<>();
        for (String preferredCipher : preferredCiphers) {
            int idx = Arrays.binarySearch(availableCiphers, preferredCipher);
            if (idx >= 0)
                aa.add(preferredCipher);
        }

//        aa.add("TLS_EMPTY_RENEGOTIATION_INFO_SCSV");

        return aa.toArray(new String[aa.size()]);
    }
}