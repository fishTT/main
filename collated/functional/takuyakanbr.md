# takuyakanbr
###### \java\seedu\address\commons\core\Theme.java
``` java
/**
 * Represents an application theme.
 */
public enum Theme {
    WHITE("view/WhiteTheme.css"),
    LIGHT("view/LightTheme.css"),
    DARK("view/DarkTheme.css");

    public static final Theme DEFAULT_THEME = WHITE;

    private final String cssFile;

    Theme(String cssFile) {
        this.cssFile = cssFile;
    }

    public String getThemeName() {
        return name().toLowerCase();
    }

    public String getCssFile() {
        return cssFile;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Returns the theme with a theme name that matches the specified {@code themeName}.
     *
     * @throws InvalidThemeException if no matching theme can be found.
     */
    public static Theme getThemeByName(String themeName) throws InvalidThemeException {
        for (Theme theme : values()) {
            if (themeName.equalsIgnoreCase(theme.getThemeName())) {
                return theme;
            }
        }

        throw new InvalidThemeException("Invalid theme name: " + themeName);
    }

}
```
###### \java\seedu\address\logic\commands\ListCommand.java
``` java
/**
 * Lists all books in the book shelf to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Shows a listing of your books, with optional filtering and sorting.\n"
            + "Parameters: "
            + "[" + PREFIX_TITLE + "TITLE] "
            + "[" + PREFIX_AUTHOR + "AUTHOR] "
            + "[" + PREFIX_CATEGORY + "CATEGORY]"
            + "[" + PREFIX_STATUS + "STATUS]"
            + "[" + PREFIX_PRIORITY + "PRIORITY]"
            + "[" + PREFIX_RATING + "RATING]"
            + "[" + PREFIX_SORT_BY + "SORT_BY]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_STATUS + " reading "
            + PREFIX_SORT_BY + "priority";

    public static final String MESSAGE_SUCCESS = "Listed %s books.";

    private final FilterDescriptor filterDescriptor;
    private final Comparator<Book> bookComparator;

    /**
     * Creates a ListCommand to show a listing of books.
     *
     * @param filterDescriptor the filters used for filtering the books to be displayed.
     * @param bookComparator the comparator used for sorting the books to be displayed.
     */
    public ListCommand(FilterDescriptor filterDescriptor, Comparator<Book> bookComparator) {
        requireAllNonNull(filterDescriptor, bookComparator);
        this.filterDescriptor = filterDescriptor;
        this.bookComparator = bookComparator;
    }

    @Override
    public CommandResult execute() {
        model.updateBookListFilter(filterDescriptor.buildCombinedFilter());
        model.updateBookListSorter(bookComparator);
        EventsCenter.getInstance().post(new SwitchToBookListRequestEvent());
        return new CommandResult(String.format(MESSAGE_SUCCESS, model.getDisplayBookList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ListCommand // instanceof handles nulls
                && this.filterDescriptor.equals(((ListCommand) other).filterDescriptor) // state check
                && this.bookComparator.equals(((ListCommand) other).bookComparator));
    }

    /**
     * Stores the filters used for filtering the books to be displayed.
     */
    public static class FilterDescriptor {
        private final List<Predicate<Book>> filters;
        private Set<String> filterCodes; // used internally for equality testing

        public FilterDescriptor() {
            filters = new LinkedList<>();
            filterCodes = new HashSet<>();
        }

        public FilterDescriptor(FilterDescriptor descriptor) {
            filters = new LinkedList<>(descriptor.filters);
            filterCodes = new HashSet<>(descriptor.filterCodes);
        }

        /** Adds a filter that selects books with titles containing the given {@code title}. */
        public void addTitleFilter(String title) {
            filters.add(book -> book.getTitle().title.toLowerCase().contains(title.toLowerCase()));
            filterCodes.add("t[" + title.toLowerCase() + "]");
        }

        /** Adds a filter that selects books with authors containing the given {@code author}. */
        public void addAuthorFilter(String author) {
            filters.add(book -> book.getAuthors().stream()
                    .anyMatch(bookAuthor -> bookAuthor.fullName.toLowerCase().contains(author.toLowerCase())));
            filterCodes.add("a[" + author.toLowerCase() + "]");
        }

        /** Adds a filter that selects books with categories containing the given {@code category}. */
        public void addCategoryFilter(String category) {
            filters.add(book -> book.getCategories().stream()
                    .anyMatch(bookCategory -> bookCategory.category.toLowerCase().contains(category.toLowerCase())));
            filterCodes.add("c[" + category.toLowerCase() + "]");
        }

        /** Adds a filter that selects books with status matching the given {@code status}. */
        public void addStatusFilter(Status status) {
            filters.add(book -> status.equals(book.getStatus()));
            filterCodes.add("s[" + status + "]");
        }

        /** Adds a filter that selects books with priority matching the given {@code priority}. */
        public void addPriorityFilter(Priority priority) {
            filters.add(book -> priority.equals(book.getPriority()));
            filterCodes.add("p[" + priority + "]");
        }

        /** Adds a filter that selects books with ratings matching the given {@code rating}. */
        public void addRatingFilter(Rating rating) {
            filters.add(book -> rating.equals(book.getRating()));
            filterCodes.add("r[" + rating + "]");
        }

        protected Predicate<Book> buildCombinedFilter() {
            List<Predicate<Book>> partialFilters = new LinkedList<>(filters);
            return book -> partialFilters.stream().allMatch(filter -> filter.test(book));
        }

        @Override
        public boolean equals(Object other) {
            return other == this // short circuit if same object
                    || (other instanceof FilterDescriptor // instanceof handles nulls
                    && this.filterCodes.equals(((FilterDescriptor) other).filterCodes)); // state check
        }
    }
}
```
###### \java\seedu\address\logic\commands\SearchCommand.java
``` java
/**
 * Searches for books on Google Books that matches a set of parameters.
 */
public class SearchCommand extends Command {

    public static final String COMMAND_WORD = "search";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Searches for books online.\n"
            + "Parameters: [SEARCH_TERM] "
            + "[" + PREFIX_ISBN + "ISBN] "
            + "[" + PREFIX_TITLE + "TITLE] "
            + "[" + PREFIX_AUTHOR + "AUTHOR] "
            + "[" + PREFIX_CATEGORY + "CATEGORY]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TITLE + " Artemis "
            + PREFIX_AUTHOR + "Andy Weir";

    public static final String MESSAGE_SEARCHING = "Searching for matching books...";
    public static final String MESSAGE_EMPTY_QUERY = "No search term or search parameter specified.";
    public static final String MESSAGE_SEARCH_FAIL = "Failed to retrieve information from online.";
    public static final String MESSAGE_SEARCH_SUCCESS = "Found %s matching books.";

    private final SearchDescriptor searchDescriptor;
    private final boolean useJavafxThread;

    /**
     * Creates a SearchCommand to search for matching books.
     * @param searchDescriptor parameters to search with.
     */
    public SearchCommand(SearchDescriptor searchDescriptor) {
        this(searchDescriptor, true);
    }

    /**
     * Creates a {@code SearchCommand} that can choose not use the JavaFX thread to update the model and UI.
     * This constructor is provided for unit-testing purposes.
     */
    protected SearchCommand(SearchDescriptor searchDescriptor, boolean useJavafxThread) {
        requireNonNull(searchDescriptor);
        this.searchDescriptor = new SearchDescriptor(searchDescriptor);
        this.useJavafxThread = useJavafxThread;
    }

    @Override
    public CommandResult execute() {
        EventsCenter.getInstance().post(new DisableCommandBoxRequestEvent());
        makeAsyncSearchRequest();
        return new CommandResult(MESSAGE_SEARCHING);
    }

    /**
     * Makes an asynchronous request to search for books.
     */
    private void makeAsyncSearchRequest() {
        network.searchBooks(searchDescriptor.toSearchString())
                .thenAccept(this::onSuccessfulRequest)
                .exceptionally(e -> {
                    EventsCenter.getInstance().post(new NewResultAvailableEvent(SearchCommand.MESSAGE_SEARCH_FAIL));
                    EventsCenter.getInstance().post(new EnableCommandBoxRequestEvent());
                    return null;
                });
    }

    /**
     * Handles the result of a successful request to search for books.
     */
    private void onSuccessfulRequest(ReadOnlyBookShelf bookShelf) {
        if (useJavafxThread) {
            Platform.runLater(() -> displaySearchResults(bookShelf));
        } else {
            displaySearchResults(bookShelf);
        }
    }

    /**
     * Updates the model with the given search results and posts events to update the UI.
     */
    private void displaySearchResults(ReadOnlyBookShelf bookShelf) {
        model.updateSearchResults(bookShelf);
        EventsCenter.getInstance().post(new SwitchToSearchResultsRequestEvent());
        EventsCenter.getInstance().post(new NewResultAvailableEvent(
                String.format(SearchCommand.MESSAGE_SEARCH_SUCCESS, bookShelf.size())));
        EventsCenter.getInstance().post(new EnableCommandBoxRequestEvent());
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SearchCommand)) {
            return false;
        }

        // state check
        SearchCommand e = (SearchCommand) other;
        return searchDescriptor.equals(e.searchDescriptor);
    }

    /**
     * Stores the parameters to search with.
     */
    public static class SearchDescriptor {
        private String searchTerm;
        private String isbn;
        private String title;
        private String author;
        private String category;

        public SearchDescriptor() {}

        /**
         * Copy constructor.
         */
        public SearchDescriptor(SearchDescriptor toCopy) {
            this.searchTerm = toCopy.searchTerm;
            this.isbn = toCopy.isbn;
            this.title = toCopy.title;
            this.author = toCopy.author;
            this.category = toCopy.category;
        }

        /**
         * Returns true if at least one field is not empty.
         */
        public boolean isValid() {
            return CollectionUtil.isAnyNonNull(searchTerm, isbn, title, author, category);
        }

        public Optional<String> getSearchTerm() {
            return Optional.ofNullable(searchTerm);
        }

        public void setSearchTerm(String searchTerm) {
            this.searchTerm = searchTerm;
        }

        public Optional<String> getIsbn() {
            return Optional.ofNullable(isbn);
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public Optional<String> getTitle() {
            return Optional.ofNullable(title);
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Optional<String> getAuthor() {
            return Optional.ofNullable(author);
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public Optional<String> getCategory() {
            return Optional.ofNullable(category);
        }

        public void setCategory(String category) {
            this.category = category;
        }

        /** Returns the search string to be used as part of the API url. */
        public String toSearchString() {
            StringBuilder builder = new StringBuilder();
            getSearchTerm().ifPresent(searchTerm -> builder.append(searchTerm).append(" "));
            getIsbn().ifPresent(isbn -> builder.append("isbn:").append(isbn).append(" "));
            getTitle().ifPresent(title -> builder.append("intitle:").append(title).append(" "));
            getAuthor().ifPresent(author -> builder.append("inauthor:").append(author).append(" "));
            getCategory().ifPresent(category -> builder.append("subject:").append(category));
            return builder.toString().trim();
        }

        @Override
        public String toString() {
            return toSearchString();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof SearchDescriptor)) {
                return false;
            }

            // state check
            SearchDescriptor e = (SearchDescriptor) other;

            return getSearchTerm().equals(e.getSearchTerm())
                    && getIsbn().equals(e.getIsbn())
                    && getTitle().equals(e.getTitle())
                    && getAuthor().equals(e.getAuthor())
                    && getCategory().equals(e.getCategory());
        }
    }
}
```
###### \java\seedu\address\network\api\google\GoogleBooksApi.java
``` java
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
```
###### \java\seedu\address\network\api\google\JsonBookDetails.java
``` java
/**
 * A temporary data holder used for deserialization of the JSON response
 * from the book details endpoint of Google Books API.
 */
public class JsonBookDetails {
    private int error = 0;
    private String id = "";
    private JsonVolumeInfo volumeInfo = new JsonVolumeInfo();

    // This should fail if the API returns an error, due to incompatible types.
    public void setError(int error) {
        this.error = error;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVolumeInfo(JsonVolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }

    /**
     * Converts this data holder object into the model's Book object.
     */
    public Book toModelType() throws InvalidBookException {
        Isbn isbn = getIsbnFromIndustryIdentifiers(volumeInfo.industryIdentifiers);

        if (isbn == null) {
            throw new InvalidBookException("No ISBN is found for the book.");
        }

        return new Book(new Gid(id), isbn,
                BookDataUtil.getAuthorList(volumeInfo.authors), new Title(volumeInfo.title),
                getCategoryList(volumeInfo.categories), new Description(volumeInfo.description),
                new Publisher(volumeInfo.publisher),
                new PublicationDate(volumeInfo.publishedDate));
    }

    private static Isbn getIsbnFromIndustryIdentifiers(JsonIndustryIdentifiers[] iiArray) {
        return Stream.of(iiArray)
                .filter(ii -> ii.type.equals("ISBN_13"))
                .findFirst()
                .map(ii -> new Isbn(ii.identifier))
                .orElse(null);
    }

    private static List<Category> getCategoryList(String[] categories) {
        return Stream.of(categories)
                .flatMap(category -> Stream.of(category.split("/")))
                .map(token -> new Category(token.trim()))
                .collect(Collectors.toList());
    }

    /** Temporary data holder used for deserialization. */
    private static class JsonVolumeInfo {
        private String title = "";
        private String[] authors = new String[0];
        private String publisher = "";
        private String publishedDate = "";
        private String description = "";
        private JsonIndustryIdentifiers[] industryIdentifiers = new JsonIndustryIdentifiers[0];
        private String[] categories = new String[0];

```
###### \java\seedu\address\network\api\google\JsonDeserializer.java
``` java
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
     * Converts the JSON string from Google Books API into a book shelf.
     */
    public ReadOnlyBookShelf convertJsonStringToBookShelf(String json) {
        try {
            JsonSearchResults jsonSearchResults = mapper.readValue(json, JsonSearchResults.class);
            return jsonSearchResults.toModelType();
        } catch (IOException e) {
            logger.warning("Failed to convert JSON to book shelf.");
            throw new CompletionException(e);
        }
    }
}
```
###### \java\seedu\address\network\api\google\JsonSearchResults.java
``` java
/**
 * A temporary data holder used for deserialization of the JSON response
 * from the book searching endpoint of Google Books API.
 */
public class JsonSearchResults {

    private static final Logger logger = LogsCenter.getLogger(JsonSearchResults.class);

    private int error = 0;
    private int totalItems = 0;
    private JsonVolume[] items = new JsonVolume[0];

    // This should fail if the API returns an error, due to incompatible types.
    public void setError(int error) {
        this.error = error;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public void setItems(JsonVolume[] items) {
        this.items = items;
    }

    /**
     * Converts this data holder object into the model's BookShelf object.
     */
    public ReadOnlyBookShelf toModelType() {
        BookShelf bookShelf = new BookShelf();

        for (JsonVolume volume : items) {
            try {
                Book book = convertToBook(volume);
                bookShelf.addBook(book);
            } catch (InvalidBookException | DuplicateBookException e) {
                logger.warning(e.getMessage());
            }
        }
        return bookShelf;
    }

    /**
     * Converts a JsonVolume into a Book.
     */
    private static Book convertToBook(JsonVolume volume) throws InvalidBookException {
        JsonVolumeInfo volumeInfo = volume.volumeInfo;
        Isbn isbn = getIsbnFromIndustryIdentifiers(volumeInfo.industryIdentifiers);

        if (isbn == null) {
            throw new InvalidBookException("Found book without ISBN");
        }

        return new Book(new Gid(volume.id), isbn,
                BookDataUtil.getAuthorList(volumeInfo.authors), new Title(volumeInfo.title),
                BookDataUtil.getCategoryList(volumeInfo.categories), new Description(volumeInfo.description),
                new Publisher(volumeInfo.publisher), new PublicationDate(volumeInfo.publishedDate));
    }

    private static Isbn getIsbnFromIndustryIdentifiers(JsonIndustryIdentifiers[] iiArray) {
        return Stream.of(iiArray)
                .filter(ii -> ii.type.equals("ISBN_13"))
                .findFirst()
                .map(ii -> new Isbn(ii.identifier))
                .orElse(null);
    }


    /** Temporary data holder used for deserialization. */
    private static class JsonVolume {
        private String id = "";
        private JsonVolumeInfo volumeInfo = new JsonVolumeInfo();

```
###### \java\seedu\address\network\api\google\JsonSearchResults.java
``` java
    /** Temporary data holder used for deserialization. */
    private static class JsonVolumeInfo {
        private String title = "";
        private String[] authors = new String[0];
        private String publisher = "";
        private String publishedDate = "";
        private String description = "";
        private JsonIndustryIdentifiers[] industryIdentifiers = new JsonIndustryIdentifiers[0];
        private String[] categories = new String[0];

```
###### \java\seedu\address\network\HttpClient.java
``` java
/**
 * A wrapper around the {@link AsyncHttpClient} class from async-http-client.
 */
public class HttpClient {

    private static final Logger logger = LogsCenter.getLogger(HttpClient.class);
    private static final int CONNECTION_TIMEOUT_MILLIS = 1000 * 5; // 5 seconds
    private static final int READ_TIMEOUT_MILLIS = 1000 * 5; // 5 seconds
    private static final int REQUEST_TIMEOUT_MILLIS = 1000 * 5; // 5 seconds

    private final AsyncHttpClient asyncHttpClient;

    protected HttpClient() {
        this.asyncHttpClient = Dsl.asyncHttpClient(Dsl.config()
                .setConnectTimeout(CONNECTION_TIMEOUT_MILLIS)
                .setReadTimeout(READ_TIMEOUT_MILLIS)
                .setRequestTimeout(REQUEST_TIMEOUT_MILLIS));
    }

    protected HttpClient(AsyncHttpClient asyncHttpClient) {
        requireNonNull(asyncHttpClient);
        this.asyncHttpClient = asyncHttpClient;
    }

    /**
     * Asynchronously executes a HTTP GET request to the specified url.
     */
    public CompletableFuture<HttpResponse> makeGetRequest(String url) {
        return asyncHttpClient
                .prepareGet(url)
                .execute()
                .toCompletableFuture()
                .thenApply(HttpResponse::new);
    }

    /**
     * Stops and closes the underlying {@link AsyncHttpClient}.
     */
    public void close() {
        try {
            if (!asyncHttpClient.isClosed()) {
                asyncHttpClient.close();
            }
        } catch (IOException e) {
            logger.warning("Failed to shut down AsyncHttpClient.");
        }
    }
}
```
###### \java\seedu\address\network\HttpResponse.java
``` java
/**
 * A wrapper around the {@link Response} class from async-http-client.
 */
public class HttpResponse {

    private final int statusCode;
    private final String contentType;
    private final String responseBody;

    public HttpResponse(int statusCode, String contentType, String responseBody) {
        requireAllNonNull(contentType, responseBody);
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.responseBody = responseBody;
    }

    public HttpResponse(Response response) {
        this(response.getStatusCode(), response.getContentType(), response.getResponseBody());
    }

```
###### \java\seedu\address\network\Network.java
``` java
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
     * Stops the Network component.
     */
    void stop();
}
```
###### \java\seedu\address\network\NetworkManager.java
``` java
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
```
###### \resources\view\BookDetailsPanel.fxml
``` fxml
<StackPane fx:id="bookDetailsPane" xmlns="http://javafx.com/javafx/8.0.141" xmlns:fx="http://javafx.com/fxml/1">
  <padding>
    <Insets bottom="10" left="5" right="10" top="10"/>
  </padding>
  <ScrollPane fx:id="scrollPane" fitToWidth="true" hbarPolicy="NEVER" styleClass="anchor-pane">
    <VBox id="bookDetailsPane" styleClass="content-pane">
      <Label fx:id="title" styleClass="label-header" text="\$title" wrapText="true" VBox.vgrow="SOMETIMES"/>
      <BorderPane>
        <left>
          <ImageView fx:id="frontCover" fitHeight="210.0" fitWidth="128.0" pickOnBounds="true"
                     preserveRatio="true" BorderPane.alignment="CENTER">
            <BorderPane.margin>
              <Insets left="4.0" right="10.0"/>
            </BorderPane.margin>
          </ImageView>
        </left>
        <center>
          <VBox BorderPane.alignment="CENTER">
            <FlowPane styleClass="pills">
              <Label fx:id="status"/>
              <Label fx:id="priority"/>
              <Label fx:id="rating"/>
              <VBox.margin>
                <Insets top="3.0"/>
              </VBox.margin>
            </FlowPane>
            <GridPane BorderPane.alignment="BOTTOM_CENTER">
              <columnConstraints>
                <ColumnConstraints hgrow="SOMETIMES" maxWidth="160.0" minWidth="130.0" prefWidth="130.0"/>
                <ColumnConstraints hgrow="SOMETIMES" maxWidth="555.0" minWidth="10.0" prefWidth="280.0"/>
              </columnConstraints>
              <rowConstraints>
                <RowConstraints minHeight="30.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                <RowConstraints minHeight="30.0" vgrow="SOMETIMES"/>
                <RowConstraints minHeight="30.0" vgrow="SOMETIMES"/>
                <RowConstraints minHeight="30.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                <RowConstraints minHeight="30.0" prefHeight="30.0" vgrow="SOMETIMES"/>
              </rowConstraints>
              <children>
                <Label styleClass="label-muted" text="ISBN"/>
                <Label styleClass="label-muted" text="Authors" GridPane.rowIndex="1"/>
                <Label styleClass="label-muted" text="Categories" GridPane.rowIndex="2"/>
                <Label styleClass="label-muted" text="Publisher" GridPane.rowIndex="3"/>
                <Label styleClass="label-muted" text="Publication Date" GridPane.rowIndex="4"/>
                <Label fx:id="isbn" text="\$isbn" GridPane.columnIndex="1"/>
                <FlowPane fx:id="authors" alignment="CENTER_LEFT" hgap="7.0" nodeOrientation="LEFT_TO_RIGHT"
                          styleClass="pills-authors" vgap="5.0" GridPane.columnIndex="1" GridPane.rowIndex="1"
                          GridPane.vgrow="SOMETIMES">
                </FlowPane>
                <FlowPane fx:id="categories" alignment="CENTER_LEFT" nodeOrientation="LEFT_TO_RIGHT"
                          styleClass="pills-categories" GridPane.columnIndex="1" GridPane.rowIndex="2"
                          GridPane.vgrow="SOMETIMES">
                </FlowPane>
                <Label fx:id="publisher" text="\$publisher" GridPane.columnIndex="1" GridPane.rowIndex="3"/>
                <Label fx:id="publicationDate" text="\$publicationDate" GridPane.columnIndex="1" GridPane.rowIndex="4"/>
              </children>
              <VBox.margin>
                <Insets top="8.0"/>
              </VBox.margin>
            </GridPane>
          </VBox>
        </center>
        <VBox.margin>
          <Insets top="8.0" bottom="5.0"/>
        </VBox.margin>
      </BorderPane>
      <StackPane fx:id="descriptionPlaceholder" VBox.vgrow="NEVER"/>
    </VBox>
  </ScrollPane>
</StackPane>
```
