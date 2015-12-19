package iedcs.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import sun.security.pkcs11.SunPKCS11;
import java.security.*;
import java.security.Provider;
import java.security.Security;
import java.security.KeyStore;
import java.io.*;
import java.util.Enumeration;
import java.security.Signature;
import java.util.Base64;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import iedcs.MainApp;
import iedcs.model.Http_Client;
import iedcs.model.KeyManager;
import iedcs.model.MyCallbackHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

	private static String cookies;

    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginBtn;
    @FXML
    private Button loginCC;

    // Reference to the main application.
    private MainApp mainApp;


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public LoginController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void sendDeviceKey() throws ClientProtocolException, IOException {

		System.out.println("iashdiuasbidbasibe");

    	String url = Http_Client.getURL() + "users/register_device/";
		HttpPost post = new HttpPost(url);
		String cookie = LoginController.getCookies();

		post.addHeader("Referer", Http_Client.getURL());

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("user", email.getText()));
		urlParameters.add(new BasicNameValuePair("device_key", KeyManager.getDeviceKey()));
		urlParameters.add(new BasicNameValuePair("csrfmiddlewaretoken", cookie.substring(cookie.indexOf("=")+1,cookie.length())));

		post.setEntity(new UrlEncodedFormEntity(urlParameters));

		HttpResponse response = Http_Client.getHttpClient().execute(post);
		BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

		StringBuffer userBooks = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			userBooks.append(line);
		}

		System.out.println(response);
		HttpEntity entity = response.getEntity();
		if(entity == null){
		}
		else{
			EntityUtils.consume(entity);
		}
		System.out.println("a");



    }

    /**
     * Called when the user clicks on the read button.
     * @throws IOException
     * @throws ClientProtocolException
     */
    @FXML
    private void handleLogin() throws ClientProtocolException, IOException {

    	KeyManager.createDeviveKey();
    	String url = Http_Client.getURL() + "users/login/";
    	String cookie="";

		HttpPost post = new HttpPost(url);

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("email", email.getText()));
		urlParameters.add(new BasicNameValuePair("password", password.getText()));

		post.setEntity(new UrlEncodedFormEntity(urlParameters));

		HttpResponse response = Http_Client.getHttpClient().execute(post);

		Header[] cookies =  response.getHeaders("Set-Cookie");


		BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

		StringBuffer userBooks = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			userBooks.append(line);
		}

		if(response.getStatusLine().getStatusCode()==302){
			cookie = cookies[0].getValue().substring(0, cookies[0].getValue().indexOf(";"));/*   value.substring(value.indexOf("="),value.length()); + ";"*/
			setCookies(cookie);
			System.out.println(response);
			sendDeviceKey();

			mainApp.showBooksOverview(email.getText());
	    }
		else{
			Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("IEDCS Player");
	        alert.setHeaderText("Login Failed");
	        alert.setContentText("Password or email wrong");
	        alert.showAndWait();
		}
    }

    public void setCookies(String cookies) {
    	LoginController.cookies = cookies;
      }

	public static String getCookies() {

		return cookies;
	}

	/**
     * Called when the user clicks on the read button.
	 * @throws IOException
     * @throws ClientProtocolException
     */
    @FXML
    private void handleLoginCC() throws ClientProtocolException, IOException{
    	String url = Http_Client.getURL() + "users/loginCC/";

		HttpPost post = new HttpPost(url);

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("user", email.getText()));

		post.setEntity(new UrlEncodedFormEntity(urlParameters));

		HttpResponse response = Http_Client.getHttpClient().execute(post);

		Header HeaderRandom = response.getFirstHeader("random");
		Header HeaderTrans = response.getFirstHeader("transactionID");
		//System.out.println("Random Recebido: " + HeaderRandom.getValue());
		//System.out.println("TransId Recebido: " + HeaderTrans.getValue());

		BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
		StringBuffer userBooks = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			userBooks.append(line);
		}
		HttpEntity entity = response.getEntity();
		if(entity == null){
		}
		else{
			EntityUtils.consume(entity);
		}

		System.out.println("Primeira resposta: "+ response);

		if(response.getStatusLine().getStatusCode()==200){

			String f = "CitizenCard.cfg";
	        Provider p = new sun.security.pkcs11.SunPKCS11(f);
	        Security.addProvider(p);
			KeyManager.createDeviveKey();
			String randomSigned = "";
			/***/
			try{
				KeyStore.CallbackHandlerProtection func = new KeyStore.CallbackHandlerProtection(new MyCallbackHandler());
	            KeyStore.Builder builder = KeyStore.Builder.newInstance("PKCS11", p, func);
	            KeyStore ks = builder.getKeyStore();

	            Signature sig = Signature.getInstance("SHA1withRSA");

	            sig.initSign((PrivateKey)ks.getKey("CITIZEN AUTHENTICATION CERTIFICATE", null));

	            byte txt[] = Base64.getDecoder().decode(HeaderRandom.getValue());

	            sig.update(txt);

	            byte[] signed = sig.sign();


	            randomSigned = Base64.getEncoder().encodeToString(signed);

	            System.out.println("Random Signed: "+ randomSigned);

			}catch(Exception e){
	            e.printStackTrace();
	            System.err.println(e);
	            System.exit(0);
	        }
            /***/

	    	url = Http_Client.getURL() + "users/validateLoginCC/";
	    	String cookie="";

			post = new HttpPost(url);

			urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("transactionId", HeaderTrans.getValue()));
			urlParameters.add(new BasicNameValuePair("signed", randomSigned));

			post.setEntity(new UrlEncodedFormEntity(urlParameters));

			response = Http_Client.getHttpClient().execute(post);

			Header[] cookies =  response.getHeaders("Set-Cookie");

			System.out.println("Token: "+ cookies[0].getValue().substring(0, cookies[0].getValue().indexOf(";")));
			rd = new BufferedReader(
	                new InputStreamReader(response.getEntity().getContent()));

			userBooks = new StringBuffer();
			line = "";
			while ((line = rd.readLine()) != null) {
				userBooks.append(line);
			}
			System.out.println("Segunda resposta: "+response);
			if(response.getStatusLine().getStatusCode()==200){
				cookie = cookies[0].getValue().substring(0, cookies[0].getValue().indexOf(";"));/*   value.substring(value.indexOf("="),value.length()); + ";"*/
				setCookies(cookie);
				System.out.println(response);
				sendDeviceKey();

				mainApp.showBooksOverview(email.getText());
		    }
			else{
				Alert alert = new Alert(AlertType.INFORMATION);
		        alert.setTitle("IEDCS Player");
		        alert.setHeaderText("Login Failed");
		        alert.setContentText("Email or CC wrong");
		        alert.showAndWait();
			}
	    }
		else{
			Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("IEDCS Player");
	        alert.setHeaderText("Login Failed");
	        alert.setContentText("Email wrong or user haven't linked the CC");
	        alert.showAndWait();
		}
    }
}
