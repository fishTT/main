package seedu.address.ui.testutil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import guitests.guihandles.AliasCardHandle;
import guitests.guihandles.AliasListPanelHandle;
import guitests.guihandles.BookCardHandle;
import guitests.guihandles.BookDetailsPanelHandle;
import guitests.guihandles.BookListPanelHandle;
import guitests.guihandles.RecentBooksPanelHandle;
import guitests.guihandles.ResultDisplayHandle;
import guitests.guihandles.SearchResultsPanelHandle;
import seedu.address.model.alias.Alias;
import seedu.address.model.book.Author;
import seedu.address.model.book.Book;
import seedu.address.model.book.Category;

/**
 * A set of assertion methods useful for writing GUI tests.
 */
public class GuiTestAssert {

    /**
     * Asserts that {@code detailsPanel} displays the details of {@code expectedBook}.
     */
    public static void assertDetailsPanelDisplaysBook(Book expectedBook, BookDetailsPanelHandle detailsPanel) {
        assertEquals(expectedBook.getTitle().toString(), detailsPanel.getTitle());
        assertEquals(expectedBook.getIsbn().toString(), detailsPanel.getIsbn());
        assertEquals(expectedBook.getPublisher().toString(), detailsPanel.getPublisher());
        assertEquals(expectedBook.getPublicationDate().toString(), detailsPanel.getPublicationDate());
        assertEquals(expectedBook.getDescription().toString(), detailsPanel.getDescription());
        assertEquals(expectedBook.getAuthors().stream().map(Author::getDisplayText)
                .collect(Collectors.toList()), detailsPanel.getAuthors());
        assertEquals(expectedBook.getCategories().stream().map(Category::getDisplayText)
                .collect(Collectors.toList()), detailsPanel.getCategories());
    }

    /**
     * Asserts that {@code actualCard} displays the same values as {@code expectedCard}.
     */
    public static void assertCardEquals(BookCardHandle expectedCard, BookCardHandle actualCard) {
        assertTrue(actualCard.equals(expectedCard));
    }

    /**
     * Asserts that {@code actualCard} displays the details of {@code expectedBook}.
     */
    public static void assertCardDisplaysBook(Book expectedBook, BookCardHandle actualCard) {
        assertTrue(actualCard.equals(expectedBook));
    }

    /**
     * Asserts that {@code actualCard} displays the same values as {@code expectedCard}.
     */
    public static void assertAliasCardEquals(AliasCardHandle expectedCard, AliasCardHandle actualCard) {
        assertTrue(actualCard.equals(expectedCard));
    }

    /**
     * Asserts that {@code actualCard} displays the details of {@code expectedAlias}.
     */
    public static void assertAliasCardDisplaysAlias(Alias expectedAlias, AliasCardHandle actualCard) {
        assertTrue(actualCard.equals(expectedAlias));
    }

    /**
     * Asserts that the list in {@code bookListPanelHandle} displays the details of {@code books} correctly and
     * in the correct order.
     */
    public static void assertListMatching(BookListPanelHandle bookListPanelHandle, Book... books) {
        for (int i = 0; i < books.length; i++) {
            bookListPanelHandle.navigateToCard(i);
            assertCardDisplaysBook(books[i], bookListPanelHandle.getBookCardHandle(i).get());
        }
    }

    /**
     * Asserts that the list in {@code searchResultsPanelHandle} displays the details of {@code books} correctly and
     * in the correct order.
     */
    public static void assertListMatching(SearchResultsPanelHandle searchResultsPanelHandle, Book... books) {
        for (int i = 0; i < books.length; i++) {
            searchResultsPanelHandle.navigateToCard(i);
            assertCardDisplaysBook(books[i], searchResultsPanelHandle.getBookCardHandle(i).get());
        }
    }

    /**
     * Asserts that the list in {@code recentBooksPanelHandle} displays the details of {@code books} correctly and
     * in the correct order.
     */
    public static void assertListMatching(RecentBooksPanelHandle recentBooksPanelHandle, Book... books) {
        for (int i = 0; i < books.length; i++) {
            recentBooksPanelHandle.navigateToCard(i);
            assertCardDisplaysBook(books[i], recentBooksPanelHandle.getBookCardHandle(i).get());
        }
    }

    /**
     * Asserts that the list in {@code aliasListPanelHandle} displays the details of {@code aliases} correctly and
     * in the correct order.
     */
    public static void assertListMatching(AliasListPanelHandle aliasListPanelHandle, Alias... aliases) {
        for (int i = 0; i < aliases.length; i++) {
            aliasListPanelHandle.navigateToCard(i);
            assertAliasCardDisplaysAlias(aliases[i], aliasListPanelHandle.getAliasCardHandle(i).get());
        }
    }

    /**
     * Asserts that the list in {@code bookListPanelHandle} displays the details of {@code books} correctly and
     * in the correct order.
     */
    public static void assertListMatching(BookListPanelHandle bookListPanelHandle, List<Book> books) {
        assertListMatching(bookListPanelHandle, books.toArray(new Book[0]));
    }

    /**
     * Asserts that the list in {@code searchResultsPanelHandle} displays the details of {@code books} correctly and
     * in the correct order.
     */
    public static void assertListMatching(SearchResultsPanelHandle searchResultsPanelHandle, List<Book> books) {
        assertListMatching(searchResultsPanelHandle, books.toArray(new Book[0]));
    }

    /**
     * Asserts that the list in {@code recentBooksPanelHandle} displays the details of {@code books} correctly and
     * in the correct order.
     */
    public static void assertListMatching(RecentBooksPanelHandle recentBooksPanelHandle, List<Book> books) {
        assertListMatching(recentBooksPanelHandle, books.toArray(new Book[0]));
    }

    /**
     * Asserts that the list in {@code aliasListPanelHandle} displays the details of {@code aliases} correctly and
     * in the correct order.
     */
    public static void assertListMatching(AliasListPanelHandle aliasListPanelHandle, List<Alias> aliases) {
        assertListMatching(aliasListPanelHandle, aliases.toArray(new Alias[0]));
    }

    /**
     * Asserts the size of the list in {@code bookListPanelHandle} equals to {@code size}.
     */
    public static void assertListSize(BookListPanelHandle bookListPanelHandle, int size) {
        int numberOfBooks = bookListPanelHandle.getListSize();
        assertEquals(size, numberOfBooks);
    }

    /**
     * Asserts the size of the list in {@code searchResultsPanelHandle} equals to {@code size}.
     */
    public static void assertListSize(SearchResultsPanelHandle searchResultsPanelHandle, int size) {
        int numberOfBooks = searchResultsPanelHandle.getListSize();
        assertEquals(size, numberOfBooks);
    }

    /**
     * Asserts the message shown in {@code resultDisplayHandle} equals to {@code expected}.
     */
    public static void assertResultMessage(ResultDisplayHandle resultDisplayHandle, String expected) {
        assertEquals(expected, resultDisplayHandle.getText());
    }
}
