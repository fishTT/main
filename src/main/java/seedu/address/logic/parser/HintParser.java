package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_AUTHOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AUTHOR_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMAND_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ISBN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ISBN_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RATING_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT_BY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT_BY_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE_STRING;

import java.util.Optional;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.AddAliasCommand;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AliasesCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteAliasCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.LibraryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RecentCommand;
import seedu.address.logic.commands.ReviewsCommand;
import seedu.address.logic.commands.SearchCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.ThemeCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author fishTT
/**
 * Class that is responsible for generating hints based on user input.
 * Contains one public method generateHint which returns an appropriate hint based on input.
 */
public class HintParser {

    private final Logic logic;
    private String commandWord;
    private String arguments;
    private String userInput;

    public HintParser(Logic logic) {
        this.logic = logic;
    }

    /**
     * Parses {@code String input} and returns an appropriate hint.
     */
    public String generateHint(String input) {
        String[] command;

        try {
            command = logic.parse(input);
        } catch (ParseException e) {
            return "";
        }

        userInput = input;
        commandWord = command[0];
        arguments = command[1];

        return generateHintContent();
    }

    /**
     * Returns an appropriate hint based on commandWord and arguments.
     * References userInput and arguments to decide whether whitespace should be added to
     * the front of the hint.
     */
    private String generateHintContent() {
        switch (commandWord) {
        case AddAliasCommand.COMMAND_WORD:
            return generateAddAliasHint();
        case AddCommand.COMMAND_WORD:
            return generateHintForIndexedCommand(" add a book");
        case AliasesCommand.COMMAND_WORD:
            return " list all command aliases";
        case ClearCommand.COMMAND_WORD:
            return " clear book shelf";
        case DeleteAliasCommand.COMMAND_WORD:
            return generateDeleteAliasHint();
        case DeleteCommand.COMMAND_WORD:
            return generateHintForIndexedCommand(" delete a book");
        case EditCommand.COMMAND_WORD:
            return generateEditHint();
        case ExitCommand.COMMAND_WORD:
            return " exit the app";
        case HelpCommand.COMMAND_WORD:
            return " show user guide";
        case HistoryCommand.COMMAND_WORD:
            return " show command history";
        case LibraryCommand.COMMAND_WORD:
            return generateHintForIndexedCommand(" find book in library");
        case ListCommand.COMMAND_WORD:
            return generateListHint();
        case RecentCommand.COMMAND_WORD:
            return " view recently selected books";
        case ReviewsCommand.COMMAND_WORD:
            return generateHintForIndexedCommand(" view book review");
        case SearchCommand.COMMAND_WORD:
            return generateSearchHint();
        case SelectCommand.COMMAND_WORD:
            return generateHintForIndexedCommand(" select a book");
        case ThemeCommand.COMMAND_WORD:
            return generateThemeHint();
        case UndoCommand.COMMAND_WORD:
            return " undo last modification";
        default:
            return "";
        }
    }

