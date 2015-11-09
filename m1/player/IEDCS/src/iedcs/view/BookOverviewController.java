package iedcs.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import iedcs.MainApp;
import iedcs.model.Book;
import iedcs.view.LoginController;
import iedcs.resources.Http_Client;
import iedcs.resources.KeyManager;
import iedcs.resources.Location.LookupService;

public class BookOverviewController {

	@FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private Label authorLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Label languageLabel;
    @FXML
    private ImageView bookImage;

    // Reference to the main application.
    private MainApp mainApp;

    private static Book currentBook;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public BookOverviewController() {
    }

    static Book getCurrentBook(){
    	return currentBook;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().bookAuthorProperty());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().bookTitleProperty());

        // Clear person details.
        showBookDetails(null);

        // Listen for selection changes and show the person details when changed.
        bookTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showBookDetails(newValue));
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        bookTable.setItems(mainApp.getBookData());
    }

    /**
     * Fills all text fields to show details about the book.
     * If the specified book is null, all text fields are cleared.
     *
     * @param book the book or null
     */
    private void showBookDetails(Book book) {
        if (book != null) {
        	currentBook = book;

        	Image bookImg = new Image(book.getBookImage(), 200, 0, false, false);


            // Fill the labels with info from the book object.
            authorLabel.setText(book.getBookAuthor());
            titleLabel.setText(book.getBookTitle());
            idLabel.setText("ID = " + book.getBookId());
            languageLabel.setText(book.getBookLanguage());
            bookImage.setImage(bookImg);


        } else {
            // Book is null, remove all the text.
        	Image bookImg = new Image("http://www.venca.pt/i/0/ori/image_not_found.jpg");

        	authorLabel.setText("");
        	titleLabel.setText("");
        	idLabel.setText("");
        	languageLabel.setText("");
            bookImage.setImage(bookImg);
        }
    }

    /**
     * Called when the user clicks on the read button.
     * @throws IOException
     */
    @FXML
    private void handleReadBook() throws IOException {

 		sendReadRequest();
		sendRestrictions();

    	mainApp.showBookReader(currentBook);
    }

    public void sendReadRequest() throws UnsupportedOperationException, IOException{
    	String url = "http://127.0.0.1:8000/requests/read_book/";

		HttpPost post = new HttpPost(url);
		String cookie = LoginController.getCookies();


		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("book_id", currentBook.getBookId()));
		urlParameters.add(new BasicNameValuePair("csrfmiddlewaretoken", cookie.substring(cookie.indexOf("=")+1,cookie.length())));


		post.setEntity(new UrlEncodedFormEntity(urlParameters));

		Http_Client.getHttpClient().execute(post);

    }

    public void sendRestrictions() throws UnsupportedOperationException, IOException{
    	/*Get location*/
    	String location="";
    	URL whatismyip = new URL("http://checkip.amazonaws.com");
		BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

		String ip = in.readLine();
		try {
			LookupService cl = new LookupService("resources/locations/GeoIP.dat",LookupService.GEOIP_MEMORY_CACHE);

		    location = cl.getCountry(ip).getCode();

		    cl.close();
		}catch (IOException e) {
		    System.out.println("IO Exception");
		}

		/*Get time*/
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        /*Send Post*/
    	String url = "http://127.0.0.1:8000/requests/validate/";

		HttpPost post = new HttpPost(url);
		String cookie = LoginController.getCookies();

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("device_key", KeyManager.getDeviceKey()));
		System.out.println("IO Exception1");
		urlParameters.add(new BasicNameValuePair("player_key", KeyManager.getPlayerKey()));
		System.out.println("IO Exception2");
		urlParameters.add(new BasicNameValuePair("so", System.getProperties().getProperty("os.name")));
		System.out.println("IO Exception3");
		urlParameters.add(new BasicNameValuePair("location", location));
		System.out.println("IO Exception4");
		urlParameters.add(new BasicNameValuePair("book_id", currentBook.getBookId()));
		System.out.println("IO Exception5");
		urlParameters.add(new BasicNameValuePair("time", sdf.format(cal.getTime())));
		System.out.println("IO Exception6");

		urlParameters.add(new BasicNameValuePair("csrfmiddlewaretoken", cookie.substring(cookie.indexOf("=")+1,cookie.length())));
		System.out.println("IO Exception7");


		post.setEntity(new UrlEncodedFormEntity(urlParameters));
		System.out.println("IO Exception8");
//		HttpResponse response =
		Http_Client.getHttpClient().execute(post);
		System.out.println("IO Exception9");

//		BufferedReader rd = new BufferedReader(
//                new InputStreamReader(response.getEntity().getContent()));
//
//		StringBuffer userBooks = new StringBuffer();
//		String line = "";
//		while ((line = rd.readLine()) != null) {
//			userBooks.append(line);
//		}
//
//		String result = userBooks.toString();
//		System.out.println(result);

    }



}
