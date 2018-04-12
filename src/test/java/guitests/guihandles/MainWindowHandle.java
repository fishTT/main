package guitests.guihandles;

import guitests.guihandles.exceptions.StylesheetNotFoundException;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Provides a handle for {@code MainWindow}.
 */
public class MainWindowHandle extends StageHandle {

    private final Scene scene;
    private final BookListPanelHandle bookListPanel;
    private final ResultDisplayHandle resultDisplay;
    private final CommandBoxHandle commandBox;
    private final StatusBarFooterHandle statusBarFooter;
    private final MainMenuHandle mainMenu;
    private final WelcomePanelHandle welcomePanel;
    private final BookDetailsPanelHandle bookDetailsPanel;
    private final BookReviewsPanelHandle bookReviewsPanel;
    private final BookInLibraryPanelHandle bookInLibraryPanel;
    private final AliasListPanelHandle aliasListPanel;

    public MainWindowHandle(Stage stage) {
        super(stage);

        scene = stage.getScene();
        bookListPanel = new BookListPanelHandle(getChildNode(BookListPanelHandle.BOOK_LIST_VIEW_ID));
        resultDisplay = new ResultDisplayHandle(getChildNode(ResultDisplayHandle.RESULT_DISPLAY_ID));
        commandBox = new CommandBoxHandle(getChildNode(CommandBoxHandle.COMMAND_INPUT_FIELD_ID));
        statusBarFooter = new StatusBarFooterHandle(getChildNode(StatusBarFooterHandle.STATUS_BAR_PLACEHOLDER));
        mainMenu = new MainMenuHandle(getChildNode(MainMenuHandle.MENU_BAR_ID));
        welcomePanel = new WelcomePanelHandle(getChildNode(WelcomePanelHandle.WELCOME_PANEL_ID));
        bookDetailsPanel = new BookDetailsPanelHandle(getChildNode(BookDetailsPanelHandle.BOOK_DETAILS_PANE_ID));
        bookReviewsPanel = new BookReviewsPanelHandle(getChildNode(BookReviewsPanelHandle.BOOK_REVIEWS_PANE_ID));
        bookInLibraryPanel =
                new BookInLibraryPanelHandle(getChildNode(BookInLibraryPanelHandle.BOOK_IN_LIBRARY_PANEL_ID));
        aliasListPanel = new AliasListPanelHandle(getChildNode(AliasListPanelHandle.ALIAS_LIST_PANEL_ID));
    }

    public BookListPanelHandle getBookListPanel() {
        return bookListPanel;
    }

    public ResultDisplayHandle getResultDisplay() {
        return resultDisplay;
    }

    public CommandBoxHandle getCommandBox() {
        return commandBox;
    }

    public StatusBarFooterHandle getStatusBarFooter() {
        return statusBarFooter;
    }

    public MainMenuHandle getMainMenu() {
        return mainMenu;
    }

    public WelcomePanelHandle getWelcomePanel() {
        return welcomePanel;
    }

    public BookDetailsPanelHandle getBookDetailsPanel() {
        return bookDetailsPanel;
    }

    public BookReviewsPanelHandle getBookReviewsPanel() {
        return bookReviewsPanel;
    }

    public BookInLibraryPanelHandle getBookInLibraryPanel() {
        return bookInLibraryPanel;
    }
    public AliasListPanelHandle getAliasListPanel() {
        return aliasListPanel;
    }

    public String getActiveStylesheet() {
        if (scene.getStylesheets().size() == 0) {
            throw new StylesheetNotFoundException();
        }
        return scene.getStylesheets().get(0);
    }
}
