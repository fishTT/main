package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.ReviewsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author qiu-siqi
/**
 * Parses input arguments and creates a new ReviewsCommand object.
 */
public class ReviewsCommandParser implements Parser<ReviewsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ReviewsCommand
     * and returns an ReviewsCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public ReviewsCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new ReviewsCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReviewsCommand.MESSAGE_USAGE));
        }
    }
}
