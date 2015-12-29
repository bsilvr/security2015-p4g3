package iedcs.model;

import java.io.FileInputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

public class Http_Client {


	// private static HttpClient client = HttpClientBuilder.create().build();

	private static HttpClient client = createHttpClient_AcceptsUntrustedCerts();
	private static HttpClient client2 = createHttpClient_AcceptsUntrustedCerts();
	public static String URL = "https://localhost:8080/";

	public static HttpClient getHttpClient(){
		return client;
	}
	public static HttpClient getHttpClient2(){
		return client2;
	}
	public static String getURL(){
		return URL;
	}

	@SuppressWarnings("deprecation")
	private static HttpClient createHttpClient_AcceptsUntrustedCerts() {

	    String pathToKeyStore = "mykeystore.keystore";

		System.out.println("a");

		try {
			FileInputStream fin = new FileInputStream(pathToKeyStore);
	        KeyStore keystore  = KeyStore.getInstance("JKS");
	        keystore.load(fin, "ebookwebstore".toCharArray());

	        TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

				factory.init(keystore);

	        TrustManager[] tm = factory.getTrustManagers();

	        KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(
	                KeyManagerFactory.getDefaultAlgorithm());
	        kmfactory.init(keystore, "ebookwebstore".toCharArray());
	        KeyManager[] km = kmfactory.getKeyManagers();

	        SSLContext sslcontext = SSLContext.getInstance("SSL");
	        sslcontext.init(km, tm, null);

	        // set up a TrustManager that trusts everything
	        sslcontext.init(km, tm, new SecureRandom());

	        HttpClientBuilder builder = HttpClientBuilder.create();
	        SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslcontext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	        builder.setSSLSocketFactory(sslConnectionFactory);

	        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
	                .register("https", sslConnectionFactory)
	                .build();

	        HttpClientConnectionManager ccm = new BasicHttpClientConnectionManager(registry);

	        builder.setConnectionManager(ccm);
	        HttpClient client = builder.build();
	        return client;


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    /*// setup a Trust Strategy that allows all certificates.
	    //
	    SSLContext sslContext = null;
		try {
			sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
			    public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			        return true;
			    }
			}).build();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    b.setSslcontext( sslContext);

	    // don't check Hostnames, either.
	    //      -- use SSLConnectionSocketFactory.getDefaultHostnameVerifier(), if you don't want to weaken
	    HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

	    // here's the special part:
	    //      -- need to create an SSL Socket Factory, to use our weakened "trust strategy";
	    //      -- and create a Registry, to register it.
	    //
	    SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
	    Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
	            .register("http", PlainConnectionSocketFactory.getSocketFactory())
	            .register("https", sslSocketFactory)
	            .build();

	    // now, we create connection-manager using our Registry.
	    //      -- allows multi-threaded use
	    PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager( socketFactoryRegistry);
	    b.setConnectionManager( connMgr);

	    // finally, build the HttpClient;
	    //      -- done!*/

	    return client;
	}
}
