package seedu.address.network;

import java.util.concurrent.ExecutionException;

import org.asynchttpclient.Dsl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class HttpClientTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new HttpClient(null);
    }

    @Test
    public void makeGetRequest_invalidUrl_throwsExecutionException() throws ExecutionException, InterruptedException {
        HttpClient httpClient = new HttpClient(Dsl.asyncHttpClient());
        thrown.expect(ExecutionException.class);
        httpClient.makeGetRequest("http://this.is.an.invalid.url").get();
    }

    @Test
    public void close_executedMultipleTimes_success() {
        HttpClient httpClient = new HttpClient(Dsl.asyncHttpClient());
        httpClient.close();
        httpClient.close();
        httpClient.close();
        // should not throw any exceptions
    }
}
