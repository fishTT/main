package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private static final CommandResult emptyResult = new CommandResult("");

    public final String feedbackToUser;

    public CommandResult(String feedbackToUser) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
    }

    public static CommandResult emptyResult() {
        return emptyResult;
    }

    public static boolean isEmptyResult(CommandResult commandResult) {
        return commandResult.equals(emptyResult);
    }

}
