package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.JumpToBookListIndexRequestEvent;
import seedu.address.commons.events.ui.JumpToRecentBooksIndexRequestEvent;
import seedu.address.commons.events.ui.JumpToSearchResultsIndexRequestEvent;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Selects a book identified using it's last displayed index from the book shelf.
 */
public class SelectCommand extends Command {

    public static final String COMMAND_WORD = "select";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the book identified by the index number in the current book listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_BOOK_SUCCESS = "Selected Book: %1$s";
    public static final String MESSAGE_WRONG_ACTIVE_LIST = "Items from the current list cannot be selected.";

    private final Index targetIndex;

    public SelectCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {
        requireNonNull(model);

        checkValidIndex();

        switch (model.getActiveListType()) {
        case BOOK_SHELF:
        {
            model.addRecentBook(model.getDisplayBookList().get(targetIndex.getZeroBased()));
            EventsCenter.getInstance().post(new JumpToBookListIndexRequestEvent(targetIndex));
            break;
        }
        case SEARCH_RESULTS:
        {
            model.addRecentBook(model.getSearchResultsList().get(targetIndex.getZeroBased()));
            EventsCenter.getInstance().post(new JumpToSearchResultsIndexRequestEvent(targetIndex));
            break;
        }
        case RECENT_BOOKS:
        {
            EventsCenter.getInstance().post(new JumpToRecentBooksIndexRequestEvent(targetIndex));
            break;
        }
        default:
            throw new CommandException(MESSAGE_WRONG_ACTIVE_LIST);
        }

        return new CommandResult(String.format(MESSAGE_SELECT_BOOK_SUCCESS, targetIndex.getOneBased()));

    }

    /**
     * Throws a {@link CommandException} if the given index is not valid.
     */
    private void checkValidIndex() throws CommandException {
        if (targetIndex.getZeroBased() >= model.getActiveList().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_BOOK_DISPLAYED_INDEX);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SelectCommand // instanceof handles nulls
                && this.targetIndex.equals(((SelectCommand) other).targetIndex)); // state check
    }
}
