package seedu.address.network.library;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.util.FileUtil;
import seedu.address.network.HttpClient;
import seedu.address.network.HttpResponse;
import seedu.address.network.api.google.JsonDeserializerTest;
import seedu.address.testutil.TypicalBooks;

//@@author qiu-siqi
public class NlbCatalogueApiTest {

    private static final File VALID_RESPONSE_BRIEF_DISPLAY =
            new File("src/test/data/NlbResultTest/ValidResponseBriefDisplay.html");
    private static final String VALID_RESPONSE_BRIEF_DISPLAY_URL =
            "https://catalogue.nlb.gov.sg/cgi-bin/spydus.exe/FULL/EXPNOS/BIBENQ/6585278/226559740,1";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private NlbCatalogueApi nlbCatalogueApi;
    private HttpClient mockClient;

    @Before
    public void setUp() {
        mockClient = mock(HttpClient.class);
        nlbCatalogueApi = new NlbCatalogueApi(mockClient);
    }

    @Test
    public void makeSearchUrl_validBook_success() {
        assertEquals("https://catalogue.nlb.gov.sg/cgi-bin/spydus.exe/ENQ/EXPNOS/BIBENQ?ENTRY=Artemis+Andy+Weir"
                + "&ENTRY_NAME=BS&ENTRY_TYPE=K&GQ=Artemis+Andy+Weir&SORTS=SQL_REL_TITLE",
                NlbCatalogueApi.makeSearchUrl(TypicalBooks.ARTEMIS).replaceAll(" ", "+"));
    }

    @Test
    public void searchForBooks_validParam_success() throws IOException {
        when(mockClient.makeGetRequest(NlbCatalogueApi.makeSearchUrl(TypicalBooks.ARTEMIS)))
                .thenReturn(makeFutureResponse(200, FileUtil.readFromFile(VALID_RESPONSE_BRIEF_DISPLAY)));

        String result = nlbCatalogueApi.searchForBook(TypicalBooks.ARTEMIS).join();

        verify(mockClient).makeGetRequest(NlbCatalogueApi.makeSearchUrl(TypicalBooks.ARTEMIS));
        assertEquals(VALID_RESPONSE_BRIEF_DISPLAY_URL, result);
    }

    @Test
    public void searchForBooks_badResponseType_throwsCompletionException() {
        when(mockClient.makeGetRequest(NlbCatalogueApi.makeSearchUrl(TypicalBooks.ARTEMIS)))
                .thenReturn(makeFutureResponse(503, ""));

        thrown.expect(CompletionException.class);
        nlbCatalogueApi.searchForBook(TypicalBooks.ARTEMIS).join();
    }

    @Test
    public void searchForBooks_badReturnType_throwsCompletionException() throws IOException {
        when(mockClient.makeGetRequest(NlbCatalogueApi.makeSearchUrl(TypicalBooks.ARTEMIS)))
                .thenReturn(makeFutureResponse(200, "application/json",
                        FileUtil.readFromFile(JsonDeserializerTest.VALID_SEARCH_RESPONSE_FILE)));

        thrown.expect(CompletionException.class);
        nlbCatalogueApi.searchForBook(TypicalBooks.ARTEMIS).join();
    }

    /**
     * Returns a {@link CompletableFuture} that resolves to a {@link HttpResponse} of content type HTML.
     */
    private static CompletableFuture<HttpResponse> makeFutureResponse(int code, String response) {
        return makeFutureResponse(code, "text/html;", response);
    }

    /**
     * Returns a {@link CompletableFuture} that resolves to a {@link HttpResponse}.
     */
    private static CompletableFuture<HttpResponse> makeFutureResponse(int code, String contentType, String response) {
        return CompletableFuture.completedFuture(new HttpResponse(code, contentType, response));
    }
}
