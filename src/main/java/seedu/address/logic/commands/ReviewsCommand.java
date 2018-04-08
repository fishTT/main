package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.ShowBookReviewsRequestEvent;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.book.Book;

//@@author qiu-siqi
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
