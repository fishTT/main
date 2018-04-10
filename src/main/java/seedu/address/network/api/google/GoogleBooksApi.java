package seedu.address.network.api.google;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Function;

import seedu.address.commons.util.StringUtil;
import seedu.address.model.ReadOnlyBookShelf;
import seedu.address.model.book.Book;
import seedu.address.network.HttpClient;
import seedu.address.network.HttpResponse;

//@@author takuyakanbr
/**
 * Provides access to the Google Books API.
 */
public class GoogleBooksApi {

    protected static final String URL_SEARCH_BOOKS =
            "https://www.googleapis.com/books/v1/volumes?maxResults=30&printType=books&q=%s";
    protected static final String URL_BOOK_DETAILS = "https://www.googleapis.com/books/v1/volumes/%s";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final int HTTP_STATUS_OK = 200;

    private final HttpClient httpClient;
    private final JsonDeserializer deserializer;

    public GoogleBooksApi(HttpClient httpClient) {
        requireNonNull(httpClient);
        this.httpClient = httpClient;
        this.deserializer = new JsonDeserializer();
    }

    /**
     * Searches for books on Google Books that matches a set of parameters.
     *
     * @param parameters search parameters.
     * @return a CompletableFuture that resolves to a ReadOnlyBookShelf.
     */
    public CompletableFuture<ReadOnlyBookShelf> searchBooks(String parameters) {
        String requestUrl = String.format(URL_SEARCH_BOOKS, StringUtil.urlEncode(parameters));
        return executeGetAndApply(requestUrl, deserializer::convertJsonStringToBookShelf);
    }

    /**
     * Retrieves the details of a single book on Google Books.
     *
     * @param bookId the ID of the book on Google Books.
     * @return a CompletableFuture that resolves to a Book.
     */
    public CompletableFuture<Book> getBookDetails(String bookId) {
        String requestUrl = String.format(URL_BOOK_DETAILS, StringUtil.urlEncode(bookId));
        return executeGetAndApply(requestUrl, deserializer::convertJsonStringToBook);
    }

    /**
     * Asynchronously executes a HTTP GET request to the specified url and
     * applies the specified function to transform the resulting response body.
     *
     * @param url the url used for the GET request.
     * @param fn the function that will be applied on the response body of the GET request.
     * @param <T> the return type of the function to be applied.
     * @return a CompleteableFuture that resolves to the result of the given function.
     */
    private <T> CompletableFuture<T> executeGetAndApply(String url, Function<String, ? extends T> fn) {
        return httpClient
                .makeGetRequest(url)
                .thenApply(GoogleBooksApi::requireJsonContentType)
                .thenApply(GoogleBooksApi::requireHttpStatusOk)
                .thenApply(HttpResponse::getResponseBody)
                .thenApply(fn);
    }

    /**
     * Throws a {@link CompletionException} if the content type of the response is not JSON.
     */
    private static HttpResponse requireJsonContentType(HttpResponse response) {
        if (!response.getContentType().startsWith(CONTENT_TYPE_JSON)) {
            throw new CompletionException(
                    new IOException("Unexpected content type " + response.getContentType()));
        }
        return response;
    }

    /**
     * Throws a {@link CompletionException} if the HTTP status code of the response is not {@code 200: OK}.
     */
    private static HttpResponse requireHttpStatusOk(HttpResponse response) {
        if (response.getStatusCode() != HTTP_STATUS_OK) {
            throw new CompletionException(
                    new IOException("Get request failed with status code " + response.getStatusCode()));
        }
        return response;
    }
}
