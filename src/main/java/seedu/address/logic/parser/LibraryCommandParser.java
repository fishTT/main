package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.LibraryCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new LibraryCommand object.
 */
public class LibraryCommandParser implements Parser<LibraryCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the LibraryCommand
     * and returns an LibraryCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public LibraryCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new LibraryCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, LibraryCommand.MESSAGE_USAGE));
        }
    }
}
