package iedcs.model;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

public class Http_Client {


	private static CloseableHttpClient httpclient;
	public static String URL = "http://127.0.0.1:8000/";

	public static CloseableHttpClient getHttpClient() throws KeyManagementException, NoSuchAlgorithmException{

		return httpclient;
	}

	public static void create() throws KeyManagementException, NoSuchAlgorithmException{
		String result = null;
	    SSLContext sslContext = SSLContext.getInstance("SSL");

	    // set up a TrustManager that trusts everything
	    sslContext.init(null, new TrustManager[] { new X509TrustManager() {
	                public X509Certificate[] getAcceptedIssuers() {
	                        System.out.println("getAcceptedIssuers =============");
	                        return null;
	                }

	                public void checkClientTrusted(X509Certificate[] certs,
	                                String authType) {
	                        System.out.println("checkClientTrusted =============");
	                }

	                public void checkServerTrusted(X509Certificate[] certs,
	                                String authType) {
	                        System.out.println("checkServerTrusted =============");
	                }
	    } }, new SecureRandom());
		httpclient = HttpClients.custom().setSSLSocketFactory(new SSLSocketFactory(sslContext)).build();
	}
	public static String getURL(){
		return URL;
	}

}
