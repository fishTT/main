package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static seedu.address.testutil.TypicalAliases.getTypicalAliasList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import seedu.address.commons.events.ui.ShowAliasListRequestEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoStack;
import seedu.address.model.BookShelf;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.network.NetworkManager;
import seedu.address.ui.testutil.EventsCollectorRule;

//@@author takuyakanbr
public class AliasesCommandTest {

    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(new BookShelf(), new UserPrefs(), new BookShelf(), getTypicalAliasList());
    }

    @Test
    public void execute_aliases_success() {
        CommandResult result = prepareCommand().execute();

        assertEquals(String.format(AliasesCommand.MESSAGE_SUCCESS, model.getAliasList().size()), result.feedbackToUser);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof ShowAliasListRequestEvent);
        assertEquals(1, eventsCollectorRule.eventsCollector.getSize());
    }

    private AliasesCommand prepareCommand() {
        AliasesCommand command = new AliasesCommand();
        command.setData(model, mock(NetworkManager.class), new CommandHistory(), new UndoStack());
        return command;
    }
}
