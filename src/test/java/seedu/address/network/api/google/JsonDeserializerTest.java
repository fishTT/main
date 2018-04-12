package seedu.address.network.api.google;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.concurrent.CompletionException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.util.FileUtil;
import seedu.address.model.ReadOnlyBookShelf;
import seedu.address.model.book.Book;

//@@author takuyakanbr
public class JsonDeserializerTest {
    private static final String TEST_DATA_BOOK_DETAILS_FOLDER =
            FileUtil.getPath("src/test/data/JsonDeserializerTest/bookDetails/");
    private static final File ERROR_BOOK_DETAILS_RESPONSE_FILE =
            new File(TEST_DATA_BOOK_DETAILS_FOLDER + "ErrorResponse.json");
    private static final File INVALID_BOOK_DETAILS_RESPONSE_NO_ISBN_FILE =
            new File(TEST_DATA_BOOK_DETAILS_FOLDER + "InvalidResponseNoIsbn.json");
    private static final File INVALID_BOOK_DETAILS_RESPONSE_WRONG_TYPE_FILE =
            new File(TEST_DATA_BOOK_DETAILS_FOLDER + "InvalidResponseWrongType.json");
    private static final File VALID_BOOK_DETAILS_RESPONSE_NO_DESC_FILE =
            new File(TEST_DATA_BOOK_DETAILS_FOLDER + "ValidResponseNoDesc.json");
    protected static final File VALID_BOOK_DETAILS_RESPONSE_FILE =
            new File(TEST_DATA_BOOK_DETAILS_FOLDER + "ValidResponse.json");

    private static final String TEST_DATA_SEARCH_FOLDER =
            FileUtil.getPath("src/test/data/JsonDeserializerTest/search/");
    private static final File ERROR_SEARCH_RESPONSE_FILE = new File(TEST_DATA_SEARCH_FOLDER + "ErrorResponse.json");
    private static final File INVALID_SEARCH_RESPONSE_NO_ISBN_FILE =
            new File(TEST_DATA_SEARCH_FOLDER + "InvalidResponseNoIsbn.json");
    private static final File INVALID_SEARCH_RESPONSE_WRONG_TYPE_FILE =
            new File(TEST_DATA_SEARCH_FOLDER + "InvalidResponseWrongType.json");
    private static final File VALID_SEARCH_RESPONSE_NO_ID_FILE =
            new File(TEST_DATA_SEARCH_FOLDER + "ValidResponseNoDesc.json");
    private static final File VALID_SEARCH_RESPONSE_DUPLICATE_BOOKS =
            new File(TEST_DATA_SEARCH_FOLDER + "ValidResponseDuplicateBooks.json");
    public static final File VALID_SEARCH_RESPONSE_FILE = new File(TEST_DATA_SEARCH_FOLDER + "ValidResponse.json");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private JsonDeserializer deserializer = new JsonDeserializer();


    @Test
    public void convertJsonStringToBook_validResponse_success() throws Exception {
        String json = FileUtil.readFromFile(VALID_BOOK_DETAILS_RESPONSE_FILE);
        Book book = deserializer.convertJsonStringToBook(json);
        assertEquals("The Book Without a Title", book.getTitle().title);
        assertEquals("This is a valid description.", book.getDescription().description);
    }

    @Test
    public void convertJsonStringToBook_validResponseNoDesc_success() throws Exception {
        String json = FileUtil.readFromFile(VALID_BOOK_DETAILS_RESPONSE_NO_DESC_FILE);
        Book book = deserializer.convertJsonStringToBook(json);
        assertEquals("The Book Without a Title", book.getTitle().title);
        assertEquals("", book.getDescription().description);
    }

    @Test
    public void convertJsonStringToBook_invalidResponseWrongType_throwsCompletionException() throws Exception {
        thrown.expect(CompletionException.class);
        String json = FileUtil.readFromFile(INVALID_BOOK_DETAILS_RESPONSE_WRONG_TYPE_FILE);
        deserializer.convertJsonStringToBook(json);
    }

