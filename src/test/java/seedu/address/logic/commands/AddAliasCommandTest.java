package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalAliases.UNREAD;
import static seedu.address.testutil.TypicalAliases.getTypicalAliasList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoStack;
import seedu.address.model.BookShelf;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.alias.Alias;
import seedu.address.network.NetworkManager;

//@@author takuyakanbr
/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code AddAliasCommand}.
 */
public class AddAliasCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(new BookShelf(), new UserPrefs(), new BookShelf(), getTypicalAliasList());
    }

    @Test
    public void constructor_nullAlias_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddAliasCommand(null);
    }

    @Test
    public void execute_nonExistingAlias_aliasAdded() {
        Alias alias = new Alias("test", "test", "e/test f/test");
        AddAliasCommand command = prepareCommand(alias);

        ModelManager expectedModel = new ModelManager(new BookShelf(),
                new UserPrefs(), new BookShelf(), getTypicalAliasList());
        expectedModel.addAlias(alias);

        assertCommandSuccess(command, model, String.format(AddAliasCommand.MESSAGE_NEW, alias), expectedModel);
    }

    @Test
    public void execute_existingAlias_aliasUpdated() {
        Alias alias = new Alias(UNREAD.getName(), UNREAD.getPrefix(), "e/test f/test");
        AddAliasCommand command = prepareCommand(alias);

        ModelManager expectedModel = new ModelManager(new BookShelf(),
                new UserPrefs(), new BookShelf(), getTypicalAliasList());
        expectedModel.addAlias(alias);

        assertCommandSuccess(command, model, String.format(AddAliasCommand.MESSAGE_UPDATE, alias), expectedModel);
    }

    //@@author
    @Test
    public void equals() {
        AddAliasCommand command = prepareCommand(new Alias("1", "1", "1"));

        // same object -> returns true
        assertTrue(command.equals(command));

        // same alias -> returns true
        AddAliasCommand commandCopy = prepareCommand(new Alias("1", "1", "1"));
        assertTrue(command.equals(commandCopy));

        // different types -> returns false
        assertFalse(command.equals("123"));

        // null -> returns false
        assertFalse(command.equals(null));

        // different alias -> returns false
        AddAliasCommand differentCommand = prepareCommand(new Alias("1", "1", "123"));
        assertFalse(command.equals(differentCommand));
    }

    /**
     * Returns an {@code AddAliasCommand} with the parameter {@code alias}.
     */
    private AddAliasCommand prepareCommand(Alias alias) {
        AddAliasCommand command = new AddAliasCommand(alias);
        command.setData(model, mock(NetworkManager.class), new CommandHistory(), new UndoStack());
        return command;
    }
}
