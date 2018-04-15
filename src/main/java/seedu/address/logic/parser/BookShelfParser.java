package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import seedu.address.commons.util.StringUtil;
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
import seedu.address.logic.commands.LibraryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.LockCommand;
import seedu.address.logic.commands.RecentCommand;
import seedu.address.logic.commands.ReviewsCommand;
import seedu.address.logic.commands.SearchCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.SetPasswordCommand;
import seedu.address.logic.commands.ThemeCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.UnlockCommand;
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
    public static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile(" *(?<commandWord>\\S+)(?<arguments>.*)");

    //@@author takuyakanbr
    /**
     * Used for pre-processing the user input, before the application of a command alias.
     */
    private static final Pattern ALIASED_COMMAND_FORMAT =
            Pattern.compile(" *(?<aliasName>\\S+)(?<unnamedArgs>((?! [\\w]+\\/.*)[\\S ])*)(?<namedArgs>.*)");

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
        final Matcher matcher = getMatcherForPattern(userInput, ALIASED_COMMAND_FORMAT);

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
        return Stream.of(alias.getPrefix(), unnamedArgs, alias.getNamedArgs(), namedArgs)
                // keep trailing spaces as they may affect the command hint shown to user
                .map(StringUtil::leftTrim)
                .filter(str -> !str.trim().isEmpty())
                .reduce("", (a, b) -> StringUtil.leftTrim(a + " " + b));
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
        final Matcher matcher = getMatcherForPattern(commandText, BASIC_COMMAND_FORMAT);

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        return getCommand(commandWord, arguments);
    }

    /**
     * Obtains the command given {@code commandWord} and {@code arguments}.
     * @throws ParseException if the commandWord is not a valid command.
     */
    private Command getCommand(String commandWord, String arguments) throws ParseException {
        switch (commandWord.toLowerCase()) {

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

        case LibraryCommand.COMMAND_WORD:
            return new LibraryCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommandParser().parse(arguments);

        case LockCommand.COMMAND_WORD:
            return new LockCommand();

        case RecentCommand.COMMAND_WORD:
            return new RecentCommand();

        case ReviewsCommand.COMMAND_WORD:
            return new ReviewsCommandParser().parse(arguments);

        case SearchCommand.COMMAND_WORD:
            return new SearchCommandParser().parse(arguments);

        case SelectCommand.COMMAND_WORD:
            return new SelectCommandParser().parse(arguments);

        case SetPasswordCommand.COMMAND_WORD:
            return new SetPasswordCommandParser().parse(arguments);

        case ThemeCommand.COMMAND_WORD:
            return new ThemeCommandParser().parse(arguments);

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        case UnlockCommand.COMMAND_WORD:
            return new UnlockCommand(arguments);

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    /**
     * Obtains a matcher given {@code commandText} which is to be matched to {@code commandFormat}.
     * @throws ParseException if {@code commandText} does not match {@code commandFormat}.
     */
    private Matcher getMatcherForPattern(String commandText, Pattern commandFormat) throws ParseException {
        final Matcher matcher = commandFormat.matcher(commandText);
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }
        return matcher;
    }

}
