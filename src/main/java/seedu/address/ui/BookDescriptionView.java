package seedu.address.ui;

import com.sun.javafx.webkit.Accessor;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import seedu.address.model.book.Book;

//@@author qiu-siqi
/**
 * The Region showing description of books.
 */
public class BookDescriptionView extends UiPart<Region> {

    private static final int HEIGHT_PAD = 20;

    private static final String FXML = "BookDescriptionView.fxml";

    @FXML
    private WebView description;

    private final WebEngine webEngine;

    public BookDescriptionView() {
        super(FXML);
        webEngine = description.getEngine();

        // disable interaction with web view
        description.setDisable(true);

        description.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                adjustHeight();
            }
        });
    }

    /**
     * Loads the description of {@code book}.
     */
    protected void loadContent(Book book) {
        description.getEngine().loadContent(getHtml(book.getDescription().toString()));
        // set transparent background for web view
        Accessor.getPageFor(webEngine).setBackgroundColor(0);
    }

    // Reused from http://tech.chitgoks.com/2014/09/13/how-to-fit-webview-height-based-on-its-content-in-java-fx-2-2/
    /**
     * Fit height of {@code WebView} according to height of content.
     */
    private void adjustHeight() {
        Platform.runLater(() -> {
            Object result = webEngine.executeScript("document.getElementById('description').offsetHeight");
            if (result instanceof Integer) {
                Integer height = (Integer) result;
                description.setPrefHeight(height + HEIGHT_PAD);
            }
        });
    }

    // Reused from http://tech.chitgoks.com/2014/09/13/how-to-fit-webview-height-based-on-its-content-in-java-fx-2-2/
    private String getHtml(String content) {
        return "<html><body><div id=\"description\">" + content + "</div></body></html>";
    }

    protected void setStyleSheet(String styleSheet) {
        description.getEngine().setUserStyleSheetLocation(getClass().getClassLoader()
                .getResource(styleSheet).toExternalForm());
    }

}
