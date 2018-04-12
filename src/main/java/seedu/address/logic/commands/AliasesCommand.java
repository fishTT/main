package seedu.address.logic.commands;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ShowAliasListRequestEvent;

//@@author takuyakanbr
/**
 * Shows a list of all command aliases.
 */
public class AliasesCommand extends Command {

    public static final String COMMAND_WORD = "aliases";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows a listing of your command aliases.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Listed %s aliases.";

    @Override
    public CommandResult execute() {
        EventsCenter.getInstance().post(new ShowAliasListRequestEvent());
        return new CommandResult(String.format(MESSAGE_SUCCESS, model.getAliasList().size()));
    }
}
