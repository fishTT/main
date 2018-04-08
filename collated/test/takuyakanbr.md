# takuyakanbr
###### \java\seedu\address\logic\commands\ListCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalBookShelf(), new UserPrefs());
    }

    @Test
    public void execute_noParameterSpecified_success() {
        // empty filter, default comparator -> shows everything
        assertExecutionSuccess(new FilterDescriptor(), Model.DEFAULT_BOOK_COMPARATOR, 5);
        assertEquals(BABYLON_ASHES, model.getDisplayBookList().get(0));
        assertEquals(COLLAPSING_EMPIRE, model.getDisplayBookList().get(1));
        assertEquals(CONSIDER_PHLEBAS, model.getDisplayBookList().get(2));
        assertEquals(WAKING_GODS, model.getDisplayBookList().get(3));
        assertEquals(ARTEMIS, model.getDisplayBookList().get(4));

        // filtered list -> shows everything
        showBookAtIndex(model, INDEX_FIRST_BOOK);
        assertExecutionSuccess(new FilterDescriptor(), Model.DEFAULT_BOOK_COMPARATOR, 5);
        assertEquals(BABYLON_ASHES, model.getDisplayBookList().get(0));
        assertEquals(COLLAPSING_EMPIRE, model.getDisplayBookList().get(1));
        assertEquals(CONSIDER_PHLEBAS, model.getDisplayBookList().get(2));
        assertEquals(WAKING_GODS, model.getDisplayBookList().get(3));
        assertEquals(ARTEMIS, model.getDisplayBookList().get(4));
    }

    @Test
    public void execute_allParametersSpecified_success() {
        // matches 1 book: ARTEMIS
        FilterDescriptor descriptor = new FilterDescriptorBuilder().withTitleFilter(ARTEMIS.getTitle().title)
                .withAuthorFilter("andy weir").withCategoryFilter("fiction").withStatusFilter(Status.READ)
                .withPriorityFilter(Priority.LOW).withRatingFilter(new Rating(5)).build();
        assertExecutionSuccess(descriptor, SortMode.STATUSD.getComparator(), 1);
        assertEquals(ARTEMIS, model.getDisplayBookList().get(0));

        // matches 0 books
        descriptor = new FilterDescriptorBuilder().withTitleFilter(ARTEMIS.getTitle().title)
                .withAuthorFilter("andy weir").withCategoryFilter("fiction").withStatusFilter(Status.UNREAD)
                .withPriorityFilter(Priority.HIGH).withRatingFilter(new Rating(-1)).build();
        assertExecutionSuccess(descriptor, SortMode.PRIORITYD.getComparator(), 0);
    }

    @Test
    public void execute_someParametersSpecified_success() {
        // matches 2 books: CONSIDER_PHLEBAS, WAKING_GODS
        FilterDescriptor descriptor = new FilterDescriptorBuilder()
                .withStatusFilter(Status.UNREAD).withRatingFilter(new Rating(-1)).build();
        assertExecutionSuccess(descriptor, SortMode.TITLE.getComparator(), 2);
        assertEquals(CONSIDER_PHLEBAS, model.getDisplayBookList().get(0));
        assertEquals(WAKING_GODS, model.getDisplayBookList().get(1));

        // matches 2 books: WAKING_GODS, CONSIDER_PHLEBAS
        descriptor = new FilterDescriptorBuilder()
                .withRatingFilter(new Rating(-1)).withStatusFilter(Status.UNREAD).build();
        assertExecutionSuccess(descriptor, SortMode.TITLED.getComparator(), 2);
        assertEquals(WAKING_GODS, model.getDisplayBookList().get(0));
        assertEquals(CONSIDER_PHLEBAS, model.getDisplayBookList().get(1));

        // matches 2 books: ARTEMIS, COLLAPSING_EMPIRE
        descriptor = new FilterDescriptorBuilder().withTitleFilter("em").build();
        assertExecutionSuccess(descriptor, SortMode.RATINGD.getComparator(), 2);
        assertEquals(ARTEMIS, model.getDisplayBookList().get(0));
        assertEquals(COLLAPSING_EMPIRE, model.getDisplayBookList().get(1));

        // matches 0 books
        descriptor = new FilterDescriptorBuilder()
                .withCategoryFilter("space").withStatusFilter(Status.READING).withRatingFilter(new Rating(3)).build();
        assertExecutionSuccess(descriptor, Model.DEFAULT_BOOK_COMPARATOR, 0);
    }

    @Test
    public void equals() {
        FilterDescriptor descriptorA = new FilterDescriptorBuilder()
                .withAuthorFilter("a1").withStatusFilter(Status.UNREAD).build();
        FilterDescriptor descriptorB = new FilterDescriptorBuilder()
                .withTitleFilter("t1").withAuthorFilter("a2").withPriorityFilter(Priority.HIGH).build();
        ListCommand standardCommand = prepareCommand(descriptorA, SortMode.STATUS.getComparator());

        // same values -> returns true
        FilterDescriptor sameDescriptor = new FilterDescriptorBuilder()
                .withAuthorFilter("a1").withStatusFilter(Status.UNREAD).build();
        ListCommand commandWithSameValues = prepareCommand(sameDescriptor, SortMode.STATUS.getComparator());
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different filter descriptor -> returns false
        assertFalse(standardCommand.equals(new ListCommand(descriptorB, SortMode.STATUS.getComparator())));

        // different comparator -> returns false
        assertFalse(standardCommand.equals(new ListCommand(descriptorA, SortMode.RATING.getComparator())));
    }

    /**
     * Executes a {@code ListCommand} with the given {@code descriptor} and {@code comparator}, and checks that
     * the resulting display book list contains the expected number of books.
     */
    private void assertExecutionSuccess(FilterDescriptor descriptor, Comparator<Book> comparator, int expectedBooks) {
        ListCommand command = prepareCommand(descriptor, comparator);
        CommandResult result = command.execute();
        assertEquals(expectedBooks, model.getDisplayBookList().size());
        assertEquals(String.format(ListCommand.MESSAGE_SUCCESS, expectedBooks), result.feedbackToUser);
    }

    private ListCommand prepareCommand(FilterDescriptor descriptor, Comparator<Book> comparator) {
        ListCommand listCommand = new ListCommand(descriptor, comparator);
        listCommand.setData(model, mock(NetworkManager.class), new CommandHistory(), new UndoStack());
        return listCommand;
    }
}
```
###### \java\seedu\address\logic\commands\SearchCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) and unit tests for SearchCommand.
 */
public class SearchCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalBookShelf(), new UserPrefs());
    }

    @Test
    public void constructor_nullDescriptor_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new SearchCommand(null);
    }

    @Test
    public void execute_allFieldsSpecifiedWithSearchTerm_success() {
        SearchDescriptor searchDescriptor = new SearchDescriptorBuilder().withTitle("1")
                .withCategory("1").withIsbn("1").withAuthor("1").withSearchTerm("searchterm").build();
        assertExecutionSuccess(searchDescriptor);
    }

    @Test
    public void execute_allFieldsSpecifiedNoSearchTerm_success() {
        SearchDescriptor searchDescriptor = new SearchDescriptorBuilder().withTitle("1")
                .withCategory("1").withIsbn("1").withAuthor("1").build();
        assertExecutionSuccess(searchDescriptor);
    }

    @Test
    public void execute_someFieldsSpecifiedNoSearchTerm_success() {
        SearchDescriptor searchDescriptor = new SearchDescriptorBuilder().withTitle("1").withIsbn("1").build();
        assertExecutionSuccess(searchDescriptor);
    }

    @Test
    public void execute_noFieldSpecifiedNoSearchTerm_success() {
        SearchDescriptor searchDescriptor = new SearchDescriptorBuilder().build();
        assertExecutionSuccess(searchDescriptor);
    }

    @Test
    public void equals() {
        SearchDescriptor descriptorA =
                new SearchDescriptorBuilder().withAuthor("author1").withIsbn("12345").build();
        SearchDescriptor descriptorB =
                new SearchDescriptorBuilder().withAuthor("author2").withIsbn("12345").withTitle("title2").build();
        final SearchCommand standardCommand = prepareCommand(descriptorA);

        // same values -> returns true
        SearchDescriptorBuilder copyDescriptor = new SearchDescriptorBuilder(descriptorA);
        SearchCommand commandWithSameValues = prepareCommand(copyDescriptor.build());
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new SearchCommand(descriptorB)));
    }

    /**
     * Executes a {@code SearchCommand} with the given {@code descriptor}, and checks that
     * {@code network.searchBooks(params)} is being called with the correct search parameters.
     */
    private void assertExecutionSuccess(SearchDescriptor descriptor) {
        SearchCommand searchCommand = new SearchCommand(descriptor, false);

        NetworkManager networkManagerMock = mock(NetworkManager.class);
        when(networkManagerMock.searchBooks(descriptor.toSearchString()))
                .thenReturn(CompletableFuture.completedFuture(new BookShelf()));

        searchCommand.setData(model, networkManagerMock, new CommandHistory(), new UndoStack());
        Model expectedModel = new ModelManager(new BookShelf(model.getBookShelf()), new UserPrefs());
        assertCommandSuccess(searchCommand, model, SearchCommand.MESSAGE_SEARCHING, expectedModel);

        verify(networkManagerMock).searchBooks(descriptor.toSearchString());
    }

    private SearchCommand prepareCommand(SearchDescriptor descriptor) {
        SearchCommand searchCommand = new SearchCommand(descriptor, false);
        searchCommand.setData(model, mock(NetworkManager.class), new CommandHistory(), new UndoStack());
        return searchCommand;
    }
}
```
###### \java\seedu\address\logic\parser\ListCommandParserTest.java
``` java
public class ListCommandParserTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ListCommandParser parser = new ListCommandParser();

    @Test
    public void parse_noParameterSpecified_success() {
        ListCommand expectedCommand = new ListCommand(new FilterDescriptor(), Model.DEFAULT_BOOK_COMPARATOR);
        assertParseSuccess(parser, "", expectedCommand);
    }

    @Test
    public void parse_allParametersSpecified_success() {
        String userInput = " t/t1 a/a1 c/c1 s/read p/low r/0 by/title";
        FilterDescriptor descriptor = new FilterDescriptorBuilder().withTitleFilter("t1")
                .withAuthorFilter("a1").withCategoryFilter("c1").withStatusFilter(Status.READ)
                .withPriorityFilter(Priority.LOW).withRatingFilter(new Rating(0)).build();
        ListCommand expectedCommand = new ListCommand(descriptor, SortMode.TITLE.getComparator());
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someParametersSpecified_success() {
        String userInput = " by/ratingd s/read p/low t/t1";
        FilterDescriptor descriptor = new FilterDescriptorBuilder().withTitleFilter("t1")
                .withStatusFilter(Status.READ).withPriorityFilter(Priority.LOW).build();
        ListCommand expectedCommand = new ListCommand(descriptor, SortMode.RATINGD.getComparator());
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFilterSpecified_success() {
        String userInput = " t/t1";
        FilterDescriptor descriptor = new FilterDescriptorBuilder().withTitleFilter("t1").build();
        ListCommand expectedCommand = new ListCommand(descriptor, Model.DEFAULT_BOOK_COMPARATOR);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = " a/a1";
        descriptor = new FilterDescriptorBuilder().withAuthorFilter("a1").build();
        expectedCommand = new ListCommand(descriptor, Model.DEFAULT_BOOK_COMPARATOR);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = " c/c1";
        descriptor = new FilterDescriptorBuilder().withCategoryFilter("c1").build();
        expectedCommand = new ListCommand(descriptor, Model.DEFAULT_BOOK_COMPARATOR);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = " s/read";
        descriptor = new FilterDescriptorBuilder().withStatusFilter(Status.READ).build();
        expectedCommand = new ListCommand(descriptor, Model.DEFAULT_BOOK_COMPARATOR);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = " p/low";
        descriptor = new FilterDescriptorBuilder().withPriorityFilter(Priority.LOW).build();
        expectedCommand = new ListCommand(descriptor, Model.DEFAULT_BOOK_COMPARATOR);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = " r/0";
        descriptor = new FilterDescriptorBuilder().withRatingFilter(new Rating(0)).build();
        expectedCommand = new ListCommand(descriptor, Model.DEFAULT_BOOK_COMPARATOR);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFilters_acceptsLast() {
        String userInput = " s/unread s/rd t/hello t/world t/t1 s/read";
        FilterDescriptor descriptor = new FilterDescriptorBuilder()
                .withTitleFilter("t1").withStatusFilter(Status.READ).build();
        ListCommand expectedCommand = new ListCommand(descriptor, Model.DEFAULT_BOOK_COMPARATOR);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_validSortBy_success() {
        // select sorting mode using its name
        String userInput = " by/title";
        ListCommand expectedCommand = new ListCommand(new FilterDescriptor(), SortMode.TITLE.getComparator());
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = " by/ratingd";
        expectedCommand = new ListCommand(new FilterDescriptor(), SortMode.RATINGD.getComparator());
        assertParseSuccess(parser, userInput, expectedCommand);

        // select sorting mode using its alias
        userInput = " by/p";
        expectedCommand = new ListCommand(new FilterDescriptor(), SortMode.PRIORITY.getComparator());
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = " by/rd";
        expectedCommand = new ListCommand(new FilterDescriptor(), SortMode.RATINGD.getComparator());
        assertParseSuccess(parser, userInput, expectedCommand);

        // sorting mode selection should be case insensitive
        userInput = " by/StatusD";
        expectedCommand = new ListCommand(new FilterDescriptor(), SortMode.STATUSD.getComparator());
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = " by/PA";
        expectedCommand = new ListCommand(new FilterDescriptor(), SortMode.PRIORITY.getComparator());
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedSortBy_acceptsLast() {
        String userInput = " by/title by/rating by/priority by/status";
        ListCommand expectedCommand = new ListCommand(new FilterDescriptor(), SortMode.STATUS.getComparator());
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidFilterOrSortBy_failure() {
        assertParseFailure(parser, " s/readd", Messages.MESSAGE_INVALID_STATUS);
        assertParseFailure(parser, " t/title p/", Messages.MESSAGE_INVALID_PRIORITY);
        assertParseFailure(parser, " p/123", Messages.MESSAGE_INVALID_PRIORITY);
        assertParseFailure(parser, " r/-2", Messages.MESSAGE_INVALID_RATING);
        assertParseFailure(parser, " r/zero c/category", Messages.MESSAGE_INVALID_RATING);

        assertParseFailure(parser, " by/author", Messages.MESSAGE_INVALID_SORT_BY);
        assertParseFailure(parser, " by/", Messages.MESSAGE_INVALID_SORT_BY);
        assertParseFailure(parser, " by/1", Messages.MESSAGE_INVALID_SORT_BY);

        // multiple invalid items -> show first error message from list [status, priority, rating, sort by]
        assertParseFailure(parser, " s/readd p/ r/zero p/123 s/123 by/1", Messages.MESSAGE_INVALID_STATUS);
        assertParseFailure(parser, " by/1 r/zero r/-2 p/123 p/", Messages.MESSAGE_INVALID_PRIORITY);
    }
}
```
###### \java\seedu\address\logic\parser\SearchCommandParserTest.java
``` java
public class SearchCommandParserTest {
    private static final String DEFAULT_SEARCH_TERM = "search term";

    private SearchCommandParser parser = new SearchCommandParser();

    @Test
    public void parse_noFieldSpecified_failure() {
        // no search term and no parameters specified
        assertParseFailure(parser, "", SearchCommand.MESSAGE_EMPTY_QUERY);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        String userInput = DEFAULT_SEARCH_TERM + TITLE_DESC_ARTEMIS + CATEGORY_DESC_ARTEMIS
                + ISBN_DESC_ARTEMIS + AUTHOR_DESC_ARTEMIS;
        SearchDescriptor searchDescriptor = new SearchDescriptorBuilder().withTitle(VALID_TITLE_ARTEMIS)
                .withCategory(VALID_CATEGORY_ARTEMIS).withIsbn(VALID_ISBN_ARTEMIS)
                .withAuthor(VALID_AUTHOR_ARTEMIS).withSearchTerm(DEFAULT_SEARCH_TERM).build();
        SearchCommand expectedCommand = new SearchCommand(searchDescriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        String userInput = DEFAULT_SEARCH_TERM + TITLE_DESC_ARTEMIS + AUTHOR_DESC_ARTEMIS;
        SearchDescriptor searchDescriptor = new SearchDescriptorBuilder().withTitle(VALID_TITLE_ARTEMIS)
                .withAuthor(VALID_AUTHOR_ARTEMIS).withSearchTerm(DEFAULT_SEARCH_TERM).build();
        SearchCommand expectedCommand = new SearchCommand(searchDescriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        String userInput = DEFAULT_SEARCH_TERM;
        SearchDescriptor searchDescriptor = new SearchDescriptorBuilder().withSearchTerm(DEFAULT_SEARCH_TERM).build();
        SearchCommand expectedCommand = new SearchCommand(searchDescriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = AUTHOR_DESC_ARTEMIS;
        searchDescriptor = new SearchDescriptorBuilder().withAuthor(VALID_AUTHOR_ARTEMIS).build();
        expectedCommand = new SearchCommand(searchDescriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = CATEGORY_DESC_ARTEMIS;
        searchDescriptor = new SearchDescriptorBuilder().withCategory(VALID_CATEGORY_ARTEMIS).build();
        expectedCommand = new SearchCommand(searchDescriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = ISBN_DESC_ARTEMIS;
        searchDescriptor = new SearchDescriptorBuilder().withIsbn(VALID_ISBN_ARTEMIS).build();
        expectedCommand = new SearchCommand(searchDescriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = TITLE_DESC_ARTEMIS;
        searchDescriptor = new SearchDescriptorBuilder().withTitle(VALID_TITLE_ARTEMIS).build();
        expectedCommand = new SearchCommand(searchDescriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        String userInput = DEFAULT_SEARCH_TERM + TITLE_DESC_ARTEMIS + TITLE_DESC_ARTEMIS + AUTHOR_DESC_ARTEMIS
                + AUTHOR_DESC_ARTEMIS + TITLE_DESC_BABYLON + AUTHOR_DESC_BABYLON;
        SearchDescriptor searchDescriptor = new SearchDescriptorBuilder().withTitle(VALID_TITLE_BABYLON)
                .withAuthor(VALID_AUTHOR_BABYLON).withSearchTerm(DEFAULT_SEARCH_TERM).build();
        SearchCommand expectedCommand = new SearchCommand(searchDescriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

}
```
###### \java\seedu\address\network\api\google\GoogleBooksApiTest.java
``` java
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
```
###### \java\seedu\address\network\api\google\JsonDeserializerTest.java
``` java
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
```
###### \java\seedu\address\network\NetworkManagerTest.java
``` java
public class NetworkManagerTest {
    private static final String PARAM_EMPTY_RESULT = "1";
    private static final String PARAM_SUCCESS = "12345";
    private static final String PARAM_FAILURE = "failure";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private NetworkManager networkManager;
    private GoogleBooksApi mockGoogleBooksApi;

    @Before
    public void setUp() {
        mockGoogleBooksApi = mock(GoogleBooksApi.class);
        networkManager = new NetworkManager(mock(HttpClient.class), mockGoogleBooksApi);
    }

    @Test
    public void handleGoogleApiSearchRequestEvent_emptyResult() throws Exception {
        when(mockGoogleBooksApi.searchBooks(PARAM_EMPTY_RESULT))
                .thenReturn(CompletableFuture.completedFuture(new BookShelf()));

        ReadOnlyBookShelf bookShelf = networkManager.searchBooks(PARAM_EMPTY_RESULT).get();

        verify(mockGoogleBooksApi).searchBooks(PARAM_EMPTY_RESULT);
        assertEquals(0, bookShelf.size());
    }

    @Test
    public void handleGoogleApiSearchRequestEvent_success() throws Exception {
        when(mockGoogleBooksApi.searchBooks(PARAM_SUCCESS))
                .thenReturn(CompletableFuture.completedFuture(TypicalBooks.getTypicalBookShelf()));

        ReadOnlyBookShelf bookShelf = networkManager.searchBooks(PARAM_SUCCESS).get();

        verify(mockGoogleBooksApi).searchBooks(PARAM_SUCCESS);
        assertEquals(5, bookShelf.size());
    }

    @Test
    public void handleGoogleApiSearchRequestEvent_failure() throws Exception {
        when(mockGoogleBooksApi.searchBooks(PARAM_FAILURE))
                .thenReturn(getFailedFuture());

        CompletableFuture<ReadOnlyBookShelf> bookShelf = networkManager.searchBooks(PARAM_FAILURE);
        verify(mockGoogleBooksApi).searchBooks(PARAM_FAILURE);

        thrown.expect(ExecutionException.class);
        bookShelf.get();
    }

    @Test
    public void handleGoogleApiBookDetailsRequestEvent_success() throws Exception {
        when(mockGoogleBooksApi.getBookDetails(PARAM_SUCCESS))
                .thenReturn(CompletableFuture.completedFuture(TypicalBooks.ARTEMIS));

        Book book = networkManager.getBookDetails(PARAM_SUCCESS).get();

        verify(mockGoogleBooksApi).getBookDetails(PARAM_SUCCESS);
        assertEquals(TypicalBooks.ARTEMIS, book);
    }

    @Test
    public void handleGoogleApiBookDetailsRequestEvent_failure() throws Exception {
        when(mockGoogleBooksApi.getBookDetails(PARAM_FAILURE))
                .thenReturn(getFailedFuture());

        CompletableFuture<Book> book = networkManager.getBookDetails(PARAM_FAILURE);
        verify(mockGoogleBooksApi).getBookDetails(PARAM_FAILURE);

        thrown.expect(ExecutionException.class);
        book.get();
    }

    /**
     * Returns a {@link CompletableFuture} that has already completed exceptionally.
     */
    private static <T> CompletableFuture<T> getFailedFuture() {
        return CompletableFuture.completedFuture(null).thenApply(obj -> {
            throw new CompletionException(new IOException());
        });
    }

}
```
###### \java\systemtests\ListCommandSystemTest.java
``` java
public class ListCommandSystemTest extends BibliotekSystemTest {

    @Test
    public void list() {
        /* ----------------------------------- Perform valid list operations ---------------------------------------- */

        /* Case: valid filters mode -> 1 book listed */
        assertListSuccess(ListCommand.COMMAND_WORD + " t/artemis a/andy weir c/fiction s/read p/low r/5", ARTEMIS);

        /* Case: valid filters mode -> 0 books listed */
        assertListSuccess(ListCommand.COMMAND_WORD + " c/space s/reading r/3");

        executeCommand(RecentCommand.COMMAND_WORD);

        /* Case: valid filters and sort mode -> 2 books listed */
        assertListSuccess(ListCommand.COMMAND_WORD + " s/u r/-1 by/title", CONSIDER_PHLEBAS, WAKING_GODS);

        /* Case: no parameters -> all 5 books listed */
        assertListSuccess(ListCommand.COMMAND_WORD,
                BABYLON_ASHES, COLLAPSING_EMPIRE, CONSIDER_PHLEBAS, WAKING_GODS, ARTEMIS);

        /* ----------------------------------- Perform invalid list operations -------------------------------------- */

        /* Case: invalid status filter -> rejected */
        assertCommandFailure(ListCommand.COMMAND_WORD + " s/", Messages.MESSAGE_INVALID_STATUS);

        /* Case: invalid priority and rating filter -> rejected */
        assertCommandFailure(ListCommand.COMMAND_WORD + " p/null r/-100", Messages.MESSAGE_INVALID_PRIORITY);

        /* Case: invalid rating filter -> rejected */
        assertCommandFailure(ListCommand.COMMAND_WORD + " r/20", Messages.MESSAGE_INVALID_RATING);

        /* Case: invalid sort mode -> rejected */
        assertCommandFailure(ListCommand.COMMAND_WORD + " by/123", Messages.MESSAGE_INVALID_SORT_BY);
    }

```
###### \java\systemtests\SearchCommandSystemTest.java
``` java
public class SearchCommandSystemTest extends BibliotekSystemTest {
    @Test
    public void search() throws Exception {
        /* ----------------------------------- Perform invalid search operations ------------------------------------ */

        /* Case: no search term or parameters -> rejected */
        assertCommandFailure(SearchCommand.COMMAND_WORD, SearchCommand.MESSAGE_EMPTY_QUERY);

        /* Case: no search term or parameters -> rejected */
        assertCommandFailure("   " + SearchCommand.COMMAND_WORD + "             ", SearchCommand.MESSAGE_EMPTY_QUERY);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("SeaRcH hello", MESSAGE_UNKNOWN_COMMAND);

        /* Case: misspelled command word -> rejected */
        assertCommandFailure("searchh hello", MESSAGE_UNKNOWN_COMMAND);

        /* ----------------------------------- Perform valid search operations -------------------------------------- */

        // Note: these tests require network connection.

        /* Case: search for books given search term -> success */
        assertSearchSuccess(SearchCommand.COMMAND_WORD + " hello");

        /* Case: search for books given search parameters -> success */
        assertSearchSuccess(SearchCommand.COMMAND_WORD + TITLE_DESC_ARTEMIS + CATEGORY_DESC_ARTEMIS
                + AUTHOR_DESC_ARTEMIS);

        /* Case: search for non-existant book -> return 0 results */
        assertSearchSuccess(SearchCommand.COMMAND_WORD + TITLE_DESC_ARTEMIS + CATEGORY_DESC_ARTEMIS
                + ISBN_DESC_ARTEMIS + AUTHOR_DESC_ARTEMIS);

        /* ----------------------------------- Perform commands on search results ----------------------------------- */

        /* Case: trying to clear or delete result items -> rejected */
        assertCommandFailure(ClearCommand.COMMAND_WORD, ClearCommand.MESSAGE_WRONG_ACTIVE_LIST);
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " 1", DeleteCommand.MESSAGE_WRONG_ACTIVE_LIST);
    }

```
###### \java\systemtests\ThemeCommandSystemTest.java
``` java
public class ThemeCommandSystemTest extends BibliotekSystemTest {

    @Test
    public void theme() {
        /* ----------------------------------- Perform invalid theme operations ------------------------------------- */

        /* Case: no theme name -> rejected */
        assertCommandFailure(ThemeCommand.COMMAND_WORD,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ThemeCommand.MESSAGE_USAGE));

        /* Case: invalid theme name -> rejected */
        assertCommandFailure(ThemeCommand.COMMAND_WORD + " 12345",
                String.format(ThemeCommand.MESSAGE_INVALID_THEME, "12345"));

        /* Case: invalid theme name -> rejected */
        assertCommandFailure(ThemeCommand.COMMAND_WORD + " light 123",
                String.format(ThemeCommand.MESSAGE_INVALID_THEME, "light 123"));

        /* ----------------------------------- Perform valid theme operations --------------------------------------- */

        assertThemeCommandSuccess(ThemeCommand.COMMAND_WORD + " light", Theme.LIGHT);
        assertThemeCommandSuccess(ThemeCommand.COMMAND_WORD + " DARK", Theme.DARK);
        assertThemeCommandSuccess(ThemeCommand.COMMAND_WORD + " WhitE", Theme.WHITE);
    }

```
