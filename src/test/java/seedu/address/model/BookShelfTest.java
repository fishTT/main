package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.address.testutil.TypicalBooks.ARTEMIS;
import static seedu.address.testutil.TypicalBooks.BABYLON_ASHES;
import static seedu.address.testutil.TypicalBooks.WAKING_GODS;
import static seedu.address.testutil.TypicalBooks.getTypicalBookShelf;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.book.Book;
import seedu.address.model.book.Isbn;
import seedu.address.model.book.exceptions.BookNotFoundException;

public class BookShelfTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private BookShelf bookShelf;

    @Before
    public void setUp() {
        bookShelf = new BookShelf();
    }

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), bookShelf.getBookList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        bookShelf.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyBookShelf_replacesData() {
        ReadOnlyBookShelf newData = getTypicalBookShelf();
        bookShelf.resetData(newData);
        assertEquals(newData, bookShelf);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsAssertionError() {
        // Repeat ARTEMIS twice
        List<Book> newBooks = Arrays.asList(ARTEMIS, ARTEMIS);
        BookShelfStub newData = new BookShelfStub(newBooks);

        thrown.expect(AssertionError.class);
        bookShelf.resetData(newData);
    }

    @Test
    public void getBookByIsbn_matchingIsbn_success() throws Exception {
        bookShelf.addBook(ARTEMIS);
        bookShelf.addBook(BABYLON_ASHES);
        assertEquals(BABYLON_ASHES, bookShelf.getBookByIsbn(BABYLON_ASHES.getIsbn()).get());
    }

    @Test
    public void getBookByIsbn_nonMatchingIsbn_returnsEmptyOptional() throws Exception {
        bookShelf.addBook(ARTEMIS);
        assertFalse(bookShelf.getBookByIsbn(BABYLON_ASHES.getIsbn()).isPresent());
    }

    @Test
    public void updateBook_validTargetAndReplacement_success() throws Exception {
        bookShelf.addBook(ARTEMIS);
        bookShelf.updateBook(ARTEMIS, BABYLON_ASHES);
        assertEquals(false, bookShelf.getBookList().contains(ARTEMIS));
        assertEquals(true, bookShelf.getBookList().contains(BABYLON_ASHES));
    }

    @Test
    public void updateBook_nonMatchingTarget_throwsBookNotFoundException() throws Exception {
        bookShelf.addBook(ARTEMIS);
        thrown.expect(BookNotFoundException.class);
        bookShelf.updateBook(BABYLON_ASHES, WAKING_GODS);
    }

    @Test
    public void updateBook_nullTarget_throwsBookNotFoundException() throws Exception {
        bookShelf.addBook(ARTEMIS);
        thrown.expect(BookNotFoundException.class);
        bookShelf.updateBook(null, WAKING_GODS);
    }

    @Test
    public void updateBook_nullReplacement_throwsNullPointerException() throws Exception {
        bookShelf.addBook(ARTEMIS);
        thrown.expect(NullPointerException.class);
        bookShelf.updateBook(ARTEMIS, null);
    }

    @Test
    public void removeBook_validBook_success() throws Exception {
        bookShelf.addBook(ARTEMIS);
        bookShelf.removeBook(ARTEMIS);
        assertEquals(0, bookShelf.size());
    }

    @Test
    public void removeBook_nonMatchingBook_throwsBookNotFoundException() throws Exception {
        bookShelf.addBook(ARTEMIS);
        thrown.expect(BookNotFoundException.class);
        bookShelf.removeBook(BABYLON_ASHES);
    }

    @Test
    public void getBookList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        bookShelf.getBookList().remove(0);
    }

    @Test
    public void hashCode_sameContent_returnsSameValue() throws Exception {
        BookShelf bookShelf = new BookShelf();
        bookShelf.addBook(ARTEMIS);
        bookShelf.addBook(BABYLON_ASHES);
        BookShelf bookShelfCopy = new BookShelf();
        bookShelfCopy.addBook(ARTEMIS);
        bookShelfCopy.addBook(BABYLON_ASHES);
        assertEquals(bookShelf.hashCode(), bookShelf.hashCode());
        assertEquals(bookShelf.hashCode(), bookShelfCopy.hashCode());
    }

    /**
     * A stub ReadOnlyBookShelf whose book list can violate interface constraints.
     */
    private static class BookShelfStub implements ReadOnlyBookShelf {
        private final ObservableList<Book> books = FXCollections.observableArrayList();

        BookShelfStub(Collection<Book> books) {
            this.books.setAll(books);
        }

        @Override
        public Optional<Book> getBookByIsbn(Isbn isbn) {
            return Optional.empty();
        }

        @Override
        public ObservableList<Book> getBookList() {
            return books;
        }

        @Override
        public int size() {
            return books.size();
        }
    }

}
