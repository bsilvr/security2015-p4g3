package iedcs;

import java.util.Stack;
import iedcs.resources.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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

    	Stack<Integer> books = getUserBooksIds();

    	String url = "http://127.0.0.1:8000/books/get_book/";
		String param1 = "49941";
		String query = "book_id="+ param1;

		URLConnection connection = new URL(url + "?" + query).openConnection();
		InputStream response = connection.getInputStream();

		String result = getStringFromInputStream(response);
		System.out.print(result);


    	File dracula = new File("resources/books/dracula.txt");
    	File sawyer = new File("resources/books/sawyer.txt");
        // Some sample data
        bookData.add(new Book("Mark Twain", "The Adventures of Tom Sawyer", "Inglês", "https://www.gutenberg.org/cache/epub/74/pg74.cover.medium.jpg", "1", sawyer));
        bookData.add(new Book("Bram Stoker", "Dracula", "Inglês", "https://www.gutenberg.org/cache/epub/345/pg345.cover.medium.jpg", "2", dracula));

    }

	public Stack<Integer> getUserBooksIds() throws MalformedURLException, IOException{
		Stack<Integer> lifo = new Stack<Integer>();
		String url = "http://127.0.0.1:8000/users/get_purchases/";
		String param1 = "bernardomrferreira@ua.pt";
		String query = "user="+ param1;

		URLConnection connection = new URL(url + "?" + query).openConnection();
		InputStream response = connection.getInputStream();

		String result = getStringFromInputStream(response);
		result = "{\"items\": " + result+"}";

		JsonArray items = Json.parse(result).asObject().get("items").asArray();
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
