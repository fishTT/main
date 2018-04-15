package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OLD;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.LockManager;
import seedu.address.logic.commands.SetPasswordCommand;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author 592363789
/**
 * Parses input arguments and creates a new SetPasswordCommand object.
 */
public class SetPasswordCommandParser implements Parser<SetPasswordCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SetPasswordCommand.
     * and returns an SetPasswordCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public SetPasswordCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_OLD, PREFIX_NEW);

        String oldKey = LockManager.NO_PASSWORD;
        if (argMultimap.getValue(PREFIX_OLD).isPresent()) {
            oldKey = String.valueOf(argMultimap.getValue(PREFIX_OLD).get().trim());
        }

        String newKey = LockManager.NO_PASSWORD;
        if (argMultimap.getValue(PREFIX_NEW).isPresent()) {
            newKey = String.valueOf(argMultimap.getValue(PREFIX_NEW).get().trim());
        }

        boolean isValid = CollectionUtil.isAnyNonNull(oldKey) || CollectionUtil.isAnyNonNull(newKey);

        if (args.trim().isEmpty() || !isValid) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetPasswordCommand.MESSAGE_USAGE));
        }

        return new SetPasswordCommand(oldKey, newKey);
    }
}
