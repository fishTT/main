package seedu.address.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.logic.UndoStackUtil.prepareStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;

import org.junit.Test;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.UndoableCommand;

public class UndoStackTest {
    private final DummyCommand dummyCommandOne = new DummyCommand();
    private final DummyUndoableCommand dummyUndoableCommandOne = new DummyUndoableCommand();
    private final DummyUndoableCommand dummyUndoableCommandTwo = new DummyUndoableCommand();

    private UndoStack undoStack = new UndoStack();

    @Test
    public void push_nonUndoableCommand_commandNotAdded() {
        undoStack = prepareStack(Collections.singletonList(dummyUndoableCommandOne));
        undoStack.push(dummyCommandOne);
        assertStackStatus(Collections.singletonList(dummyUndoableCommandOne));

        undoStack.push(dummyCommandOne);
        assertStackStatus(Collections.singletonList(dummyUndoableCommandOne));
    }

    @Test
    public void push_undoableCommand_commandAdded() {
        undoStack = prepareStack(Collections.singletonList(dummyUndoableCommandOne));
        undoStack.push(dummyUndoableCommandOne);
        assertStackStatus(Arrays.asList(dummyUndoableCommandOne, dummyUndoableCommandOne));

        undoStack.push(dummyUndoableCommandOne);
        assertStackStatus(Arrays.asList(dummyUndoableCommandOne, dummyUndoableCommandOne, dummyUndoableCommandOne));
    }

    @Test
    public void push_undoCommand_stackRemainsUnchanged() {
        undoStack = prepareStack(Collections.singletonList(dummyUndoableCommandOne));
        undoStack.push(new UndoCommand());
        assertStackStatus(Collections.singletonList(dummyUndoableCommandOne));

        undoStack = prepareStack(Collections.singletonList(dummyUndoableCommandOne));
        undoStack.push(new UndoCommand());
        assertStackStatus(Collections.singletonList(dummyUndoableCommandOne));
    }

    @Test
    public void canUndo() {
        // empty undo stack
        assertFalse(undoStack.canUndo());

        // non-empty undo stack
        undoStack = prepareStack(Collections.singletonList(dummyUndoableCommandOne));
        assertTrue(undoStack.canUndo());
    }

    @Test
    public void popUndo() {
        undoStack = prepareStack(Arrays.asList(dummyUndoableCommandOne, dummyUndoableCommandTwo));

        // multiple commands in undoStack
        assertPopUndoSuccess(dummyUndoableCommandTwo, Collections.singletonList(dummyUndoableCommandOne));

        // single command in undoStack
        assertPopUndoSuccess(dummyUndoableCommandOne, Collections.emptyList());

        // no command in undoStack
        assertPopUndoFailure(Collections.emptyList());
    }

    @Test
    public void equals() {
        undoStack = prepareStack(Arrays.asList(dummyUndoableCommandTwo, dummyUndoableCommandOne));

        // same values -> returns true
        UndoStack copy = prepareStack(Arrays.asList(dummyUndoableCommandTwo, dummyUndoableCommandOne));
        assertTrue(undoStack.equals(copy));

        // same object -> returns true
        assertTrue(undoStack.equals(undoStack));

        // null -> returns false
        assertFalse(undoStack.equals(null));

        // different types -> returns false
        assertFalse(undoStack.equals(1));

        // different undoStack -> returns false
        UndoStack differentUndoStack = prepareStack(Collections.singletonList(dummyUndoableCommandTwo));
        assertFalse(undoStack.equals(differentUndoStack));
    }

    /**
     * Asserts that the result of {@code undoStack#popUndo()} equals {@code expectedCommand}.
     * Also asserts that the content of the {@code undoStack#undoStack} equals {@code undoElements}.
     */
    private void assertPopUndoSuccess(UndoableCommand expectedCommand,
                                      List<UndoableCommand> expectedUndoElements) {
        assertEquals(expectedCommand, undoStack.popUndo());
        assertStackStatus(expectedUndoElements);
    }

    /**
     * Asserts that the execution of {@code undoStack#popUndo()} fails and {@code EmptyStackException} is thrown.
     * Also asserts that the content of the {@code undoStack#undoStack} equals {@code undoElements}.
     */
    private void assertPopUndoFailure(List<UndoableCommand> expectedUndoElements) {
        try {
            undoStack.popUndo();
            fail("The expected EmptyStackException was not thrown.");
        } catch (EmptyStackException ese) {
            assertStackStatus(expectedUndoElements);
        }
    }

    /**
     * Asserts that {@code undoStack#undoStack} equals {@code undoElements}.
     */
    private void assertStackStatus(List<UndoableCommand> undoElements) {
        assertEquals(prepareStack(undoElements), undoStack);
    }

    class DummyCommand extends Command {
        @Override
        public CommandResult execute() {
            return new CommandResult("");
        }
    }

    class DummyUndoableCommand extends UndoableCommand {
        @Override
        protected String undo() {
            return "";
        }

        @Override
        public CommandResult executeUndoableCommand() {
            return new CommandResult("");
        }
    }
}
