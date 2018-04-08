package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.SwitchToRecentBooksRequestEvent;

//@@author qiu-siqi
/**
 * Lists all recently selected books to the user.
 */
public class RecentCommand extends Command {

    public static final String COMMAND_WORD = "recent";
    public static final String MESSAGE_SUCCESS = "Listed all recently selected books.";

    @Override
    public CommandResult execute() {
        requireNonNull(model);
        EventsCenter.getInstance().post(new SwitchToRecentBooksRequestEvent());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
