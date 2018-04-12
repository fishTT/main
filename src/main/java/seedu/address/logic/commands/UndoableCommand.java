package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Represents a command which can be undone.
 */
public abstract class UndoableCommand extends Command {

    protected abstract CommandResult executeUndoableCommand() throws CommandException;

    /**
     * Store what is required of the current state of {@code model#bookShelf}.
     * {@code UndoableCommand}s that needs to save some data should override this method.
     */
    protected void saveSnapshot() {}

    /**
     * This method is called before the execution of {@code UndoableCommand}.
     * {@code UndoableCommand}s that require this preprocessing step should override this method.
     */
    protected void preprocessUndoableCommand() throws CommandException {}

    /**
     * Revert the state of {@code model#bookShelf} back to before this command was executed.
     * @return success or failure message.
     */
    protected abstract String undo();

    @Override
    public final CommandResult execute() throws CommandException {
        saveSnapshot();
        preprocessUndoableCommand();
        return executeUndoableCommand();
    }
}
