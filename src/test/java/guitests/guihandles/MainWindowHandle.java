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
    private final SearchResultsPanelHandle searchResultsPanel;
    private final RecentBooksPanelHandle recentBooksPanel;
    private final AliasListPanelHandle aliasListPanel;
    private final ResultDisplayHandle resultDisplay;
    private final CommandBoxHandle commandBox;
    private final StatusBarFooterHandle statusBarFooter;
    private final MainMenuHandle mainMenu;
    private final BookDetailsPanelHandle bookDetailsPanel;
    private final BookReviewsPanelHandle bookReviewsPanel;

    public MainWindowHandle(Stage stage) {
        super(stage);

        scene = stage.getScene();
        bookListPanel = new BookListPanelHandle(getChildNode(BookListPanelHandle.BOOK_LIST_VIEW_ID));
        searchResultsPanel =
                new SearchResultsPanelHandle(getChildNode(SearchResultsPanelHandle.SEARCH_RESULTS_LIST_VIEW_ID));
        recentBooksPanel = new RecentBooksPanelHandle(getChildNode(RecentBooksPanelHandle.RECENT_BOOKS_LIST_VIEW_ID));
        aliasListPanel = new AliasListPanelHandle(getChildNode(AliasListPanelHandle.ALIAS_LIST_PANEL_ID));
        resultDisplay = new ResultDisplayHandle(getChildNode(ResultDisplayHandle.RESULT_DISPLAY_ID));
        commandBox = new CommandBoxHandle(getChildNode(CommandBoxHandle.COMMAND_INPUT_FIELD_ID));
        statusBarFooter = new StatusBarFooterHandle(getChildNode(StatusBarFooterHandle.STATUS_BAR_PLACEHOLDER));
        mainMenu = new MainMenuHandle(getChildNode(MainMenuHandle.MENU_BAR_ID));
        bookDetailsPanel = new BookDetailsPanelHandle(getChildNode(BookDetailsPanelHandle.BOOK_DETAILS_PANE_ID));
        bookReviewsPanel = new BookReviewsPanelHandle(getChildNode(BookReviewsPanelHandle.BOOK_REVIEWS_PANE_ID));
    }

    public BookListPanelHandle getBookListPanel() {
        return bookListPanel;
    }

    public SearchResultsPanelHandle getSearchResultsPanel() {
        return searchResultsPanel;
    }

    public RecentBooksPanelHandle getRecentBooksPanel() {
        return recentBooksPanel;
    }

    public AliasListPanelHandle getAliasListPanel() {
        return aliasListPanel;
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

    public BookDetailsPanelHandle getBookDetailsPanel() {
        return bookDetailsPanel;
    }

    public BookReviewsPanelHandle getBookReviewsPanel() {
        return bookReviewsPanel;
    }

    public String getActiveStylesheet() {
        if (scene.getStylesheets().size() == 0) {
            throw new StylesheetNotFoundException();
        }
        return scene.getStylesheets().get(0);
    }
}
