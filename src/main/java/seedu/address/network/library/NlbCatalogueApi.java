package seedu.address.network.library;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import seedu.address.model.book.Book;
import seedu.address.network.HttpClient;
import seedu.address.network.HttpResponse;

//@@author qiu-siqi
/**
 * Provides access to the NLB catalogue.
 */
public class NlbCatalogueApi {

    private static final String SEARCH_URL =
            "https://catalogue.nlb.gov.sg/cgi-bin/spydus.exe/ENQ/EXPNOS/BIBENQ?ENTRY=%t %a&ENTRY_NAME=BS"
                    + "&ENTRY_TYPE=K&GQ=%t %a&SORTS=SQL_REL_TITLE";
    private static final String CONTENT_TYPE_HTML = "text/html";
    private static final int HTTP_STATUS_OK = 200;

    private final HttpClient httpClient;

    public NlbCatalogueApi(HttpClient httpClient) {
        requireNonNull(httpClient);
        this.httpClient = httpClient;
    }

    /**
     * Searches for a book in NLB catalogue.
     *
     * @param book book to search for.
     * @return CompleteableFuture which resolves to a single book page.
     */
    public CompletableFuture<String> searchForBook(Book book) {
        requireNonNull(book);
        return execute(makeSearchUrl(book), book);
    }

    /**
     * Obtains the search URL for a particular {@code book} to search for.
     */
    protected static String makeSearchUrl(Book book) {
        requireNonNull(book);
        return SEARCH_URL.replace("%t", book.getTitle().toString()).replace("%a", book.getAuthorsAsString());
    }

    /**
     * Asynchronously executes a HTTP GET request to the specified url to find the specified book.
     *
     * @param url the url used for the GET request.
     * @param book the book to search for.
     * @return CompleteableFuture which resolves to a single book page.
     */
    private CompletableFuture<String> execute(String url, Book book) {
        return httpClient
                .makeGetRequest(url)
                .thenApply(NlbCatalogueApi::requireHtmlContentType)
                .thenApply(NlbCatalogueApi::requireHttpStatusOk)
                .thenApply(HttpResponse::getResponseBody)
                .thenApply(result -> NlbResultHelper.getUrl(result, book));
    }

    /**
     * Throws a {@link CompletionException} if the content type of the response is not HTML.
     */
    private static HttpResponse requireHtmlContentType(HttpResponse response) {
        if (!response.getContentType().startsWith(CONTENT_TYPE_HTML)) {
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
