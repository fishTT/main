package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMAND;

import seedu.address.model.alias.Alias;

//@@author takuyakanbr
/**
 * Adds a command alias to the alias list.
 */
public class AddAliasCommand extends Command {

    public static final String COMMAND_WORD = "addalias";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a new alias for a command.\n"
            + "Parameters: ALIAS_NAME "
            + PREFIX_COMMAND + "COMMAND\n"
            + "Example: " + COMMAND_WORD + "urd "
            + PREFIX_COMMAND + "list s/unread";

    public static final String MESSAGE_NEW = "Added a new alias: %1$s.";
    public static final String MESSAGE_UPDATE = "Updated an existing alias: %1$s.";

    private final Alias toAdd;

    /**
     * Creates an {@code AddAliasCommand}.
     *
     * @param toAdd the alias to be added.
     */
    public AddAliasCommand(Alias toAdd) {
        requireNonNull(toAdd);
        this.toAdd = toAdd;
    }

    @Override
    public CommandResult execute() {
        boolean isUpdating = model.getAlias(toAdd.getName()).isPresent();
        model.addAlias(toAdd);

        String message = isUpdating ? MESSAGE_UPDATE : MESSAGE_NEW;
        return new CommandResult(String.format(message, toAdd));
    }

    //@@author
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddAliasCommand // instanceof handles nulls
                && this.toAdd.equals(((AddAliasCommand) other).toAdd)); // state check
    }
}
