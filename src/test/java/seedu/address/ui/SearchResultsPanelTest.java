package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.EventsUtil.postNow;
import static seedu.address.testutil.TypicalBooks.getTypicalBooks;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_BOOK;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysBook;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardEquals;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.BookCardHandle;
import guitests.guihandles.SearchResultsPanelHandle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.events.ui.JumpToSearchResultsIndexRequestEvent;
import seedu.address.model.book.Book;

public class SearchResultsPanelTest extends GuiUnitTest {
    private static final ObservableList<Book> TYPICAL_BOOKS =
            FXCollections.observableList(getTypicalBooks());

    private static final JumpToSearchResultsIndexRequestEvent JUMP_TO_SECOND_EVENT =
            new JumpToSearchResultsIndexRequestEvent(INDEX_SECOND_BOOK);

    private SearchResultsPanelHandle searchResultsPanelHandle;

    @Before
    public void setUp() {
        SearchResultsPanel searchResultsPanel = new SearchResultsPanel(TYPICAL_BOOKS);
        uiPartRule.setUiPart(searchResultsPanel);

        searchResultsPanelHandle = new SearchResultsPanelHandle(getChildNode(searchResultsPanel.getRoot(),
                SearchResultsPanelHandle.SEARCH_RESULTS_LIST_VIEW_ID));
    }

    @Test
    public void display() {
        for (int i = 0; i < TYPICAL_BOOKS.size(); i++) {
            searchResultsPanelHandle.navigateToCard(TYPICAL_BOOKS.get(i));
            Book expectedBook = TYPICAL_BOOKS.get(i);
            BookCardHandle actualCard = searchResultsPanelHandle.getBookCardHandle(i).get();

            assertCardDisplaysBook(expectedBook, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    public void handleJumpToSearchResultsIndexRequestEvent() {
        postNow(JUMP_TO_SECOND_EVENT);
        guiRobot.pauseForHuman();

        BookCardHandle expectedCard =
                searchResultsPanelHandle.getBookCardHandle(INDEX_SECOND_BOOK.getZeroBased()).get();
        BookCardHandle selectedCard = searchResultsPanelHandle.getHandleToSelectedCard();
        assertCardEquals(expectedCard, selectedCard);
    }
}
