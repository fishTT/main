package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalAliases.getTypicalAliasList;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoStack;
import seedu.address.model.BookShelf;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.alias.Alias;
import seedu.address.network.NetworkManager;
import seedu.address.testutil.TypicalAliases;

//@@author takuyakanbr
/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code DeleteAliasCommand}.
 */
public class DeleteAliasCommandTest {

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(new BookShelf(), new UserPrefs(), new BookShelf(), getTypicalAliasList());
    }

    @Test
    public void execute_validName_success() {
        Alias aliasToDelete = TypicalAliases.UNREAD;
        DeleteAliasCommand command = prepareCommand(aliasToDelete.getName());

        String expectedMessage = String.format(DeleteAliasCommand.MESSAGE_SUCCESS, aliasToDelete);
        ModelManager expectedModel = new ModelManager(model.getBookShelf(),
                new UserPrefs(), new BookShelf(), model.getAliasList());
        expectedModel.removeAlias(aliasToDelete.getName());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_mixedCaseAndSpacePaddedName_success() {
        Alias aliasToDelete = TypicalAliases.UNREAD;
        DeleteAliasCommand command = prepareCommand("   URd     ");

        String expectedMessage = String.format(DeleteAliasCommand.MESSAGE_SUCCESS, aliasToDelete);
        ModelManager expectedModel = new ModelManager(model.getBookShelf(),
                new UserPrefs(), new BookShelf(), model.getAliasList());
        expectedModel.removeAlias(aliasToDelete.getName());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidName_doesNotModifyList() {
        String toDelete = "non_existing_alias";
        DeleteAliasCommand command = prepareCommand(toDelete);

        String expectedMessage = String.format(DeleteAliasCommand.MESSAGE_NOT_FOUND, toDelete);
        ModelManager expectedModel = new ModelManager(model.getBookShelf(),
                new UserPrefs(), new BookShelf(), model.getAliasList());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    //@@author
    @Test
    public void equals() {
        DeleteAliasCommand command = new DeleteAliasCommand("urd");

        // same object -> returns true
        assertTrue(command.equals(command));

        // same value -> returns true
        DeleteAliasCommand commandCopy = new DeleteAliasCommand("urd");
        assertTrue(command.equals(commandCopy));

        // different types -> returns false
        assertFalse(command.equals("123"));

        // null -> returns false
        assertFalse(command.equals(null));

        // different value -> returns false
        DeleteAliasCommand differentCommand = new DeleteAliasCommand("s");
        assertFalse(command.equals(differentCommand));
    }

    private DeleteAliasCommand prepareCommand(String toDelete) {
        DeleteAliasCommand command = new DeleteAliasCommand(toDelete);
        command.setData(model, mock(NetworkManager.class), new CommandHistory(), new UndoStack());
        return command;
    }
}
