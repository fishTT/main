package seedu.address.ui;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import com.google.common.base.Charsets;
import com.google.common.eventbus.Subscribe;
import com.google.common.io.Resources;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.ShowLibraryResultRequestEvent;

//@@author qiu-siqi
/**
 * The region showing availability of the book in NLB.
 */
public class BookInLibraryPanel extends UiPart<Region> {

    private static final String FXML = "BookInLibraryPanel.fxml";
    private static final URL NLB_RESULT_SCRIPT_FILE = MainApp.class.getResource("/view/nlbResultScript.js");
    private static final URL CLEAR_PAGE_SCRIPT_FILE = MainApp.class.getResource("/view/clearPageScript.js");

    private final Logger logger = LogsCenter.getLogger(this.getClass());
    private final String nlbResultScript;
    private final String clearPageScript;

    @FXML
    private StackPane browserPlaceholder;
    private WebViewManager webViewManager;

    public BookInLibraryPanel(WebViewManager webViewManager) {
        super(FXML);

        this.webViewManager = webViewManager;
        registerAsAnEventHandler(this);
        getRoot().setVisible(false);

        try {
            nlbResultScript = Resources.toString(NLB_RESULT_SCRIPT_FILE, Charsets.UTF_8);
            clearPageScript = Resources.toString(CLEAR_PAGE_SCRIPT_FILE, Charsets.UTF_8);
        } catch (IOException e) {
            throw new AssertionError("Missing script file: " + e.getMessage());
        }

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        webViewManager.onLoadProgress(getRoot(), 0.59, () -> {
            webViewManager.executeScript(nlbResultScript);
            getRoot().setDisable(false);
        });
    }

    private void loadPageWithResult(String result) {
        webViewManager.load(browserPlaceholder, result);
    }

    private void clearPage() {
        webViewManager.executeScript(clearPageScript);
    }

    protected void hide() {
        getRoot().setVisible(false);
    }

    protected void show() {
        getRoot().setVisible(true);
    }

    @Subscribe
    private void handleShowBookInLibraryRequestEvent(ShowLibraryResultRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Platform.runLater(() -> {
            clearPage();
            // Prevent browser from getting focus
            getRoot().setDisable(true);
            getRoot().setVisible(true);
            loadPageWithResult(event.getResult());
        });
    }
}
