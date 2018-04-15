package seedu.address.commons.util;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.book.Book;

/**
 * Utility methods used by Commands.
 */
public class CommandUtil {

    /**
     * Throws a {@link CommandException} if there isn't a book displayed at {@code targetIndex}.
     */
    public static void checkValidIndex(Model model, Index targetIndex) throws CommandException {
        requireAllNonNull(model, targetIndex);

        if (targetIndex.getZeroBased() >= model.getActiveList().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_BOOK_DISPLAYED_INDEX);
        }
    }

    /**
     * Precondition: {@code targetIndex} is a valid index. @see CommandUtil#checkValidIndex(Model, Index). <br>
     * Returns the book at the given index in the shown list.
     */
    public static Book getBook(Model model, Index targetIndex) {
        assert targetIndex.getZeroBased() < model.getActiveList().size();

        return model.getActiveList().get(targetIndex.getZeroBased());
    }
}
