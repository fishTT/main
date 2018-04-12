package seedu.address.network;

import java.util.concurrent.CompletableFuture;

import seedu.address.model.ReadOnlyBookShelf;
import seedu.address.model.book.Book;

//@@author takuyakanbr
/**
 * The API of the Network component.
 */
public interface Network {

    /**
     * Searches for books on Google Books that matches a set of parameters.
     *
     * @param parameters search parameters.
     * @return a CompletableFuture that resolves to a ReadOnlyBookShelf.
     */
    CompletableFuture<ReadOnlyBookShelf> searchBooks(String parameters);

    /**
     * Retrieves the details of a single book on Google Books.
     *
     * @param bookId the ID of the book on Google Books.
     * @return a CompletableFuture that resolves to a Book.
     */
    CompletableFuture<Book> getBookDetails(String bookId);

    /**
     * Searches for a book in the library.
     *
     * @param book book to search for.
     * @return CompletableFuture that resolves to a String with the search results.
     */
    CompletableFuture<String> searchLibraryForBook(Book book);

    /**
     * Stops the Network component.
     */
    void stop();
}
