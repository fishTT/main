package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Theme;
import seedu.address.commons.events.ui.ChangeThemeRequestEvent;

//@@author takuyakanbr
/**
 * Changes the application theme.
 */
public class ThemeCommand extends Command {

    public static final String COMMAND_WORD = "theme";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Changes the theme used by the application.\n"
            + "Parameters: THEME_NAME\n"
            + "Example: " + COMMAND_WORD + " dark";

    public static final String MESSAGE_SUCCESS = "Application theme changed to: %1$s";
    public static final String MESSAGE_INVALID_THEME = "Invalid application theme: %1$s";

    private final Theme newTheme;

    public ThemeCommand(Theme newTheme) {
        requireNonNull(newTheme);
        this.newTheme = newTheme;
    }

    @Override
    public CommandResult execute() {
        EventsCenter.getInstance().post(new ChangeThemeRequestEvent(newTheme));
        return new CommandResult(String.format(MESSAGE_SUCCESS, newTheme.getThemeName()));
    }

    //@@author
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ThemeCommand // instanceof handles nulls
                && this.newTheme.equals(((ThemeCommand) other).newTheme)); // state check
    }
}
