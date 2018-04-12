package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.testutil.TypicalAliases.SEARCH;
import static seedu.address.testutil.TypicalAliases.UNREAD;
import static seedu.address.testutil.TypicalAliases.getTypicalAliasList;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_BOOK;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.AddAliasCommand;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AliasesCommand;
import seedu.address.logic.commands.DeleteAliasCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.LibraryCommand;
import seedu.address.logic.commands.ReviewsCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.alias.Alias;
import seedu.address.model.book.Priority;
import seedu.address.model.book.Rating;
import seedu.address.model.book.Status;
import seedu.address.testutil.EditDescriptorBuilder;

public class BookShelfParserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final BookShelfParser parser = new BookShelfParser(getTypicalAliasList());

    @Test
    public void constructor_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new BookShelfParser(null);
    }

    @Test
    public void applyCommandAlias_matchingAlias_success() throws Exception {
        String namedArgs = "s/unread by/title";
        String command = UNREAD.getName() + "  " + namedArgs;
        assertEquals(UNREAD.getPrefix() + " " + UNREAD.getNamedArgs() + " " + namedArgs,
                parser.applyCommandAlias(command));

        command = SEARCH.getName() + " 1984 a/george orwell   ";
        assertEquals(SEARCH.getPrefix() + " 1984 a/george orwell", parser.applyCommandAlias(command));
    }

    @Test
    public void applyCommandAlias_noMatchingAlias_doesNothing() throws Exception {
        String command = "helloworld 123456 z/arg1";
        assertEquals(command, parser.applyCommandAlias(command));
    }

    @Test
    public void applyCommandAlias_invalidInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        parser.applyCommandAlias("");
    }

    @Test
    public void parseCommand_addAlias() throws Exception {
        AddAliasCommand command = (AddAliasCommand) parser.parseCommand(AddAliasCommand.COMMAND_WORD
                + " urd " + PREFIX_COMMAND + "list s/unread");
        assertEquals(new AddAliasCommand(new Alias("urd", "list", "s/unread")), command);
    }

    @Test
    public void parseCommand_add() throws Exception {
        AddCommand command = (AddCommand) parser.parseCommand(AddCommand.COMMAND_WORD + " 1");
        assertEquals(new AddCommand(INDEX_FIRST_BOOK), command);
    }

    @Test
    public void parseCommand_aliases() throws Exception {
        assertTrue(parser.parseCommand(AliasesCommand.COMMAND_WORD) instanceof AliasesCommand);
        assertTrue(parser.parseCommand(AliasesCommand.COMMAND_WORD + " 3 abc") instanceof AliasesCommand);
    }

    @Test
    public void parseCommand_deleteAlias() throws Exception {
        DeleteAliasCommand command = (DeleteAliasCommand) parser.parseCommand(DeleteAliasCommand.COMMAND_WORD + " urd");
        assertEquals(new DeleteAliasCommand("urd"), command);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(DeleteCommand.COMMAND_WORD + " 1");
        assertEquals(new DeleteCommand(INDEX_FIRST_BOOK), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_BOOK.getOneBased() + " " + PREFIX_RATING + "2" + " " + PREFIX_PRIORITY
                + "n" + " " + PREFIX_STATUS + "u");
        assertEquals(new EditCommand(INDEX_FIRST_BOOK, new EditDescriptorBuilder().withRating(new Rating(2))
                .withPriority(Priority.NONE).withStatus(Status.UNREAD).build()), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_library() throws Exception {
        LibraryCommand command = (LibraryCommand) parser.parseCommand(LibraryCommand.COMMAND_WORD + " 1");
        assertEquals(new LibraryCommand(INDEX_FIRST_BOOK), command);
    }

    @Test
    public void parseCommand_history() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD + " 3") instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            fail("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }

    @Test
    public void parseCommand_reviewsCommandWord_returnsReviewsCommand() throws Exception {
        assertTrue(parser.parseCommand(ReviewsCommand.COMMAND_WORD + " 3") instanceof ReviewsCommand);
    }

    @Test
    public void parseCommand_undoCommandWord_returnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
        assertTrue(parser.parseCommand("undo 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        parser.parseCommand("");
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        parser.parseCommand("unknownCommand");
    }

    @Test
    public void attemptCommandAutoCorrection_add() throws Exception {
        assertEquals("list by/title", parser.attemptCommandAutoCorrection("lst by/title"));
        assertEquals("add 1", parser.attemptCommandAutoCorrection("dd 1"));
        assertEquals("deletealias r", parser.attemptCommandAutoCorrection("deletealia r"));
    }

    @Test
    public void attemptCommandAutoCorrection_remove() throws Exception {
        assertEquals("edit 1 s/r", parser.attemptCommandAutoCorrection("eedit 1 s/r"));
        assertEquals("delete 1", parser.attemptCommandAutoCorrection("deletee 1"));
        assertEquals("aliases", parser.attemptCommandAutoCorrection("alliases"));
    }

    @Test
    public void attemptCommandAutoCorrection_replace() throws Exception {
        assertEquals("edit 1 s/r", parser.attemptCommandAutoCorrection("adit 1 s/r"));
        assertEquals("recent", parser.attemptCommandAutoCorrection("resent"));
        assertEquals("addalias a cmd/add", parser.attemptCommandAutoCorrection("addaliae a cmd/add"));
    }

    @Test
    public void attemptCommandAutoCorrection_unknownCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        parser.attemptCommandAutoCorrection("addbook 1");
    }

    @Test
    public void attemptCommandAutoCorrection_tooDifferent_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        parser.attemptCommandAutoCorrection("eidt 1 s/r");
    }

    @Test
    public void attemptCommandAutoCorrection_longText_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        parser.attemptCommandAutoCorrection("thisismetryingtomakeyoursystemcrashbygivingareallylongstring 1");
    }
}
