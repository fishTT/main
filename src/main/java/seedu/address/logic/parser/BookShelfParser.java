package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AddAliasCommand;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AliasesCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteAliasCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RecentCommand;
import seedu.address.logic.commands.ReviewsCommand;
import seedu.address.logic.commands.SearchCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.ThemeCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.alias.Alias;
import seedu.address.model.alias.ReadOnlyAliasList;

/**
 * Parses user input.
 */
public class BookShelfParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    //@@author takuyakanbr
    /**
     * Used for pre-processing the user input, before the application of a command alias.
     */
    private static final Pattern ALIASED_COMMAND_FORMAT =
            Pattern.compile("(?<aliasName>\\S+)(?<unnamedArgs>((?! [\\w]+\\/.*)[\\S ])*)(?<namedArgs>.*)");

    private final ReadOnlyAliasList aliases;

    public BookShelfParser(ReadOnlyAliasList aliases) {
        requireNonNull(aliases);
        this.aliases = aliases;
    }

    /**
     * Applies a command alias (if any) to the user input, and returns the result.
     * If no command alias can be applied, the user input will be returned unchanged.
     *
     * @param userInput full user input string.
     * @return the processed input after the application of command alias.
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public String applyCommandAlias(String userInput) throws ParseException {
        final Matcher matcher = ALIASED_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String aliasName = matcher.group("aliasName");
        if (!aliases.getAliasByName(aliasName).isPresent()) {
            return userInput;
        }

        return buildCommand(aliases.getAliasByName(aliasName).get(),
                matcher.group("unnamedArgs"), matcher.group("namedArgs"));
    }

    /**
     * Builds and returns a command string from the given alias, unnamed arguments, and named arguments.
     */
    private static String buildCommand(Alias alias, String unnamedArgs, String namedArgs) {
        String commandPrefix = alias.getPrefix() + " " + unnamedArgs.trim();
        String commandNamedArgs = alias.getNamedArgs() + " " + namedArgs.trim();
        String result = commandPrefix.trim() + " " + commandNamedArgs.trim();
        return result.trim();
    }

    //@@author
    /**
     * Parses a command string into a command for execution.
     *
     * @param commandText the command string.
     * @return the resulting command based on the command string.
     * @throws ParseException if the command string does not conform the expected format.
     */
    public Command parseCommand(String commandText) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(commandText.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        switch (commandWord) {

        case AddAliasCommand.COMMAND_WORD:
            return new AddAliasCommandParser().parse(arguments);

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case AliasesCommand.COMMAND_WORD:
            return new AliasesCommand();

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case DeleteAliasCommand.COMMAND_WORD:
            return new DeleteAliasCommand(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case HistoryCommand.COMMAND_WORD:
            return new HistoryCommand();

        case ListCommand.COMMAND_WORD:
            return new ListCommandParser().parse(arguments);

        case RecentCommand.COMMAND_WORD:
            return new RecentCommand();

        case ReviewsCommand.COMMAND_WORD:
            return new ReviewsCommandParser().parse(arguments);

        case SearchCommand.COMMAND_WORD:
            return new SearchCommandParser().parse(arguments);

        case SelectCommand.COMMAND_WORD:
            return new SelectCommandParser().parse(arguments);

        case ThemeCommand.COMMAND_WORD:
            return new ThemeCommandParser().parse(arguments);

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }
}
