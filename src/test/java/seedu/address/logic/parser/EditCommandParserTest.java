package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_BOOK;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.model.book.Priority;
import seedu.address.model.book.Rating;
import seedu.address.model.book.Status;
import seedu.address.testutil.EditDescriptorBuilder;
//@@author 592363789
public class EditCommandParserTest {
    private static final int EMPTY_RATING = -1;
    private static final int NON_EMPTY_RATING = 5;
    private static final String STATUS_UNREAD = "u";
    private static final String PRIORITY_HIGH = "high";

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_indexSpecified_success() throws Exception {
        // edit status
        Index targetIndex = INDEX_FIRST_BOOK;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_STATUS + STATUS_UNREAD;
        EditCommand expectedCommand = new EditCommand(INDEX_FIRST_BOOK,
                new EditDescriptorBuilder().withStatus(Status.UNREAD).build());
        assertParseSuccess(parser, userInput, expectedCommand);

        // edit priority
        userInput = targetIndex.getOneBased() + " " + PREFIX_PRIORITY + PRIORITY_HIGH;
        expectedCommand = new EditCommand(INDEX_FIRST_BOOK,
                new EditDescriptorBuilder().withPriority(Priority.HIGH).build());
        assertParseSuccess(parser, userInput, expectedCommand);

        // edit rating
        userInput = targetIndex.getOneBased() + " " + PREFIX_RATING + NON_EMPTY_RATING;
        expectedCommand = new EditCommand(INDEX_FIRST_BOOK,
                new EditDescriptorBuilder().withRating(new Rating(NON_EMPTY_RATING)).build());
        assertParseSuccess(parser, userInput, expectedCommand);

        // delete rating
        userInput = targetIndex.getOneBased() + " " + PREFIX_RATING + EMPTY_RATING;
        expectedCommand = new EditCommand(INDEX_FIRST_BOOK,
                new EditDescriptorBuilder().withRating(new Rating(EMPTY_RATING)).build());
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingCompulsoryField_failure() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

        // no parameters
        assertParseFailure(parser, EditCommand.COMMAND_WORD + " 1", expectedMessage);

        // no parameters
        assertParseFailure(parser, EditCommand.COMMAND_WORD, expectedMessage);

        // no index
        assertParseFailure(parser, EditCommand.COMMAND_WORD + " " + NON_EMPTY_RATING + " "
                + Priority.DEFAULT_PRIORITY + " " + Status.DEFAULT_STATUS, expectedMessage);
    }
}
