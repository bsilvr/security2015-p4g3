package iedcs.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import iedcs.MainApp;
import iedcs.resources.Http_Client;
import iedcs.resources.KeyManager;
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


    	String url = "http://127.0.0.1:8000/users/register_device/";
    	System.out.println(KeyManager.getDeviceKey());
		HttpPost post = new HttpPost(url);
		String cookie = LoginController.getCookies();


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

		String result = userBooks.toString();
		System.out.println(result);
    }

    /**
     * Called when the user clicks on the read button.
     * @throws IOException
     * @throws ClientProtocolException
     */
    @FXML
    private void handleLogin() throws ClientProtocolException, IOException {

    	KeyManager.createDeviveKey();
    	String url = "http://127.0.0.1:8000/users/login/";
    	String cookie="";

		HttpPost post = new HttpPost(url);

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("email", email.getText()));
		urlParameters.add(new BasicNameValuePair("password", password.getText()));

		post.setEntity(new UrlEncodedFormEntity(urlParameters));

		HttpResponse response = Http_Client.getHttpClient().execute(post);

		Header[] cookies =  response.getHeaders("Set-Cookie");
		for(int i=0; i< cookies.length; i++){
			System.out.println(cookies[i].getValue());
		}

		cookie = cookies[0].getValue().substring(0, cookies[0].getValue().indexOf(";"));/*   value.substring(value.indexOf("="),value.length()); + ";"*/


		setCookies(cookie);
		BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

		StringBuffer userBooks = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			userBooks.append(line);
		}

		String result = userBooks.toString();
		System.out.println(result);
		if(response.getStatusLine().getStatusCode()==302){
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
		// TODO Auto-generated method stub
		return cookies;
	}
}
