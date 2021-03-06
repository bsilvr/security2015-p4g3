package iedcs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import iedcs.model.Book;
import iedcs.model.Http_Client;
import iedcs.resources.JsonParser.Json;
import iedcs.resources.JsonParser.JsonArray;
import iedcs.resources.JsonParser.JsonObject;
import iedcs.resources.JsonParser.JsonValue;
import iedcs.view.BookOverviewController;
import iedcs.view.BookReaderController;
import iedcs.view.LoginController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

	private Stage primaryStage;
    private BorderPane rootLayout;

    /**
     * The data as an observable list of Books.
     */
    private ObservableList<Book> bookData = FXCollections.observableArrayList();

    /**
     * Constructor
     * @throws IOException
     * @throws MalformedURLException
     */
    public MainApp() throws MalformedURLException, IOException {

    }

	private void getUserBooksInfo(Integer pop) throws MalformedURLException, IOException {


        String url = Http_Client.getURL() + "books/get_book/";
        String param1 = pop.toString();
		String query = "book_id="+ param1;


		HttpGet request = new HttpGet(url + "?" + query);

		HttpResponse response = Http_Client.getHttpClient().execute(request);

		BufferedReader rd = new BufferedReader(
                       new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		JsonObject items = Json.parse(result.toString()).asObject();

		String author = items.get("author").asString();
		String title = items.get("title").asString();
		String language = items.get("language").asString();
		String cover = items.get("cover").asString();
		String id = Integer.toString(items.get("ebook_id").asInt());

        bookData.add(new Book(author,title,language,cover,id));
        HttpEntity entity = response.getEntity();
		if(entity == null){
		}
		else{
			EntityUtils.consume(entity);
		}
		System.out.println("b");




	}

	public Stack<Integer> getUserBooksIds(String email) throws MalformedURLException, IOException{

		Stack<Integer> lifo = new Stack<Integer>();

		String url = Http_Client.getURL() + "users/get_purchases/";


		HttpPost post = new HttpPost(url);

		post.addHeader("Referer", Http_Client.getURL());

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("user", email));
		String cookie =LoginController.getCookies();
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
		result = "{\"items\": " + result+"}";

		JsonArray items = Json.parse(result.toString()).asObject().get("items").asArray();
		for (JsonValue item : items) {
		  lifo.push(item.asObject().getInt("book_id", 0));
		}
		HttpEntity entity = response.getEntity();
		if(entity == null){
		}
		else{
			EntityUtils.consume(entity);
		}

		return lifo;
	}


    /**
     * Returns the data as an observable list of Books.
     * @return
     */
    public ObservableList<Book> getBookData() {
        return bookData;
    }

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
        this.primaryStage.setTitle("IEDCS Player");

        this.primaryStage.getIcons().add(new Image("file:resources/images/book_icon.png"));

        initRootLayout();

        showLogin();
	}

	/**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the book overview inside the root layout.
     */
    public void showBooksOverview(String email) {
        try {

        	Stack<Integer> books = getUserBooksIds(email);
        	while ( !books.empty() )
            {
        		getUserBooksInfo(books.pop());
            }

            // Load book overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/BooksOverview.fxml"));
            AnchorPane bookOverview = (AnchorPane) loader.load();

            // Set book overview into the center of root layout.
            rootLayout.setCenter(bookOverview);

         // Give the controller access to the main app.
            BookOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Shows the book overview inside the root layout.
     */
    public void showBooksOverview() {
        try {
            // Load book overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/BooksOverview.fxml"));
            AnchorPane bookOverview = (AnchorPane) loader.load();

            // Set book overview into the center of root layout.
            rootLayout.setCenter(bookOverview);

         // Give the controller access to the main app.
            BookOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the book reader
     */
    public void showBookReader(Book book) {
        try {
            // Load book overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/BookReader.fxml"));
            AnchorPane bookReader = (AnchorPane) loader.load();

            // Set book reader into the center of root layout.
            rootLayout.setCenter(bookReader);

         // Give the controller access to the main app.
            BookReaderController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the login
     */
    public void showLogin() {
        try {
            // Load book overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/LoginForm.fxml"));
            AnchorPane login = (AnchorPane) loader.load();

            // Set book reader into the center of root layout.
            rootLayout.setCenter(login);

         // Give the controller access to the main app.
            LoginController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

	public static void main(String[] args) {
//		String pathToKeyStore = "mykeystore.keystore";
//
//
//		try {
//			FileInputStream fin = new FileInputStream(pathToKeyStore);
//	        KeyStore keystore  = KeyStore.getInstance("JKS");
//	        keystore.load(fin, "ebookwebstore".toCharArray());
//
//	        TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//
//				factory.init(keystore);
//
//	        TrustManager[] tm = factory.getTrustManagers();
//
//	        KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(
//	                KeyManagerFactory.getDefaultAlgorithm());
//	        kmfactory.init(keystore, "ebookwebstore".toCharArray());
//	        KeyManager[] km = kmfactory.getKeyManagers();
//
//	        SSLContext sslcontext = SSLContext.getInstance("SSL");
//	        sslcontext.init(km, tm, null);
//
//	        // set up a TrustManager that trusts everything
//	        sslcontext.init(km, tm, new SecureRandom());
//
//	        HttpClientBuilder builder = HttpClientBuilder.create();
//	        @SuppressWarnings("deprecation")
//			SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslcontext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//	        builder.setSSLSocketFactory(sslConnectionFactory);
//
//	        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
//	                .register("https", sslConnectionFactory)
//	                .build();
//
//	        HttpClientConnectionManager ccm = new BasicHttpClientConnectionManager(registry);
//
//	        builder.setConnectionManager(ccm);
//
//
//			URL destinationURL = new URL("https://localhost:8080/");
//	        HttpsURLConnection conn = (HttpsURLConnection) destinationURL
//	                .openConnection();
//	        ccm.connect();
//	        Certificate[] certs = conn.getServerCertificates();
//	        for (Certificate cert : certs) {
//	            System.out.println("Certificate is: " + cert);
//	            if(cert instanceof X509Certificate) {
//	                System.out.println("Certificate is active for current date");
//	            }
//	        }
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		launch(args);
	}

}
