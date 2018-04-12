package seedu.address.network.api.google;

import java.io.IOException;
import java.util.concurrent.CompletionException;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.ReadOnlyBookShelf;
import seedu.address.model.book.Book;
import seedu.address.model.book.exceptions.InvalidBookException;

//@@author takuyakanbr
/**
 * Provides utilities to deserialize JSON responses.
 */
public class JsonDeserializer {

    private static final Logger logger = LogsCenter.getLogger(JsonDeserializer.class);

    private final ObjectMapper mapper;

    JsonDeserializer() {
        mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Converts the JSON string from Google Books API into a book.
     */
    public Book convertJsonStringToBook(String json) {
        try {
            JsonBookDetails jsonBookDetails = mapper.readValue(json, JsonBookDetails.class);
            return jsonBookDetails.toModelType();
        } catch (IOException | InvalidBookException e) {
            logger.warning("Failed to convert JSON to book.");
            throw new CompletionException(e);
        }
    }

    /**
     * Converts the JSON string from Google Books API into a book shelf,
     * with the specified limit on the number of books to populate the book shelf with.
     */
    public ReadOnlyBookShelf convertJsonStringToBookShelf(String json, int maxBookCount) {
        assert maxBookCount >= 0;
        try {
            JsonSearchResults jsonSearchResults = mapper.readValue(json, JsonSearchResults.class);
            return jsonSearchResults.toModelType(maxBookCount);
        } catch (IOException e) {
            logger.warning("Failed to convert JSON to book shelf.");
            throw new CompletionException(e);
        }
    }
}
