package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.commons.events.ui.ShowLibraryResultRequestEvent;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.book.Book;

//@@author qiu-siqi
/**
 * Searches for a book in the library.
 */
public class LibraryCommand extends Command {

    public static final String COMMAND_WORD = "library";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Views the availability of the book identified by the index number in the library.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Showing availability of book: %1$s.";
    public static final String MESSAGE_FAIL = "Failed to search for book in library. "
            + "Make sure you are connected to the Internet.";
    public static final String MESSAGE_SEARCHING = "Searching for the book in the library...";
    public static final String MESSAGE_WRONG_ACTIVE_LIST = "Cannot view availability of books "
            + "in the current list.";

    private final Index targetIndex;

    public LibraryCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {
        requireNonNull(model);

        checkValidIndex();

        makeAsyncBookInLibraryRequest(getBook(targetIndex));
        return new CommandResult(MESSAGE_SEARCHING);
    }

    /**
     * Throws a {@link CommandException} if the given index is not valid.
     */
    private void checkValidIndex() throws CommandException {
        if (targetIndex.getZeroBased() >= model.getActiveList().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_BOOK_DISPLAYED_INDEX);
        }
    }

    /**
     * Assumes: {@code targetIndex} is a valid index.
     * Returns the book to search for.
     */
    private Book getBook(Index targetIndex) {
        return model.getActiveList().get(targetIndex.getZeroBased());
    }

    /**
     * Makes an asynchronous request to search for {@code book} in library.
     */
    private void makeAsyncBookInLibraryRequest(Book book) {
        network.searchLibraryForBook(book)
                .thenAccept(result -> onSuccessfulRequest(result, book))
                .exceptionally(e -> {
                    EventsCenter.getInstance().post(new NewResultAvailableEvent(MESSAGE_FAIL));
                    return null;
                });
    }

    /**
     * Handles the result of a successful search for the book in library.
     */
    private void onSuccessfulRequest(String result, Book book) {
        EventsCenter.getInstance().post(new ShowLibraryResultRequestEvent(result));
        EventsCenter.getInstance().post(new NewResultAvailableEvent(
                String.format(MESSAGE_SUCCESS, book)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof LibraryCommand // instanceof handles nulls
                && this.targetIndex.equals(((LibraryCommand) other).targetIndex));
    }
}
