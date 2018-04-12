package seedu.address.model;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.model.alias.Alias;
import seedu.address.model.alias.ReadOnlyAliasList;
import seedu.address.model.book.Book;
import seedu.address.model.book.exceptions.BookNotFoundException;
import seedu.address.model.book.exceptions.DuplicateBookException;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Book> PREDICATE_SHOW_ALL_BOOKS = unused -> true;

    /**
     * Default book {@code Comparator} that sorts by status (in descending order: Reading, Unread, Read),
     * priority (High, Medium, Low, None), and finally title.
     * */
    Comparator<Book> DEFAULT_BOOK_COMPARATOR = (book1, book2) -> {
        int statusComparison = book2.getStatus().compareTo(book1.getStatus());
        if (statusComparison != 0) {
            return statusComparison;
        }
        int priorityComparison =  book2.getPriority().compareTo(book1.getPriority());
        if (priorityComparison != 0) {
            return priorityComparison;
        }
        return book1.getTitle().compareTo(book2.getTitle());
    };

    /** Returns the type of list that is currently active. */
    ActiveListType getActiveListType();

    /** Returns an unmodifiable view of the list that is currently active. */
    ObservableList<Book> getActiveList();

    /** Sets the type of list that is currently active. */
    void setActiveListType(ActiveListType type);

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyBookShelf newData);

    /** Returns the BookShelf */
    ReadOnlyBookShelf getBookShelf();

    /** Deletes the given book. */
    void deleteBook(Book target) throws BookNotFoundException;

    /** Adds the given book */
    void addBook(Book book) throws DuplicateBookException;

    /**
     * Replaces the given book {@code target} with {@code editedBook}.
     *
     * @throws BookNotFoundException if {@code target} could not be found in the list.
     * @throws DuplicateBookException if updating the book details causes the book to be equivalent to
     *      another existing book in the list.
     */
    void updateBook(Book target, Book editedBook)
            throws BookNotFoundException, DuplicateBookException;

    //=========== Display Book List Accessors ==============================================================

    /** Returns an unmodifiable view of the filtered and sorted book list */
    ObservableList<Book> getDisplayBookList();

    /**
     * Returns the predicate used for filtering the book list.
     */
    Predicate<? super Book> getBookListFilter();

    /**
     * Returns the comparator used for sorting the book list.
     */
    Comparator<? super Book> getBookListSorter();

    /**
     * Updates the filter of the filtered book list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateBookListFilter(Predicate<? super Book> predicate);

    /**
     * Updates the comparator of the sorted book list to sort by the given {@code comparator}.
     * @throws NullPointerException if {@code comparator} is null.
     */
    void updateBookListSorter(Comparator<? super Book> comparator);

    //=========== Search Results ===========================================================================

    /** Returns an unmodifiable view of the search results. */
    ObservableList<Book> getSearchResultsList();

    /** Updates the search results that should be displayed. */
    void updateSearchResults(ReadOnlyBookShelf newResults);

    //=========== Recent books ===========================================================================

    /** Returns an unmodifiable view of the recently selected books. */
    ObservableList<Book> getRecentBooksList();

    /** Returns the recently selected books as a ReadOnlyBookShelf*/
    ReadOnlyBookShelf getRecentBooksListAsBookShelf();

    /** Add a recently selected book. */
    void addRecentBook(Book newBook);

    //=========== Alias List =============================================================================

    /** Returns the alias list. */
    ReadOnlyAliasList getAliasList();

    /** Returns an unmodifiable view of the alias list. */
    ObservableList<Alias> getDisplayAliasList();

    /** Adds the specified {@code alias} to the alias list. */
    void addAlias(Alias alias);

    /**
     * Returns an {@code Optional} containing the alias that matches the specified {@code name}.
     * Returns an empty {@code Optional} if no matching alias was found.
     */
    Optional<Alias> getAlias(String name);

    /** Removes the alias that matches the specified {@code name} from the alias list. */
    void removeAlias(String name);
}
