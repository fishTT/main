# qiu-siqi
###### \java\seedu\address\logic\commands\AddCommand.java
``` java
/**
 * Adds a book to the book shelf.
 */
public class AddCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds the book identified by the index number used in the current search result.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_ADDING = "Adding the book into your book shelf...";
    public static final String MESSAGE_ADD_FAIL = "Failed to add book into your book shelf. "
            + "Make sure you are connected to the Internet.";
    public static final String MESSAGE_SUCCESS = "New book added: %1$s";
    public static final String MESSAGE_DUPLICATE_BOOK = "This book already exists in the book shelf";
    public static final String MESSAGE_WRONG_ACTIVE_LIST = "Items from the current list cannot be added.";

    private final Index targetIndex;

    private Book toAdd;
    private final boolean useJavafxThread;

    public AddCommand(Index targetIndex) {
        this(targetIndex, true);
    }

    /**
     * Creates a {@code AddCommand} that can choose not use the JavaFX thread to update the model and UI.
     * This constructor is provided for unit-testing purposes.
     */
    protected AddCommand(Index targetIndex, boolean useJavafxThread) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
        this.useJavafxThread = useJavafxThread;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(toAdd);

        EventsCenter.getInstance().post(new DisableCommandBoxRequestEvent());
        makeAsyncBookDetailsRequest();
        return new CommandResult(MESSAGE_ADDING);
    }

    /**
     * Makes an asynchronous request to retrieve book details.
     */
    private void makeAsyncBookDetailsRequest() {
        network.getBookDetails(toAdd.getGid().gid)
                .thenAccept(this::onSuccessfulRequest)
                .exceptionally(e -> {
                    EventsCenter.getInstance().post(new NewResultAvailableEvent(AddCommand.MESSAGE_ADD_FAIL));
                    EventsCenter.getInstance().post(new EnableCommandBoxRequestEvent());
                    return null;
                });
    }

    /**
     * Handles the result of a successful request for book details.
     */
    private void onSuccessfulRequest(Book book) {
        if (useJavafxThread) {
            Platform.runLater(() -> addBook(book));
        } else {
            addBook(book);
        }
    }

    /**
     * Adds the given book to the book shelf and posts events to update the UI.
     */
    protected void addBook(Book book) {
        try {
            model.addBook(book);
            EventsCenter.getInstance().post(new NewResultAvailableEvent(
                    String.format(AddCommand.MESSAGE_SUCCESS, book)));
        } catch (DuplicateBookException e) {
            EventsCenter.getInstance().post(new NewResultAvailableEvent(AddCommand.MESSAGE_DUPLICATE_BOOK));
        }
        EventsCenter.getInstance().post(new EnableCommandBoxRequestEvent());
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        requireNonNull(model);

        switch (model.getActiveListType()) {
        case SEARCH_RESULTS:
        {
            List<Book> searchResultsList = model.getSearchResultsList();

            if (targetIndex.getZeroBased() >= searchResultsList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_BOOK_DISPLAYED_INDEX);
            }

            toAdd = searchResultsList.get(targetIndex.getZeroBased());
            break;
        }
        case RECENT_BOOKS:
        {
            List<Book> recentBooksList = model.getRecentBooksList();

            if (targetIndex.getZeroBased() >= recentBooksList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_BOOK_DISPLAYED_INDEX);
            }

            toAdd = recentBooksList.get(targetIndex.getZeroBased());
            break;
        }
        default:
            throw new CommandException(MESSAGE_WRONG_ACTIVE_LIST);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                && this.targetIndex.equals(((AddCommand) other).targetIndex)
                && Objects.equals(this.toAdd, ((AddCommand) other).toAdd));
    }
}
```
###### \java\seedu\address\logic\commands\RecentCommand.java
``` java
/**
 * Lists all recently selected books to the user.
 */
public class RecentCommand extends Command {

    public static final String COMMAND_WORD = "recent";
    public static final String MESSAGE_SUCCESS = "Listed all recently selected books.";

    @Override
    public CommandResult execute() {
        requireNonNull(model);
        EventsCenter.getInstance().post(new SwitchToRecentBooksRequestEvent());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
```
###### \java\seedu\address\logic\commands\ReviewsCommand.java
``` java
/**
 * Shows reviews for a specified book.
 */
public class ReviewsCommand extends Command {

    public static final String COMMAND_WORD = "reviews";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Loads reviews of the book identified by the index number used in the current list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Showing reviews for book: %1$s.";
    public static final String MESSAGE_WRONG_ACTIVE_LIST = "Cannot load reviews for items "
            + "from the current list.";

    private final Index targetIndex;

    public ReviewsCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {
        requireNonNull(model);
        Book toLoad;
        switch (model.getActiveListType()) {
        case BOOK_SHELF:
        {
            List<Book> displayBookList = model.getDisplayBookList();

            if (targetIndex.getZeroBased() >= displayBookList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_BOOK_DISPLAYED_INDEX);
            }

            toLoad = displayBookList.get(targetIndex.getZeroBased());
            break;
        }
        case SEARCH_RESULTS:
        {
            List<Book> searchResultsList = model.getSearchResultsList();

            if (targetIndex.getZeroBased() >= searchResultsList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_BOOK_DISPLAYED_INDEX);
            }

            toLoad = searchResultsList.get(targetIndex.getZeroBased());
            break;
        }
        case RECENT_BOOKS:
        {
            List<Book> recentBooksList = model.getRecentBooksList();

            if (targetIndex.getZeroBased() >= recentBooksList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_BOOK_DISPLAYED_INDEX);
            }

            toLoad = recentBooksList.get(targetIndex.getZeroBased());
            break;
        }
        default:
            throw new CommandException(MESSAGE_WRONG_ACTIVE_LIST);
        }

        EventsCenter.getInstance().post(new ShowBookReviewsRequestEvent(toLoad));
        return new CommandResult(String.format(MESSAGE_SUCCESS, toLoad));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReviewsCommand // instanceof handles nulls
                && this.targetIndex.equals(((ReviewsCommand) other).targetIndex));
    }
}
```
###### \java\seedu\address\logic\parser\AddCommandParser.java
``` java
/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new AddCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }
    }
}
```
###### \java\seedu\address\logic\parser\ReviewsCommandParser.java
``` java
/**
 * Parses input arguments and creates a new ReviewsCommand object.
 */
public class ReviewsCommandParser implements Parser<ReviewsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ReviewsCommand
     * and returns an ReviewsCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public ReviewsCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new ReviewsCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReviewsCommand.MESSAGE_USAGE));
        }
    }
}
```
###### \java\seedu\address\model\book\UniqueBookCircularList.java
``` java
/**
 * A list of items that enforces no nulls and uniqueness between its elements,
 * with maximum of a pre-set number of elements.
 * When the limit is reached, the earliest added element is removed to add the new element.
 *
 * Supports a minimal set of operations.
 */
public class UniqueBookCircularList extends UniqueList<Book> {

    private final int size;

    public UniqueBookCircularList() {
        this.size = 50;
    }

    /**
     * Constructs a list where the maximum number of books in the list is {@code size}.
     */
    public UniqueBookCircularList(int size) {
        this.size = size;
    }

    /**
     * Adds a book to the front of the list.
     * Moves the book to the front of the list if it exists in the list.
     * Removes the earliest added book if the list is full before adding the new book.
     */
    public void addToFront(Book toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            internalList.remove(toAdd);
        }
        if (internalList.size() >= size) {
            internalList.remove(size - 1);
        }
        internalList.add(0, toAdd);

        assert internalList.size() <= size;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueBookCircularList // instanceof handles nulls
                && this.internalList.equals(((UniqueBookCircularList) other).internalList)
                && this.size == ((UniqueBookCircularList) other).size);
    }
}
```
###### \java\seedu\address\storage\XmlRecentBooksStorage.java
``` java
/**
 * A class to access recently selected books data stored as an xml file on the hard disk.
 */
public class XmlRecentBooksStorage implements RecentBooksStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlRecentBooksStorage.class);

    private String filePath;

    public XmlRecentBooksStorage(String filePath) {
        this.filePath = filePath;
    }

    public String getRecentBooksFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyBookShelf> readRecentBooksList() throws DataConversionException, IOException {
        return readRecentBooksList(filePath);
    }

    /**
     * Similar to {@link #readRecentBooksList()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyBookShelf> readRecentBooksList(String filePath) throws DataConversionException,
            FileNotFoundException {
        requireNonNull(filePath);

        File file = new File(filePath);

        if (!file.exists()) {
            logger.info("Recent books file "  + file + " not found");
            return Optional.empty();
        }

        XmlSerializableBookShelf xmlRecentBooksList = XmlFileStorage.loadDataFromSaveFile(new File(filePath));
        try {
            return Optional.of(xmlRecentBooksList.toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + file + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveRecentBooksList(ReadOnlyBookShelf recentBooksList) throws IOException {
        saveRecentBooksList(recentBooksList, filePath);
    }

    /**
     * Similar to {@link #saveRecentBooksList(ReadOnlyBookShelf)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveRecentBooksList(ReadOnlyBookShelf recentBooksList, String filePath) throws IOException {
        requireNonNull(recentBooksList);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableBookShelf(recentBooksList));
    }
}
```
###### \java\seedu\address\ui\BookDescriptionView.java
``` java
/**
 * The Region showing description of books.
 */
public class BookDescriptionView extends UiPart<Region> {

    private static final int HEIGHT_PAD = 20;

    private static final String FXML = "BookDescriptionView.fxml";

    @FXML
    private WebView description;

    private final WebEngine webEngine;

    public BookDescriptionView() {
        super(FXML);
        webEngine = description.getEngine();

        // disable interaction with web view
        description.setDisable(true);

        description.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                adjustHeight();
            }
        });
    }

    /**
     * Loads the description of {@code book}.
     */
    protected void loadContent(Book book) {
        description.getEngine().loadContent(getHtml(book.getDescription().toString()));
        // set transparent background for web view
        Accessor.getPageFor(webEngine).setBackgroundColor(0);
    }

    // Reused from http://tech.chitgoks.com/2014/09/13/how-to-fit-webview-height-based-on-its-content-in-java-fx-2-2/
    /**
     * Fit height of {@code WebView} according to height of content.
     */
    private void adjustHeight() {
        Platform.runLater(() -> {
            Object result = webEngine.executeScript("document.getElementById('description').offsetHeight");
            if (result instanceof Integer) {
                Integer height = (Integer) result;
                description.setPrefHeight(height + HEIGHT_PAD);
            }
        });
    }

    // Reused from http://tech.chitgoks.com/2014/09/13/how-to-fit-webview-height-based-on-its-content-in-java-fx-2-2/
    private String getHtml(String content) {
        return "<html><body><div id=\"description\">" + content + "</div></body></html>";
    }

    protected void setStyleSheet(String styleSheet) {
        description.getEngine().setUserStyleSheetLocation(getClass().getClassLoader()
                .getResource(styleSheet).toExternalForm());
    }

}
```
