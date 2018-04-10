package seedu.address.network.api.google;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.util.FileUtil;
import seedu.address.model.ReadOnlyBookShelf;
import seedu.address.model.book.Book;
import seedu.address.network.HttpClient;
import seedu.address.network.HttpResponse;

//@@author takuyakanbr
public class GoogleBooksApiTest {

    private static final String URL_SEARCH_BOOKS_OK = String.format(GoogleBooksApi.URL_SEARCH_BOOKS, "123");
    private static final String URL_BOOK_DETAILS_OK = String.format(GoogleBooksApi.URL_BOOK_DETAILS, "123");
    private static final String URL_SEARCH_BOOKS_FAIL = String.format(GoogleBooksApi.URL_SEARCH_BOOKS, "");
    private static final String URL_BOOK_DETAILS_FAIL = String.format(GoogleBooksApi.URL_BOOK_DETAILS, "");
    private static final String URL_SEARCH_BOOKS_BAD_RESPONSE = String.format(GoogleBooksApi.URL_SEARCH_BOOKS, "html");
    private static final String URL_BOOK_DETAILS_BAD_RESPONSE = String.format(GoogleBooksApi.URL_BOOK_DETAILS, "html");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private GoogleBooksApi googleBooksApi;
    private HttpClient mockClient;

    @Before
    public void setUp() {
        mockClient = mock(HttpClient.class);
        googleBooksApi = new GoogleBooksApi(mockClient);
    }

    @Test
    public void searchBooks_validParam_success() throws IOException {
        when(mockClient.makeGetRequest(URL_SEARCH_BOOKS_OK))
                .thenReturn(makeFutureResponse(200,
                        FileUtil.readFromFile(JsonDeserializerTest.VALID_SEARCH_RESPONSE_FILE)));

        ReadOnlyBookShelf bookShelf = googleBooksApi.searchBooks("123").join();
        Book book1 = bookShelf.getBookList().get(0);

        verify(mockClient).makeGetRequest(URL_SEARCH_BOOKS_OK);
        assertEquals("The Book Without a Title", book1.getTitle().title);
        assertEquals("This is a valid description.", book1.getDescription().description);
    }

    @Test
    public void searchBooks_invalidParam_throwsCompletionException() {
        when(mockClient.makeGetRequest(URL_SEARCH_BOOKS_FAIL))
                .thenReturn(makeFutureResponse(503, "{ \"error\": { \"code\": 503 } }"));

        thrown.expect(CompletionException.class);
        googleBooksApi.searchBooks("").join();
    }

    @Test
    public void searchBooks_badResponseType_throwsCompletionException() {
        when(mockClient.makeGetRequest(URL_SEARCH_BOOKS_BAD_RESPONSE))
                .thenReturn(makeFutureResponse(503, "text/html;", "{ \"error\": { \"code\": 503 } }"));

        thrown.expect(CompletionException.class);
        googleBooksApi.searchBooks("html").join();
    }

    @Test
    public void getBookDetails_validId_success() throws IOException {
        when(mockClient.makeGetRequest(URL_BOOK_DETAILS_OK))
                .thenReturn(makeFutureResponse(200,
                        FileUtil.readFromFile(JsonDeserializerTest.VALID_BOOK_DETAILS_RESPONSE_FILE)));

        Book book = googleBooksApi.getBookDetails("123").join();

        verify(mockClient).makeGetRequest(URL_BOOK_DETAILS_OK);
        assertEquals("The Book Without a Title", book.getTitle().title);
        assertEquals("This is a valid description.", book.getDescription().description);
    }

    @Test
    public void getBookDetails_invalidId_throwsCompletionException() {
        when(mockClient.makeGetRequest(URL_BOOK_DETAILS_FAIL))
                .thenReturn(makeFutureResponse(503, "{ \"error\": { \"code\": 503 } }"));

        thrown.expect(CompletionException.class);
        googleBooksApi.getBookDetails("").join();
    }

    @Test
    public void getBookDetails_badResponseType_throwsCompletionException() {
        when(mockClient.makeGetRequest(URL_BOOK_DETAILS_BAD_RESPONSE))
                .thenReturn(makeFutureResponse(503, "text/html;", "{ \"error\": { \"code\": 503 } }"));

        thrown.expect(CompletionException.class);
        googleBooksApi.getBookDetails("html").join();
    }

    /**
     * Returns a {@link CompletableFuture} that resolves to a {@link HttpResponse} of content type JSON.
     */
    private static CompletableFuture<HttpResponse> makeFutureResponse(int code, String response) {
        return makeFutureResponse(code, "application/json;", response);
    }

    /**
     * Returns a {@link CompletableFuture} that resolves to a {@link HttpResponse}.
     */
    private static CompletableFuture<HttpResponse> makeFutureResponse(int code, String contentType, String response) {
        return CompletableFuture.completedFuture(new HttpResponse(code, contentType, response));
    }

}
