package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import javafx.collections.ObservableList;
import seedu.address.model.book.Book;
import seedu.address.model.book.Isbn;
import seedu.address.model.book.UniqueBookList;
import seedu.address.model.book.exceptions.BookNotFoundException;
import seedu.address.model.book.exceptions.DuplicateBookException;

/**
 * Wraps all data at the book-shelf level
 * Duplicates are not allowed (by .equals comparison)
 */
public class BookShelf implements ReadOnlyBookShelf {

    private final UniqueBookList books;

    public BookShelf() {
        books = new UniqueBookList();
    }

    /**
     * Creates a BookShelf using the Books in the {@code toBeCopied}
     */
    public BookShelf(ReadOnlyBookShelf toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    public void setBooks(Collection<Book> books) throws DuplicateBookException {
        this.books.setBooks(books);
    }

    /**
     * Resets the existing data of this {@code BookShelf} with {@code newData}.
     */
    public void resetData(ReadOnlyBookShelf newData) {
        requireNonNull(newData);

        try {
            setBooks(newData.getBookList());
        } catch (DuplicateBookException e) {
            throw new AssertionError("BookShelf should not have duplicate books");
        }
    }

    //// book-level operations

    @Override
    public Optional<Book> getBookByIsbn(Isbn isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return Optional.of(book);
            }
        }
        return Optional.empty();
    }

    /**
     * Adds a book to the book shelf.
     *
     * @throws DuplicateBookException if an equivalent book already exists.
     */
    public void addBook(Book book) throws DuplicateBookException {
        books.add(book);
    }

    /**
     * Replaces the given book {@code target} in the list with {@code editedBook}.
     *
     * @throws BookNotFoundException  if {@code target} could not be found in the list.
     * @throws DuplicateBookException if updating the book's details causes the book to be equivalent to
     *                                another existing book in the list.
     */
    public void updateBook(Book target, Book editedBook)
        throws BookNotFoundException, DuplicateBookException {
        requireNonNull(editedBook);
        books.setBook(target, editedBook);
    }

    /**
     * Removes {@code key} from this {@code BookShelf}.
     *
     * @throws BookNotFoundException if the {@code key} is not in this {@code BookShelf}.
     */
    public boolean removeBook(Book key) throws BookNotFoundException {
        return books.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return books.asObservableList().size() + " books";
        // TODO: refine later
    }

    @Override
    public ObservableList<Book> getBookList() {
        return books.asObservableList();
    }

    @Override
    public int size() {
        return getBookList().size();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof BookShelf // instanceof handles nulls
            && this.books.equalsOrderInsensitive(((BookShelf) other).books));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(books);
    }
}
