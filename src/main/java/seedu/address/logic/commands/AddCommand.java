package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import javafx.application.Platform;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.DisableCommandBoxRequestEvent;
import seedu.address.commons.events.ui.EnableCommandBoxRequestEvent;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.book.Book;
import seedu.address.model.book.exceptions.DuplicateBookException;

//@@author qiu-siqi
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
