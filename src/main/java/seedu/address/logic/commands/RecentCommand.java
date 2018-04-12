package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ActiveListChangedEvent;
import seedu.address.model.ActiveListType;

//@@author qiu-siqi
/**
 * Lists all recently selected books to the user.
 */
public class RecentCommand extends Command {

    public static final String COMMAND_WORD = "recent";
    public static final String MESSAGE_SUCCESS = "Listed %s recently selected books.";

    @Override
    public CommandResult execute() {
        requireNonNull(model);

        model.setActiveListType(ActiveListType.RECENT_BOOKS);
        EventsCenter.getInstance().post(new ActiveListChangedEvent());
        return new CommandResult(String.format(MESSAGE_SUCCESS, model.getRecentBooksList().size()));
    }
}
