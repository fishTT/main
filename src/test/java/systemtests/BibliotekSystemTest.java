package systemtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.StatusBarFooter.SYNC_STATUS_INITIAL;
import static seedu.address.ui.StatusBarFooter.SYNC_STATUS_UPDATED;
import static seedu.address.ui.testutil.GuiTestAssert.assertDetailsPanelDisplaysBook;
import static seedu.address.ui.testutil.GuiTestAssert.assertListMatching;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;

import guitests.GuiRobot;
import guitests.guihandles.AliasListPanelHandle;
import guitests.guihandles.BookDetailsPanelHandle;
import guitests.guihandles.BookListPanelHandle;
import guitests.guihandles.BookReviewsPanelHandle;
import guitests.guihandles.CommandBoxHandle;
import guitests.guihandles.MainMenuHandle;
import guitests.guihandles.MainWindowHandle;
import guitests.guihandles.RecentBooksPanelHandle;
import guitests.guihandles.ResultDisplayHandle;
import guitests.guihandles.SearchResultsPanelHandle;
import guitests.guihandles.StatusBarFooterHandle;
import guitests.guihandles.WelcomePanelHandle;
import seedu.address.TestApp;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.model.BookShelf;
import seedu.address.model.Model;
import seedu.address.model.book.Book;
import seedu.address.testutil.TestUtil;
import seedu.address.testutil.TypicalBooks;
import seedu.address.ui.CommandBox;

/**
 * A system test class for Bibliotek, which provides access to handles of GUI components and helper methods
 * for test verification.
 */
public abstract class BibliotekSystemTest {
    @ClassRule
    public static ClockRule clockRule = new ClockRule();

    private static final List<String> COMMAND_BOX_DEFAULT_STYLE = Arrays.asList("text-input", "text-field");
    private static final List<String> COMMAND_BOX_ERROR_STYLE =
            Arrays.asList("text-input", "text-field", CommandBox.ERROR_STYLE_CLASS);

    private MainWindowHandle mainWindowHandle;
    private TestApp testApp;
    private SystemTestSetupHelper setupHelper;

    @BeforeClass
    public static void setupBeforeClass() {
        SystemTestSetupHelper.initialize();
    }

    @Before
    public void setUp() {
        setupHelper = new SystemTestSetupHelper();
        testApp = setupHelper.setupApplication(this::getInitialData, getDataFileLocation());
        mainWindowHandle = setupHelper.setupMainWindowHandle();

        assertApplicationStartingStateIsCorrect();
    }

    @After
    public void tearDown() throws Exception {
        setupHelper.tearDownStage();
        EventsCenter.clearSubscribers();
    }

    /**
     * Returns the data to be loaded into the file in {@link #getDataFileLocation()}.
     */
    protected BookShelf getInitialData() {
        return TypicalBooks.getTypicalBookShelf();
    }

    /**
     * Returns the directory of the data file.
     */
    protected String getDataFileLocation() {
        return TestUtil.getFilePathInSandboxFolder("sampleData.xml");
    }

    public MainWindowHandle getMainWindowHandle() {
        return mainWindowHandle;
    }

    public CommandBoxHandle getCommandBox() {
        return mainWindowHandle.getCommandBox();
    }

    public BookListPanelHandle getBookListPanel() {
        return mainWindowHandle.getBookListPanel();
    }

    public SearchResultsPanelHandle getSearchResultsPanel() {
        return mainWindowHandle.getSearchResultsPanel();
    }

    public RecentBooksPanelHandle getRecentBooksPanel() {
        return mainWindowHandle.getRecentBooksPanel();
    }

    public MainMenuHandle getMainMenu() {
        return mainWindowHandle.getMainMenu();
    }

    public WelcomePanelHandle getWelcomePanel() {
        return mainWindowHandle.getWelcomePanel();
    }

    public BookDetailsPanelHandle getBookDetailsPanel() {
        return mainWindowHandle.getBookDetailsPanel();
    }

    public BookReviewsPanelHandle getBookReviewsPanel() {
        return mainWindowHandle.getBookReviewsPanel();
    }

    public AliasListPanelHandle getAliasListPanel() {
        return mainWindowHandle.getAliasListPanel();
    }

