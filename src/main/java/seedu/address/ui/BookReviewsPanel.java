package seedu.address.ui;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import com.google.common.base.Charsets;
import com.google.common.eventbus.Subscribe;
import com.google.common.io.Resources;

import javafx.concurrent.Worker;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.ShowBookReviewsRequestEvent;
import seedu.address.model.book.Book;

/**
 * The panel showing book reviews.
 */
public class BookReviewsPanel extends UiPart<Region> {
    protected static final String SEARCH_PAGE_URL = "https://www.goodreads.com/search?q=%isbn#other_reviews";
    private static final URL BOOK_REVIEWS_SCRIPT_FILE = MainApp.class.getResource("/view/bookReviewsScript.js");
    private static final URL CLEAR_PAGE_SCRIPT_FILE = MainApp.class.getResource("/view/clearPageScript.js");

    private static final String FXML = "BookReviewsPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(this.getClass());
    private final String bookReviewsScript;
    private final String clearPageScript;

    @FXML
    private WebView browser;

    public BookReviewsPanel() {
        super(FXML);

        registerAsAnEventHandler(this);
        getRoot().setVisible(false);

        try {
            bookReviewsScript = Resources.toString(BOOK_REVIEWS_SCRIPT_FILE, Charsets.UTF_8);
            clearPageScript = Resources.toString(CLEAR_PAGE_SCRIPT_FILE, Charsets.UTF_8);
        } catch (IOException e) {
            throw new AssertionError("Missing script file: " + e.getMessage());
        }

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        WebEngine engine = browser.getEngine();
        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            // run custom javascript when loading is complete
            if (newState == Worker.State.SUCCEEDED) {
                engine.executeScript(bookReviewsScript);
            }
        });
    }

    protected void loadPageForBook(Book book) {
        loadPage(SEARCH_PAGE_URL.replace("%isbn", book.getIsbn().isbn));
    }

    private void loadPage(String url) {
        browser.getEngine().load(url);
    }

    protected void hide() {
        getRoot().setVisible(false);
    }

    @Subscribe
    private void handleShowBookReviewsRequestEvent(ShowBookReviewsRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        browser.getEngine().executeScript(clearPageScript);
        loadPageForBook(event.getBook());
        getRoot().setVisible(true);
    }
}
