package iedcs;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import iedcs.model.Book;
import iedcs.view.BookOverviewController;
import iedcs.view.BookReaderController;
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

    	/*String url = "http://127.0.0.1:8000/users/get_purchases/";
    	String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
    	String param1 = "bernardomrferreira@ua.pt";
    	// ...

    	String query = "user=%22"+ param1 + "%22";

    	URLConnection connection = new URL(url + "?" + query).openConnection();
    	connection.setRequestProperty("Accept-Charset", charset);
    	InputStream response = connection.getInputStream();

    	String result = getStringFromInputStream(response);

    	System.out.print(result);
    	*/

    	String url = "http://127.0.0.1:8000/users/get_purchases/?format=api&user=%22bernardomrferreira%40ua.pt%22";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("user", "bernardomrferreira@ua.pt");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());


    	File dracula = new File("resources/books/dracula.txt");
    	File sawyer = new File("resources/books/sawyer.txt");
        // Some sample data
        bookData.add(new Book("Mark Twain", "The Adventures of Tom Sawyer", "Inglês", "https://www.gutenberg.org/cache/epub/74/pg74.cover.medium.jpg", "1", sawyer));
        bookData.add(new Book("Bram Stoker", "Dracula", "Inglês", "https://www.gutenberg.org/cache/epub/345/pg345.cover.medium.jpg", "2", dracula));

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

        showBooksOverview();
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
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

	public static void main(String[] args) throws IOException {
		/*System.getProperties().list(System.out);

		URL whatismyip = new URL("http://checkip.amazonaws.com");
		BufferedReader in = new BufferedReader(new InputStreamReader(
		                whatismyip.openStream()));

		String ip = in.readLine(); //you get the IP as a String
		System.out.println(ip);*/

		launch(args);
	}

	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

}
