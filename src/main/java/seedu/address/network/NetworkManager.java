package seedu.address.network;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.logging.Logger;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.ReadOnlyBookShelf;
import seedu.address.model.book.Book;
import seedu.address.network.api.google.GoogleBooksApi;

//@@author takuyakanbr
/**
 * Provides networking functionality (making web API calls).
 *
 * The methods on this class (except {@code stop()}) are asynchronous, and returns a
 * {@code CompletableFuture} that will resolve to the desired object or data.
 */
public class NetworkManager extends ComponentManager implements Network {

    private static final Logger logger = LogsCenter.getLogger(NetworkManager.class);

    private final HttpClient httpClient;
    private final GoogleBooksApi googleBooksApi;

    public NetworkManager() {
        super();
        httpClient = new HttpClient();
        googleBooksApi = new GoogleBooksApi(httpClient);
    }

    protected NetworkManager(HttpClient httpClient, GoogleBooksApi googleBooksApi) {
        super();
        requireAllNonNull(httpClient, googleBooksApi);
        this.httpClient = httpClient;
        this.googleBooksApi = googleBooksApi;
    }

    @Override
    public CompletableFuture<ReadOnlyBookShelf> searchBooks(String parameters) {
        return googleBooksApi.searchBooks(parameters)
                .thenApply(bookShelf -> {
                    logger.info("Search books succeeded: " + parameters);
                    return bookShelf;
                })
                .exceptionally(e -> {
                    logger.warning("Search books failed: " + StringUtil.getDetails(e));
                    throw new CompletionException(e);
                });
    }

    @Override
    public CompletableFuture<Book> getBookDetails(String bookId) {
        return googleBooksApi.getBookDetails(bookId)
                .thenApply(book -> {
                    logger.info("Get book details succeeded: " + bookId);
                    return book;
                })
                .exceptionally(e -> {
                    logger.warning("Get book details failed: " + StringUtil.getDetails(e));
                    throw new CompletionException(e);
                });
    }

    @Override
    public void stop() {
        httpClient.close();
    }

}
