package seedu.address.logic;

import java.util.Stack;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.UndoableCommand;

/**
 * Maintains the undo-stack (the stack of commands that can be undone).
 */
public class UndoStack {
    private Stack<UndoableCommand> undoStack;

    public UndoStack() {
        undoStack = new Stack<>();
    }

    /**
     * Pushes {@code command} onto the undo-stack if it is of type {@code UndoableCommand}.
     */
    public void push(Command command) {
        if (!(command instanceof UndoableCommand)) {
            return;
        }

        undoStack.add((UndoableCommand) command);
    }

    /**
     * Pops and returns the next {@code UndoableCommand} to be undone in the stack.
     */
    public UndoableCommand popUndo() {
        return undoStack.pop();
    }

    /**
     * Returns true if there are more commands that can be undone.
     */
    public boolean canUndo() {
        return !undoStack.empty();
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UndoStack)) {
            return false;
        }

        UndoStack stack = (UndoStack) other;

        // state check
        return undoStack.equals(stack.undoStack);
    }
}
