package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ActiveListChangedEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ActiveListType;
import seedu.address.model.Model;
import seedu.address.network.Network;

/**
 * Undo the previous {@code UndoableCommand}.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Undo success!";
    public static final String MESSAGE_FAILURE = "No more commands to undo!";

    @Override
    public CommandResult execute() throws CommandException {
        requireAllNonNull(model, undoStack);

        if (!undoStack.canUndo()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        String message = undoStack.popUndo().undo();
        model.updateBookListFilter(Model.PREDICATE_SHOW_ALL_BOOKS);
        model.setActiveListType(ActiveListType.BOOK_SHELF);
        EventsCenter.getInstance().post(new ActiveListChangedEvent());
        return new CommandResult(message);
    }

    @Override
    public void setData(Model model, Network network, CommandHistory commandHistory, UndoStack undoStack) {
        this.model = model;
        this.undoStack = undoStack;
    }
}