    @Test
    public void convertJsonStringToBook_invalidResponseNoIsbn_throwsCompletionException() throws Exception {
        thrown.expect(CompletionException.class);
        String json = FileUtil.readFromFile(INVALID_BOOK_DETAILS_RESPONSE_NO_ISBN_FILE);
        deserializer.convertJsonStringToBook(json);
    }

    @Test
    public void convertJsonStringToBook_errorResponse_throwsCompletionException() throws Exception {
        thrown.expect(CompletionException.class);
        String json = FileUtil.readFromFile(ERROR_BOOK_DETAILS_RESPONSE_FILE);
        deserializer.convertJsonStringToBook(json);
    }


    @Test
    public void convertJsonStringToBookShelf_validResponse_success() throws Exception {
        String json = FileUtil.readFromFile(VALID_SEARCH_RESPONSE_FILE);
        ReadOnlyBookShelf bookShelf = deserializer.convertJsonStringToBookShelf(json);
        Book book1 = bookShelf.getBookList().get(0);
        assertEquals(3, bookShelf.size());
        assertEquals("The Book Without a Title", book1.getTitle().title);
        assertEquals("This is a valid description.", book1.getDescription().description);
    }

    @Test
    public void convertJsonStringToBookShelf_validResponseDuplicateBooks_success() throws Exception {
        String json = FileUtil.readFromFile(VALID_SEARCH_RESPONSE_DUPLICATE_BOOKS);
        ReadOnlyBookShelf bookShelf = deserializer.convertJsonStringToBookShelf(json);
        Book book1 = bookShelf.getBookList().get(0);
        assertEquals(1, bookShelf.size());
        assertEquals("The Book Without a Title", book1.getTitle().title);
        assertEquals("This is a valid description.", book1.getDescription().description);
    }

    @Test
    public void convertJsonStringToBookShelf_validResponseNoId_success() throws Exception {
        String json = FileUtil.readFromFile(VALID_SEARCH_RESPONSE_NO_ID_FILE);
        ReadOnlyBookShelf bookShelf = deserializer.convertJsonStringToBookShelf(json);
        Book book1 = bookShelf.getBookList().get(0);
        assertEquals("The Book Without a Title", book1.getTitle().title);
        assertEquals("", book1.getDescription().description);
        Book book2 = bookShelf.getBookList().get(1);
        assertEquals("The Book Without a Title 2", book2.getTitle().title);
        assertEquals("", book2.getDescription().description);
    }

    @Test
    public void convertJsonStringToBookShelf_invalidResponseNoIsbn_ignoresBookWithoutIsbn() throws Exception {
        String json = FileUtil.readFromFile(INVALID_SEARCH_RESPONSE_NO_ISBN_FILE);
        ReadOnlyBookShelf bookShelf = deserializer.convertJsonStringToBookShelf(json);
        Book book1 = bookShelf.getBookList().get(0);
        assertEquals("The Book Without a Title 2", book1.getTitle().title);
        Book book2 = bookShelf.getBookList().get(1);
        assertEquals("The Book Without a Title 3", book2.getTitle().title);
    }

    @Test
    public void convertJsonStringToBookShelf_invalidResponseWrongType_throwsCompletionException() throws Exception {
        thrown.expect(CompletionException.class);
        String json = FileUtil.readFromFile(INVALID_SEARCH_RESPONSE_WRONG_TYPE_FILE);
        deserializer.convertJsonStringToBookShelf(json);
    }

    @Test
    public void convertJsonStringToBookShelf_errorResponse_throwsCompletionException() throws Exception {
        thrown.expect(CompletionException.class);
        String json = FileUtil.readFromFile(ERROR_SEARCH_RESPONSE_FILE);
        deserializer.convertJsonStringToBookShelf(json);
    }

}
