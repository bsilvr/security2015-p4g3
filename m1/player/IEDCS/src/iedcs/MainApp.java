package iedcs;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import iedcs.resources.*;
import iedcs.resources.JsonParser.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import iedcs.model.Book;
import iedcs.view.BookOverviewController;
import iedcs.view.BookReaderController;
import iedcs.view.LoginController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

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
		/*Livros estaticos para ja*/
    	File sawyer = new File("resources/books/sawyer.txt");
    	/*------------------------*/

        String url = "http://127.0.0.1:8000/books/get_book/";
        String param1 = pop.toString();
		String query = "book_id="+ param1;

        HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url + "?" + query);

		HttpResponse response = client.execute(request);

		BufferedReader rd = new BufferedReader(
                       new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		JsonObject items = Json.parse(result.toString()).asObject();

		System.out.println(items.get("title").asString());

		String author = items.get("author").asString();
		String title = items.get("title").asString();
		String language = items.get("language").asString();
		String cover = items.get("cover").asString();
		String id = Integer.toString(items.get("ebook_id").asInt());

        bookData.add(new Book(author,title,language,cover,id, sawyer));




	}

	public Stack<Integer> getUserBooksIds(String email) throws MalformedURLException, IOException{

		Stack<Integer> lifo = new Stack<Integer>();

		String url = "http://127.0.0.1:8000/users/get_purchases/";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);


		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("user", email));

		post.setEntity(new UrlEncodedFormEntity(urlParameters));

		HttpResponse response = client.execute(post);

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

	public static void main(String[] args) throws IOException {
		URL whatismyip = new URL("http://checkip.amazonaws.com");
		BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

		String ip = in.readLine();
		System.out.print(ip);
		try {
			LookupService cl = new LookupService("resources/locations/GeoIP.dat",LookupService.GEOIP_MEMORY_CACHE);

		    System.out.println(cl.getCountry(ip).getCode());
		    System.out.println(cl.getCountry(ip).getName());
		    

		    cl.close();
		}
		catch (IOException e) {
		    System.out.println("IO Exception");
		}
		launch(args);
	}

}
