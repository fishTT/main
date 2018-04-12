package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_BOOK;

import org.junit.Test;

import seedu.address.logic.commands.LibraryCommand;

//@@author qiu-siqi
public class LibraryCommandParserTest {
    private LibraryCommandParser parser = new LibraryCommandParser();

    @Test
    public void parse_validArgs_returnsLibraryCommand() {
        assertParseSuccess(parser, "1", new LibraryCommand(INDEX_FIRST_BOOK));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // No args
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, LibraryCommand.MESSAGE_USAGE));

        // Invalid arg
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, LibraryCommand.MESSAGE_USAGE));

        // Multiple args
        assertParseFailure(parser, "1 2", String.format(MESSAGE_INVALID_COMMAND_FORMAT, LibraryCommand.MESSAGE_USAGE));
    }
}
