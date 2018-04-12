package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import seedu.address.model.alias.Alias;

//@@author takuyakanbr
/**
 * Deletes the alias identified by the alias name from the alias list.
 */
public class DeleteAliasCommand extends Command {

    public static final String COMMAND_WORD = "deletealias";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes the alias identified by the alias name.\n"
            + "Parameters: ALIAS_NAME\n"
            + "Example: " + COMMAND_WORD + "urd";

    public static final String MESSAGE_SUCCESS = "Deleted alias: %1$s.";
    public static final String MESSAGE_NOT_FOUND = "Alias does not exist: %1$s.";

    private final String toDelete;

    /**
     * Creates an {@code DeleteAliasCommand}.
     *
     * @param toDelete the name of the alias to be deleted.
     */
    public DeleteAliasCommand(String toDelete) {
        requireNonNull(toDelete);
        this.toDelete = toDelete.trim().toLowerCase();
    }

    @Override
    public CommandResult execute() {
        Optional<Alias> alias = model.getAlias(toDelete);
        if (!alias.isPresent()) {
            return new CommandResult(String.format(MESSAGE_NOT_FOUND, toDelete));
        }

        model.removeAlias(toDelete);
        return new CommandResult(String.format(MESSAGE_SUCCESS, alias.get()));
    }

    //@@author
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteAliasCommand // instanceof handles nulls
                && this.toDelete.equals(((DeleteAliasCommand) other).toDelete)); // state check
    }
}