    public StatusBarFooterHandle getStatusBarFooter() {
        return mainWindowHandle.getStatusBarFooter();
    }

    public ResultDisplayHandle getResultDisplay() {
        return mainWindowHandle.getResultDisplay();
    }

    /**
     * Returns a defensive copy of the current model.
     */
    protected Model getModel() {
        return testApp.getModel();
    }

    /**
     * Executes {@code command} in the application's {@code CommandBox}.
     * Method returns after UI components have been updated.
     */
    protected void executeCommand(String command) {
        rememberStates();
        // Injects a fixed clock before executing a command so that the time stamp shown in the status bar
        // after each command is predictable and also different from the previous command.
        clockRule.setInjectedClockToCurrentTime();

        mainWindowHandle.getCommandBox().run(command);
    }

    /**
     * Executes a {@code command} in the application's {@code CommandBox} and waits for it to complete.
     * In particular, this method will wait for the text in the result display to be changed to something other
     * than {@code resultText}, and for the command box to be enabled again.
     */
    protected void executeBackgroundCommand(String command, String resultText) {
        executeCommand(command);

        new GuiRobot().waitForEvent(() -> !getResultDisplay().getText().equals(resultText),
                GuiRobot.NETWORK_ACTION_TIMEOUT_MILLISECONDS);
        new GuiRobot().waitForEvent(() -> getCommandBox().isEnabled(), GuiRobot.NETWORK_ACTION_TIMEOUT_MILLISECONDS);
    }

    /**
     * Displays all books in the book shelf.
     */
    protected void showAllBooks() {
        executeCommand(ListCommand.COMMAND_WORD);
        assertEquals(getModel().getBookShelf().size(), getModel().getDisplayBookList().size());
    }

    /**
     * Selects the book at {@code index} of the book list.
     */
    protected void selectBook(Index index) {
        executeCommand(SelectCommand.COMMAND_WORD + " " + index.getOneBased());
        assertEquals(index.getZeroBased(), getBookListPanel().getSelectedCardIndex());
    }

    /**
     * Selects the book at {@code index} of the search result list.
     */
    protected void selectSearchResult(Index index) {
        executeCommand(SelectCommand.COMMAND_WORD + " " + index.getOneBased());
        assertEquals(index.getZeroBased(), getSearchResultsPanel().getSelectedCardIndex());
    }

    /**
     * Selects the book at {@code index} of the recently selected books list.
     */
    protected void selectRecentBooks(Index index) {
        executeCommand(SelectCommand.COMMAND_WORD + " " + index.getOneBased());
        assertEquals(index.getZeroBased(), getRecentBooksPanel().getSelectedCardIndex());
    }

