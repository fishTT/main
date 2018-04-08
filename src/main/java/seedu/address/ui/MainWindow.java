package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.WindowSettings;
import seedu.address.commons.events.ui.BookListSelectionChangedEvent;
import seedu.address.commons.events.ui.ChangeThemeRequestEvent;
import seedu.address.commons.events.ui.ClearMainContentRequestEvent;
import seedu.address.commons.events.ui.ExitAppRequestEvent;
import seedu.address.commons.events.ui.RecentBooksSelectionChangedEvent;
import seedu.address.commons.events.ui.SearchResultsSelectionChangedEvent;
import seedu.address.commons.events.ui.ShowAliasListRequestEvent;
import seedu.address.commons.events.ui.ShowBookReviewsRequestEvent;
import seedu.address.commons.events.ui.ShowHelpRequestEvent;
import seedu.address.commons.events.ui.SwitchToBookListRequestEvent;
import seedu.address.commons.events.ui.SwitchToRecentBooksRequestEvent;
import seedu.address.commons.events.ui.SwitchToSearchResultsRequestEvent;
import seedu.address.logic.Logic;
import seedu.address.model.UserPrefs;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private WelcomePanel welcomePanel;
    private BookDetailsPanel bookDetailsPanel;
    private BookReviewsPanel bookReviewsPanel;
    private AliasListPanel aliasListPanel;
    private BookListPanel bookListPanel;
    private SearchResultsPanel searchResultsPanel;
    private RecentBooksPanel recentBooksPanel;
    private Config config;
    private UserPrefs prefs;

    @FXML
    private Scene scene;

    @FXML
    private StackPane mainContentPlaceholder;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane bookListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    public MainWindow(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;
        this.config = config;
        this.prefs = prefs;

        // Configure the UI
        setTitle(config.getAppTitle());
        setWindowDefaultSize(prefs);
        updateStylesheet(prefs);

        setAccelerators();
        registerAsAnEventHandler(this);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        welcomePanel = new WelcomePanel();
        bookDetailsPanel = new BookDetailsPanel();
        bookReviewsPanel = new BookReviewsPanel();
        aliasListPanel = new AliasListPanel(logic.getDisplayAliasList());
        mainContentPlaceholder.getChildren().add(welcomePanel.getRoot());
        mainContentPlaceholder.getChildren().add(bookDetailsPanel.getRoot());
        mainContentPlaceholder.getChildren().add(bookReviewsPanel.getRoot());
        mainContentPlaceholder.getChildren().add(aliasListPanel.getRoot());
        bookReviewsPanel.getRoot().setVisible(false);
        bookDetailsPanel.setStyleSheet(prefs.getAppTheme().getCssFile());

        bookListPanel = new BookListPanel(logic.getDisplayBookList());
        searchResultsPanel = new SearchResultsPanel(logic.getSearchResultsList());
        recentBooksPanel = new RecentBooksPanel(logic.getRecentBooksList());
        bookListPanelPlaceholder.getChildren().add(searchResultsPanel.getRoot());
        bookListPanelPlaceholder.getChildren().add(bookListPanel.getRoot());
        bookListPanelPlaceholder.getChildren().add(recentBooksPanel.getRoot());
        searchResultsPanel.getRoot().setVisible(false);
        recentBooksPanel.getRoot().setVisible(false);

        ResultDisplay resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(prefs.getBookShelfFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(logic);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the default size based on user preferences.
     */
    private void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getWindowSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getWindowSettings().getWindowWidth());
        if (prefs.getWindowSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getWindowSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getWindowSettings().getWindowCoordinates().getY());
        }
    }

    /** Updates the stylesheet used based on user preferences. */
    private void updateStylesheet(UserPrefs prefs) {
        scene.getStylesheets().setAll(prefs.getAppTheme().getCssFile());
        if (bookDetailsPanel != null) {
            bookDetailsPanel.setStyleSheet(prefs.getAppTheme().getCssFile());
        }
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    WindowSettings getCurrentGuiSetting() {
        return new WindowSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
    }

    /**
     * Opens the help window.
     */
    @FXML
    public void handleHelp() {
        HelpWindow helpWindow = new HelpWindow();
        helpWindow.show();
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Clears the list selections in the book list, search results, and recent books panel.
     */
    private void clearAllListSelections() {
        bookListPanel.clearSelection();
        searchResultsPanel.clearSelection();
        recentBooksPanel.clearSelection();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

    @Subscribe
    private void handleChangeThemeRequestEvent(ChangeThemeRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        prefs.setAppTheme(event.newTheme);
        updateStylesheet(prefs);
    }

    @Subscribe
    private void handleShowHelpRequestEvent(ShowHelpRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        handleHelp();
    }

    @Subscribe
    private void handleSwitchToBookListRequestEvent(SwitchToBookListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        welcomePanel.show();
        bookDetailsPanel.hide();
        bookReviewsPanel.hide();
        aliasListPanel.hide();
        bookListPanel.clearSelection();
        bookListPanel.scrollToTop();
        bookListPanel.getRoot().setVisible(true);
        searchResultsPanel.getRoot().setVisible(false);
        recentBooksPanel.getRoot().setVisible(false);
    }

    @Subscribe
    private void handleSwitchToSearchResultsRequestEvent(SwitchToSearchResultsRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        welcomePanel.show();
        bookDetailsPanel.hide();
        bookReviewsPanel.hide();
        aliasListPanel.hide();
        searchResultsPanel.clearSelection();
        searchResultsPanel.scrollToTop();
        bookListPanel.getRoot().setVisible(false);
        searchResultsPanel.getRoot().setVisible(true);
        recentBooksPanel.getRoot().setVisible(false);
    }

    @Subscribe
    private void handleSwitchToRecentBooksRequestEvent(SwitchToRecentBooksRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        welcomePanel.show();
        bookDetailsPanel.hide();
        bookReviewsPanel.hide();
        aliasListPanel.hide();
        recentBooksPanel.clearSelection();
        recentBooksPanel.scrollToTop();
        bookListPanel.getRoot().setVisible(false);
        searchResultsPanel.getRoot().setVisible(false);
        recentBooksPanel.getRoot().setVisible(true);
    }

    @Subscribe
    private void handleClearBookDetailsRequestEvent(ClearMainContentRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        bookDetailsPanel.hide();
        bookReviewsPanel.hide();
        welcomePanel.show();
    }

    @Subscribe
    private void handleBookListSelectionChangedEvent(BookListSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        welcomePanel.hide();
        bookReviewsPanel.hide();
        aliasListPanel.hide();
    }

    @Subscribe
    private void handleSearchResultsSelectionChangedEvent(SearchResultsSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        welcomePanel.hide();
        bookReviewsPanel.hide();
        aliasListPanel.hide();
    }

    @Subscribe
    private void handleRecentBooksSelectionChangedEvent(RecentBooksSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        welcomePanel.hide();
        bookReviewsPanel.hide();
        aliasListPanel.hide();
    }

    @Subscribe
    private void handleShowBookReviewsRequestEvent(ShowBookReviewsRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        clearAllListSelections();
        welcomePanel.hide();
        bookDetailsPanel.hide();
        aliasListPanel.hide();
    }

    @Subscribe
    private void handleShowAliasListRequestEvent(ShowAliasListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        clearAllListSelections();
        welcomePanel.hide();
        bookDetailsPanel.hide();
        bookReviewsPanel.hide();
        aliasListPanel.show();
    }
}
