package seedu.address.ui;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import com.google.common.base.Charsets;
import com.google.common.eventbus.Subscribe;
import com.google.common.io.Resources;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
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
    private StackPane browserPlaceholder;

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

        WebViewManager webViewManager = WebViewManager.getInstance();
        webViewManager.onLoadSuccess(getRoot(), () -> webViewManager.executeScript(bookReviewsScript));
    }

    protected void loadPageForBook(Book book) {
        loadPage(SEARCH_PAGE_URL.replace("%isbn", book.getIsbn().isbn));
    }

    private void loadPage(String url) {
        WebViewManager.getInstance().load(browserPlaceholder, url);
    }

    private void clearPage() {
        WebViewManager.getInstance().executeScript(clearPageScript);
    }

    protected void hide() {
        getRoot().setVisible(false);
    }

    protected void show() {
        getRoot().setVisible(true);
    }

    @Subscribe
    private void handleShowBookReviewsRequestEvent(ShowBookReviewsRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        clearPage();
        show();
        loadPageForBook(event.getBook());
    }
}