    /**
     * Deletes all books in the book shelf.
     */
    protected void deleteAllBooks() {
        executeCommand(ClearCommand.COMMAND_WORD);
        assertEquals(0, getModel().getBookShelf().size());
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Model}, {@code Storage}, {@code BookListPanel}, and {@code SearchResultsPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by {@code assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see #assertApplicationDisplaysExpected(String, String, Model)
     */
    protected void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedBookListCardUnchanged();
        assertSelectedSearchResultsCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Asserts that the {@code CommandBox} displays {@code expectedCommandInput}, the {@code ResultDisplay} displays
     * {@code expectedResultMessage}, the model and storage contains the same book objects as {@code expectedModel}
     * and the currently displayed list displays the books in the model correctly.
     */
    protected void assertApplicationDisplaysExpected(String expectedCommandInput, String expectedResultMessage,
            Model expectedModel) {
        assertEquals(expectedCommandInput, getCommandBox().getInput());
        assertEquals(expectedResultMessage, getResultDisplay().getText());
        assertEquals(expectedModel, getModel());
        assertEquals(expectedModel.getBookShelf(), testApp.readStorageBookShelf());
        if (getBookListPanel().isVisible()) {
            assertListMatching(getBookListPanel(), expectedModel.getDisplayBookList());
        }
        if (getSearchResultsPanel().isVisible()) {
            assertListMatching(getSearchResultsPanel(), expectedModel.getSearchResultsList());
        }
        if (getRecentBooksPanel().isVisible()) {
            assertListMatching(getRecentBooksPanel(), expectedModel.getRecentBooksList());
        }
    }

    /**
     * Asserts that the alias list panel displays the aliases in the model correctly.
     */
    protected void assertAliasListDisplaysExpected(Model expectedModel) {
        assertListMatching(getAliasListPanel(), expectedModel.getDisplayAliasList());
    }

    /**
     * Calls {@code BookListPanelHandle}, {@code SearchResultsPanelHandle},  {@code StatusBarFooterHandle},
     * and {@code BookDetailsPanelHandle} to remember their current state.
     */
    private void rememberStates() {
        StatusBarFooterHandle statusBarFooterHandle = getStatusBarFooter();
        statusBarFooterHandle.rememberSaveLocation();
        statusBarFooterHandle.rememberSyncStatus();
        getBookListPanel().rememberSelectedBookCard();
        getSearchResultsPanel().rememberSelectedBookCard();
        getRecentBooksPanel().rememberSelectedBookCard();
        getBookDetailsPanel().rememberIsbn();
        getBookDetailsPanel().rememberVisibility();
    }

    /**
     * Asserts that the book details panel remains displaying the details of the previously selected book.
     * @see BookDetailsPanelHandle#isIsbnChanged() and BookDetailsPanelHandle#isVisibilityChanged()
     */
    protected void assertBookDetailsPanelUnchanged() {
        assertFalse(getBookDetailsPanel().isIsbnChanged()
                || getBookDetailsPanel().isVisibilityChanged());
    }

    /**
     * Asserts that the previously selected book list card is now deselected.
     */
    protected void assertSelectedBookListCardDeselected() {
        assertFalse(getBookListPanel().isAnyCardSelected());
    }

    /**
     * Asserts that the book details panel displays the details of the book in the book list panel at
     * {@code expectedSelectedCardIndex}, and only the card at {@code expectedSelectedCardIndex} is selected.
     * @see BookListPanelHandle#isSelectedBookCardChanged()
     */
    protected void assertSelectedBookListCardChanged(Index expectedSelectedCardIndex) {
        Book selectedBook = getModel().getDisplayBookList().get(expectedSelectedCardIndex.getZeroBased());
        assertDetailsPanelDisplaysBook(selectedBook, getBookDetailsPanel());
        assertEquals(expectedSelectedCardIndex.getZeroBased(), getBookListPanel().getSelectedCardIndex());
    }

    /**
     * Asserts that the previously selected search results card is now deselected.
     */
    protected void assertSelectedSearchResultsCardDeselected() {
        assertFalse(getSearchResultsPanel().isAnyCardSelected());
    }

    /**
     * Asserts that the book details panel displays the details of the book in the search results panel at
     * {@code expectedSelectedCardIndex}, and only the card at {@code expectedSelectedCardIndex} is selected.
     * @see SearchResultsPanelHandle#isSelectedBookCardChanged()
     */
    protected void assertSelectedSearchResultsCardChanged(Index expectedSelectedCardIndex) {
        Book selectedBook = getModel().getSearchResultsList().get(expectedSelectedCardIndex.getZeroBased());
        assertDetailsPanelDisplaysBook(selectedBook, getBookDetailsPanel());
        assertEquals(expectedSelectedCardIndex.getZeroBased(), getSearchResultsPanel().getSelectedCardIndex());
    }

    /**
     * Asserts that the previously selected recent books card is now deselected.
     */
    protected void assertSelectedRecentBooksCardDeselected() {
        assertFalse(getRecentBooksPanel().isAnyCardSelected());
    }

    /**
     * Asserts that the book details panel displays the details of the book in the recent books panel at
     * {@code expectedSelectedCardIndex}, and only the card at {@code expectedSelectedCardIndex} is selected.
     * @see RecentBooksPanelHandle#isSelectedBookCardChanged()
     */
    protected void assertSelectedRecentBooksCardChanged(Index expectedSelectedCardIndex) {
        Book selectedBook = getModel().getRecentBooksList().get(expectedSelectedCardIndex.getZeroBased());
        assertDetailsPanelDisplaysBook(selectedBook, getBookDetailsPanel());
        assertEquals(expectedSelectedCardIndex.getZeroBased(), getRecentBooksPanel().getSelectedCardIndex());
    }

    /**
     * Asserts that the selected card in the book list panel remain unchanged and the book details panel
     * remains displaying the details of the previously selected book.
     * @see BookDetailsPanelHandle#isIsbnChanged()
     * @see BookListPanelHandle#isSelectedBookCardChanged()
     */
    protected void assertSelectedBookListCardUnchanged() {
        assertFalse(getBookDetailsPanel().isIsbnChanged());
        assertFalse(getBookListPanel().isSelectedBookCardChanged());
    }

    /**
     * Asserts that the selected card in the search results panel remain unchanged and the book details panel
     * remains displaying the details of the previously selected book.
     * @see BookDetailsPanelHandle#isIsbnChanged()
     * @see SearchResultsPanelHandle#isSelectedBookCardChanged()
     */
    protected void assertSelectedSearchResultsCardUnchanged() {
        assertFalse(getBookDetailsPanel().isIsbnChanged());
        assertFalse(getSearchResultsPanel().isSelectedBookCardChanged());
    }

    /**
     * Asserts that the selected card in the recent books panel remain unchanged and the book details panel
     * remains displaying the details of the previously selected book.
     * @see BookDetailsPanelHandle#isIsbnChanged()
     * @see RecentBooksPanelHandle#isSelectedBookCardChanged()
     */
    protected void assertSelectedRecentBooksCardUnchanged() {
        assertFalse(getBookDetailsPanel().isIsbnChanged());
        assertFalse(getRecentBooksPanel().isSelectedBookCardChanged());
    }

    /**
     * Asserts that the command box's shows the default style.
     */
    protected void assertCommandBoxShowsDefaultStyle() {
        assertEquals(COMMAND_BOX_DEFAULT_STYLE, getCommandBox().getStyleClass());
    }

    /**
     * Asserts that the command box's shows the error style.
     */
    protected void assertCommandBoxShowsErrorStyle() {
        assertEquals(COMMAND_BOX_ERROR_STYLE, getCommandBox().getStyleClass());
    }

    /**
     * Asserts that the command box is disabled.
     */
    protected void assertCommandBoxDisabled() {
        assertFalse(getCommandBox().isEnabled());
    }

    /**
     * Asserts that the command box is enabled.
     */
    protected void assertCommandBoxEnabled() {
        assertTrue(getCommandBox().isEnabled());
    }

    /**
     * Asserts that the entire status bar remains the same.
     */
    protected void assertStatusBarUnchanged() {
        StatusBarFooterHandle handle = getStatusBarFooter();
        assertFalse(handle.isSaveLocationChanged());
        assertFalse(handle.isSyncStatusChanged());
    }

    /**
     * Asserts that only the sync status in the status bar was changed to the timing of
     * {@code ClockRule#getInjectedClock()}, while the save location remains the same.
     */
    protected void assertStatusBarUnchangedExceptSyncStatus() {
        StatusBarFooterHandle handle = getStatusBarFooter();
        String timestamp = new Date(clockRule.getInjectedClock().millis()).toString();
        String expectedSyncStatus = String.format(SYNC_STATUS_UPDATED, timestamp);
        assertEquals(expectedSyncStatus, handle.getSyncStatus());
        assertFalse(handle.isSaveLocationChanged());
    }

    /**
     * Asserts that the starting state of the application is correct.
     */
    private void assertApplicationStartingStateIsCorrect() {
        try {
            assertEquals("", getCommandBox().getInput());
            assertEquals("", getResultDisplay().getText());
            assertListMatching(getBookListPanel(), getModel().getDisplayBookList());
            assertTrue(getWelcomePanel().isVisible());
            assertFalse(getBookDetailsPanel().isVisible());
            assertFalse(getBookReviewsPanel().isVisible());
            assertFalse(getAliasListPanel().isVisible());
            assertEquals("./" + testApp.getStorageSaveLocation(), getStatusBarFooter().getSaveLocation());
            assertEquals(SYNC_STATUS_INITIAL, getStatusBarFooter().getSyncStatus());
        } catch (Exception e) {
            throw new AssertionError("Starting state is wrong.", e);
        }
    }

}
