package iedcs.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import iedcs.MainApp;
import iedcs.model.Book;

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
     */
    @FXML
    private void handleReadBook() {

    	mainApp.showBookReader(currentBook);
    }


}
