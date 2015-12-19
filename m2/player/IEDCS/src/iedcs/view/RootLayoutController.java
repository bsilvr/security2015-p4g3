package iedcs.view;

import iedcs.MainApp;
import iedcs.model.Http_Client;
import iedcs.model.KeyManager;
import iedcs.model.MyCallbackHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import sun.security.pkcs11.SunPKCS11;
import java.security.*;
import java.security.Provider;
import java.security.Security;
import java.security.KeyStore;
import java.io.*;
import java.util.Enumeration;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.security.Signature;
import java.util.ArrayList;
import java.util.Base64;

public class RootLayoutController {

	// Reference to the main application
    @SuppressWarnings("unused")
	private MainApp mainApp;

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Opens an about dialog.
     */

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("IEDCS Player");
        alert.setHeaderText("About");
        alert.setContentText("Author: Bernardo Ferreira and Bruno Silva");

        alert.showAndWait();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }


    /**
     * Adds CC.
     */
    @FXML
    private void handleCC() {
    	String f = "CitizenCard.cfg";
        Provider p = new sun.security.pkcs11.SunPKCS11(f);
        Security.addProvider(p);
        try{


        	KeyStore.CallbackHandlerProtection func = new KeyStore.CallbackHandlerProtection(new MyCallbackHandler());
            KeyStore.Builder builder = KeyStore.Builder.newInstance("PKCS11", p, func);
            KeyStore ks = builder.getKeyStore();

            Signature dec = Signature.getInstance("SHA1withRSA");

            dec.initVerify(ks.getCertificate("CITIZEN AUTHENTICATION CERTIFICATE"));

            PublicKey pub = ks.getCertificate("CITIZEN AUTHENTICATION CERTIFICATE").getPublicKey();

            System.out.println("-----BEGIN PUBLIC KEY-----\n"+Base64.getEncoder().encodeToString(pub.getEncoded()) + "\n-----END PUBLIC KEY-----");

            String publicKey = "-----BEGIN PUBLIC KEY-----\n"+Base64.getEncoder().encodeToString(pub.getEncoded()) + "\n-----END PUBLIC KEY-----";

            /**************************************/

            String url = Http_Client.getURL() + "users/addCC/";

    		HttpPost post = new HttpPost(url);
    		String cookie = LoginController.getCookies();

    		post.addHeader("Referer", Http_Client.getURL());


    		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
    		urlParameters.add(new BasicNameValuePair("public_key", publicKey));
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


    		HttpEntity entity = response.getEntity();
    		if(entity == null){
    		}
    		else{
    			EntityUtils.consume(entity);
    		}
    		System.out.println(response);

    		if(response.getStatusLine().getStatusCode()==202){
    			Alert alert = new Alert(AlertType.INFORMATION);
    	        alert.setTitle("Add CC request");
    	        alert.setHeaderText("Error");
    	        alert.setContentText("CC already linked to this user");
    	        alert.showAndWait();
    	    }
    		else if(response.getStatusLine().getStatusCode()==200){
    			Alert alert = new Alert(AlertType.INFORMATION);
    	        alert.setTitle("Add CC request");
    	        alert.setHeaderText("Success");
    	        alert.setContentText("CC successfully linked to this user");
    	        alert.showAndWait();
    		}
    		else{
    			Alert alert = new Alert(AlertType.INFORMATION);
    	        alert.setTitle("Add CC request");
    	        alert.setHeaderText("Error");
    	        alert.setContentText("Invalid key");
    	        alert.showAndWait();
    		}

        }catch(Exception e){

        	e.printStackTrace();
            System.err.println(e);
            System.exit(0);

        }
    }

}
