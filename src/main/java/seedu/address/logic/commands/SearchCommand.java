package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AUTHOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ISBN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;

import java.util.Optional;

import javafx.application.Platform;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.DisableCommandBoxRequestEvent;
import seedu.address.commons.events.ui.EnableCommandBoxRequestEvent;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.commons.events.ui.SwitchToSearchResultsRequestEvent;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.ReadOnlyBookShelf;

//@@author takuyakanbr
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
