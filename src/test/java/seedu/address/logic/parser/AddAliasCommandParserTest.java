package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMAND;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.AddAliasCommand;
import seedu.address.model.alias.Alias;

//@@author takuyakanbr
public class AddAliasCommandParserTest {
    private AddAliasCommandParser parser = new AddAliasCommandParser();

    @Test
    public void parse_validArgs_success() {
        // command without named argument
        assertParseSuccess(parser, "s " + PREFIX_COMMAND + "search",
                new AddAliasCommand(new Alias("s", "search", "")));

        // command with named argument
        assertParseSuccess(parser, "e " + PREFIX_COMMAND + "edit s/reading",
                new AddAliasCommand(new Alias("e", "edit", "s/reading")));

        // leading and trailing spaces should be removed
        assertParseSuccess(parser, "      e      " + PREFIX_COMMAND + "        edit s/reading        ",
                new AddAliasCommand(new Alias("e", "edit", "s/reading")));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // no args
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAliasCommand.MESSAGE_USAGE));

        // empty alias name
        assertParseFailure(parser, "     " + PREFIX_COMMAND + "edit s/reading",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAliasCommand.MESSAGE_USAGE));

        // invalid alias name
        assertParseFailure(parser, " hello world " + PREFIX_COMMAND + "edit s/reading",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAliasCommand.MESSAGE_USAGE));

        // empty aliased command
        assertParseFailure(parser, " e " + PREFIX_COMMAND + "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAliasCommand.MESSAGE_USAGE));
    }
}
