package iedcs.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import iedcs.MainApp;
import iedcs.resources.Location.SNMac;
import iedcs.resources.Location.SNUnix;
import iedcs.resources.Location.SNWindows;
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
    	String SN="";

    	if (System.getProperties().getProperty("os.name").contains("Mac")) {
			SN = SNMac.getSerialNumber();
			SN = SN.substring(Math.max(SN.length() - 8, 0));
	    }
    	else if (System.getProperties().getProperty("os.name").contains("Windows")) {
			SN = SNWindows.getSerialNumber();
			SN = SN.substring(Math.max(SN.length() - 8, 0));
	    }
    	else if (System.getProperties().getProperty("os.name").contains("Linux")) {
			SN = SNUnix.getSerialNumber();
			SN = SN.substring(Math.max(SN.length() - 8, 0));
	    }
		else{
			System.exit(0);
		}

    	String url = "http://127.0.0.1:8000/users/register_device/";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("user", email.getText()));
		urlParameters.add(new BasicNameValuePair("device_key", SN));

		post.setEntity(new UrlEncodedFormEntity(urlParameters));

		client.execute(post);

    }

    /**
     * Called when the user clicks on the read button.
     * @throws IOException
     * @throws ClientProtocolException
     */
    @FXML
    private void handleLogin() throws ClientProtocolException, IOException {


    	String url = "http://127.0.0.1:8000/users/login/";
    	String cookie="";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("email", email.getText()));
		urlParameters.add(new BasicNameValuePair("password", password.getText()));

		post.setEntity(new UrlEncodedFormEntity(urlParameters));

		HttpResponse response = client.execute(post);

//		setCookies(response.getFirstHeader("Set-Cookie") == null ? "" :
//			response.getFirstHeader("Set-Cookie").toString());
//		System.out.println(cookies);
//		Header[] cookies =  response.getHeaders("Set-Cookie");

//		cookie += cookies[1].getValue().substring(0, cookies[1].getValue().indexOf(";")) + "; "
//				+ cookies[0].getValue().substring(0, cookies[0].getValue().indexOf(";"));
//		System.out.print(cookie);
//
//		setCookies(cookie);
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
    public static  String getCookies() {
    	return cookies;
      }
    public void setCookies(String cookies) {
    	this.cookies = cookies;
      }
}