    //@@author
    /**
     * Parses the end of arguments to check if user is currently typing a prefix that is in prefixes.
     * Returns hint if user is typing a prefix.
     * Returns empty Optional if user is not typing a prefix.
     */
    private Optional<String> generatePrefixHintBasedOnEndArgs(Prefix... prefixes) {
        for (Prefix p : prefixes) {
            String prefixString = p.getPrefix();
            String parameter = prefixIntoParameter(p);
            for (int i = 1; i <= prefixString.length(); ++i) {
                if (arguments.endsWith(" " + prefixString.substring(0, i))) {
                    return Optional.of(prefixString.substring(i, prefixString.length()) + parameter);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Parses arguments to check for parameters that have not been used.
     * Only the specified {@code prefixes} will be checked.
     * Returns hint for all parameter that are not present.
     * Returns {@code defaultHint} if all parameters are present.
     */
    private String showUnusedParameters(String defaultHint, String preamble, Prefix... prefixes) {
        String whitespace = userInput.endsWith(" ") ? "" : " ";
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(arguments, prefixes);

        StringBuilder sb = new StringBuilder();
        if (preamble != null && argumentMultimap.getPreamble().trim().isEmpty()) {
            sb.append("[").append(preamble).append("] ");
        }

        for (Prefix p : prefixes) {
            Optional<String> parameterOptional = argumentMultimap.getValue(p);
            if (!parameterOptional.isPresent()) {
                sb.append("[").append(p.getPrefix()).append(prefixIntoParameter(p)).append("] ");
            }
        }
        if (sb.length() == 0) {
            return whitespace + defaultHint;
        }
        return whitespace + sb.toString();
    }

    //@@author fishTT
    /**
     * Currently this method is always called after generatePrefixHintBasedOnEndArgs.
     * It parses arguments to check for parameters that have not been filled up.
     * Only the specified {@code prefixes} will be checked.
     * Returns hint for parameter that is not present.
     * Returns {@code defaultHint} if all parameters are present.
     */
    private String offerHint(String defaultHint, Prefix... prefixes) {
        String whitespace = userInput.endsWith(" ") ? "" : " ";
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(arguments, prefixes);

        for (Prefix p : prefixes) {
            Optional<String> parameterOptional = argumentMultimap.getValue(p);
            if (!parameterOptional.isPresent()) {
                return whitespace + p.getPrefix() + prefixIntoParameter(p);
            }
        }
        return whitespace + defaultHint;
    }

    /**
     * Returns a parameter based on {@code prefix}.
     */
    private static String prefixIntoParameter(Prefix prefix) {
        switch (prefix.toString()) {
        case PREFIX_AUTHOR_STRING:
            return "AUTHOR";
        case PREFIX_CATEGORY_STRING:
            return "CATEGORY";
        case PREFIX_ISBN_STRING:
            return "ISBN";
        case PREFIX_TITLE_STRING:
            return "TITLE";
        case PREFIX_STATUS_STRING:
            return "STATUS";
        case PREFIX_PRIORITY_STRING:
            return "PRIORITY";
        case PREFIX_RATING_STRING:
            return "RATING";
        case PREFIX_SORT_BY_STRING:
            return "SORT_BY";
        case PREFIX_COMMAND_STRING:
            return "COMMAND";
        default:
            return "KEYWORD";
        }
    }

    /**
     * Parses arguments to check if index is present.
     * Checks on userInput to handle whitespace.
     * Returns "index" if index is not present, else returns an empty Optional.
     */
    private Optional<String> generateIndexHint() {
        String whitespace = userInput.endsWith(" ") ? "" : " ";
        try {
            ParserUtil.parseIndex(arguments);
            return Optional.empty();
        } catch (IllegalValueException ive) {
            if (arguments.matches(".*\\s\\d+\\s.*")) {
                return Optional.empty();
            }
            return Optional.of(whitespace + "INDEX");
        }
    }

    //@@author
    /**
     * Parses arguments to check if a preamble is present.
     * Returns {@code parameterName} if preamble is not present, else returns an empty Optional.
     */
    private Optional<String> generatePreambleHint(String parameterName) {
        String whitespace = userInput.endsWith(" ") ? "" : " ";
        if (arguments.matches("\\s+\\S+.*")) {
            return Optional.empty();
        }
        return Optional.of(whitespace + parameterName);
    }

    /**
     * Returns a hint for commands that accepts only an index.
     */
    private String generateHintForIndexedCommand(String defaultMessage) {
        Optional<String> indexHintOptional = generateIndexHint();
        return indexHintOptional.orElse(defaultMessage);
    }

    /** Returns a hint specific to the addalias command. */
    private String generateAddAliasHint() {
        Optional<String> preambleHintOptional = generatePreambleHint("ALIAS_NAME");
        if (preambleHintOptional.isPresent()) {
            return preambleHintOptional.get();
        }
        Optional<String> endHintOptional = generatePrefixHintBasedOnEndArgs(PREFIX_COMMAND);
        return endHintOptional.orElseGet(() -> offerHint(" add a command alias", PREFIX_COMMAND));
    }

    /** Returns a hint specific to the deletealias command. */
    private String generateDeleteAliasHint() {
        Optional<String> preambleHintOptional = generatePreambleHint("ALIAS_NAME");
        return preambleHintOptional.orElse(" delete a command alias");
    }

    /**
     * Returns a hint specific to the edit command.
     */
    private String generateEditHint() {
        Optional<String> indexHintOptional = generateIndexHint();
        if (indexHintOptional.isPresent()) {
            return indexHintOptional.get();
        }

        Optional<String> endHintOptional =
                generatePrefixHintBasedOnEndArgs(PREFIX_STATUS, PREFIX_PRIORITY, PREFIX_RATING);
        return endHintOptional.orElseGet(() -> showUnusedParameters(" edit a book", null,
                PREFIX_STATUS, PREFIX_PRIORITY, PREFIX_RATING));

    }

    /** Returns a hint specific to the list command. */
    private String generateListHint() {
        Optional<String> endHintOptional = generatePrefixHintBasedOnEndArgs(PREFIX_TITLE, PREFIX_AUTHOR,
                PREFIX_CATEGORY, PREFIX_STATUS, PREFIX_PRIORITY, PREFIX_RATING, PREFIX_SORT_BY);
        return endHintOptional.orElseGet(() -> showUnusedParameters(" list, filter, and sort books", null, PREFIX_TITLE,
                PREFIX_AUTHOR, PREFIX_CATEGORY, PREFIX_STATUS, PREFIX_PRIORITY, PREFIX_RATING, PREFIX_SORT_BY));
    }

    /**
     * Returns a hint specific to the search command.
     */
    private String generateSearchHint() {
        Optional<String> endHintOptional = generatePrefixHintBasedOnEndArgs(PREFIX_ISBN,
                PREFIX_TITLE, PREFIX_AUTHOR, PREFIX_CATEGORY);
        return endHintOptional.orElseGet(() -> showUnusedParameters(" search for books", "KEY_WORDS",
                PREFIX_ISBN, PREFIX_TITLE, PREFIX_AUTHOR, PREFIX_CATEGORY));
    }

    /** Returns a hint specific to the theme command. */
    private String generateThemeHint() {
        Optional<String> preambleHintOptional = generatePreambleHint("THEME");
        return preambleHintOptional.orElse(" change app theme");
    }
}
