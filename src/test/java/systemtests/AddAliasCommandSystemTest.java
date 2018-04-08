package systemtests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMAND;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_BOOK;

import org.junit.Test;

import seedu.address.logic.commands.AddAliasCommand;
import seedu.address.logic.commands.AliasesCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.model.Model;
import seedu.address.model.alias.Alias;
import seedu.address.model.book.Status;

//@@author takuyakanbr
public class AddAliasCommandSystemTest extends BibliotekSystemTest {

    @Test
    public void addAlias() {
        /* --------------------------------- Performing valid add operation ----------------------------------------- */

        /* case: add a new alias without named args -> added */
        Alias alias = new Alias("s", "select", "");
        assertCommandSuccess(AddAliasCommand.COMMAND_WORD + " s " + PREFIX_COMMAND + "select",
                alias, String.format(AddAliasCommand.MESSAGE_NEW, alias));

        executeCommand(AliasesCommand.COMMAND_WORD);
        assertTrue(getAliasListPanel().isVisible());

        /* case: add a new alias with named args -> added */
        alias = new Alias("read", "edit", "s/read");
        assertCommandSuccess(AddAliasCommand.COMMAND_WORD + " read " + PREFIX_COMMAND + "edit s/read",
                alias, String.format(AddAliasCommand.MESSAGE_NEW, alias));

        /* case: replace an existing alias -> replaced */
        alias = new Alias("read", "list", "s/read by/title");
        assertCommandSuccess(AddAliasCommand.COMMAND_WORD + " read " + PREFIX_COMMAND + "list s/read by/title",
                alias, String.format(AddAliasCommand.MESSAGE_UPDATE, alias));

        /* --------------------------------- Performing invalid add operation --------------------------------------- */

        /* case: no args -> rejected */
        assertCommandFailure(AddAliasCommand.COMMAND_WORD + "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAliasCommand.MESSAGE_USAGE));

        /* case: empty alias name -> rejected */
        assertCommandFailure(AddAliasCommand.COMMAND_WORD + "  " + PREFIX_COMMAND + "edit s/read",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAliasCommand.MESSAGE_USAGE));

        /* case: invalid alias name -> rejected */
        assertCommandFailure(AddAliasCommand.COMMAND_WORD + " hello world " + PREFIX_COMMAND + "edit s/read",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAliasCommand.MESSAGE_USAGE));

        /* case: empty aliased command -> rejected */
        assertCommandFailure(AddAliasCommand.COMMAND_WORD + " read " + PREFIX_COMMAND + "  ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAliasCommand.MESSAGE_USAGE));

        /* --------------------------------- Performing commands using aliases -------------------------------------- */

        Model model = getModel();

        /* case: perform select command using alias */
        executeCommand("s " + INDEX_FIRST_BOOK.getOneBased());
        model.addRecentBook(model.getDisplayBookList().get(INDEX_FIRST_BOOK.getZeroBased()));
        assertTrue(getBookDetailsPanel().isVisible());
        assertFalse(getAliasListPanel().isVisible());
        assertApplicationDisplaysExpected("",
                String.format(SelectCommand.MESSAGE_SELECT_BOOK_SUCCESS, INDEX_FIRST_BOOK.getOneBased()), model);

        /* case: perform list command using alias */
        executeCommand("read");
        model.updateBookListFilter(book -> book.getStatus() == Status.READ);
        assertApplicationDisplaysExpected("", String.format(ListCommand.MESSAGE_SUCCESS, 1), model);

        /* case: perform list command using alias, with overridden named parameter */
        executeCommand("read s/unread");
        model.updateBookListFilter(book -> book.getStatus() == Status.UNREAD);
        assertApplicationDisplaysExpected("", String.format(ListCommand.MESSAGE_SUCCESS, 2), model);
    }

    /**
     * Executes the addalias {@code command} and verifies that,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the expected message.<br>
     * 4. {@code Model} and {@code Storage} equal to the corresponding components after adding.<br>
     * 5. If the alias list is visible, the aliases in the list matches the expected alias list.<br>
     * 6. Status bar remains unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code BibliotekSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see BibliotekSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Alias toAdd, String expectedMessage) {
        Model expectedModel = getModel();
        expectedModel.addAlias(toAdd);

        executeCommand(command);
        assertCommandBoxShowsDefaultStyle();

        assertApplicationDisplaysExpected("", expectedMessage, expectedModel);
        if (getAliasListPanel().isVisible()) {
            assertAliasListDisplaysExpected(expectedModel);
        }

        assertStatusBarUnchanged();
    }
}
