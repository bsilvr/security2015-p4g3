package iedcs.resources;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class Http_Client {
	private static HttpClient client = HttpClientBuilder.create().build();

	public static HttpClient getHttpClient(){
		return client;

	}
}
