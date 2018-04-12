package seedu.address.ui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalBooks.ARTEMIS;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import guitests.GuiRobot;
import guitests.guihandles.BookReviewsPanelHandle;

public class BookReviewsPanelTest extends GuiUnitTest {

    private static final String URL_GOODREADS_ARTEMIS = "https://www.goodreads.com/book/show/35098840-artemis";

    private BookReviewsPanel bookReviewsPanel;
    private BookReviewsPanelHandle bookReviewsPanelHandle;

    @Before
    public void setUp() {
        guiRobot.interact(() -> bookReviewsPanel = new BookReviewsPanel());
        uiPartRule.setUiPart(bookReviewsPanel);
        bookReviewsPanelHandle = new BookReviewsPanelHandle(bookReviewsPanel.getRoot());
    }

    @Test
    public void display() throws Exception {
        // hidden by default
        assertFalse(bookReviewsPanelHandle.isVisible());

        // associated reviews page of a book
        new GuiRobot().interact(() -> bookReviewsPanel.loadPageForBook(ARTEMIS));
        String expectedUrl =
                new URL(BookReviewsPanel.SEARCH_PAGE_URL.replace("%isbn", ARTEMIS.getIsbn().isbn)).toExternalForm();
        String loadedUrl = bookReviewsPanelHandle.getLoadedUrl().toExternalForm();
        assertTrue(loadedUrl.equals(expectedUrl) || loadedUrl.startsWith(URL_GOODREADS_ARTEMIS));
    }

}
