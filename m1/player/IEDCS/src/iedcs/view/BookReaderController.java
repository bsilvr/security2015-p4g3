package iedcs.view;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import iedcs.MainApp;
import iedcs.view.BookOverviewController;
import iedcs.model.TextFileReader;

public class BookReaderController {

	// Reference to the main application.
	private MainApp mainApp;



	@FXML
	private TextArea linesTextArea;

	@FXML
	private Label titleLabel;

	@FXML
	private Button back;

	private Future<List<String>> future;
	private ExecutorService executorService = Executors.newSingleThreadExecutor();
	private TextFileReader reader = new TextFileReader();

	public void initialize() throws InterruptedException, ExecutionException {
		titleLabel.setText(BookOverviewController.getCurrentBook().getBookTitle());
		showFile();
	}

	@FXML
	public void handleBackButton() {

		mainApp.showBooksOverview();
	}

	public void showFile() throws InterruptedException, ExecutionException {

		future = executorService.submit(new Callable<List<String>>() {
			public List<String> call() throws Exception {
				return reader.read(BookOverviewController.getCurrentBook().getBookTxt());
			}
		});

		List<String> lines = future.get();
		executorService.shutdownNow();
		linesTextArea.clear();
		for (String line : lines ) {
			linesTextArea.appendText(line + "\n");
		}


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
