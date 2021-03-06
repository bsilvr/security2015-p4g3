package iedcs.view;

import java.util.concurrent.ExecutionException;

import iedcs.MainApp;
import iedcs.model.KeyManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class BookReaderController {

	// Reference to the main application.
	private MainApp mainApp;



	@FXML
	private TextArea linesTextArea;

	@FXML
	private Label titleLabel;

	@FXML
	private Button back;


	public void initialize() throws InterruptedException, ExecutionException {
		titleLabel.setText(BookOverviewController.getCurrentBook().getBookTitle());

		showFile();
	}

	@FXML
	public void handleBackButton() {

		mainApp.showBooksOverview();
	}

	public void showFile() throws InterruptedException, ExecutionException {

		linesTextArea.appendText(KeyManager.getBook() + "\n");
		Platform.runLater( new Runnable() {
		    @Override
		    public void run() {
		    	linesTextArea.positionCaret( 0 );
		    }
		});

		linesTextArea.setScrollTop(0);
	}

	/**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

    }


}
