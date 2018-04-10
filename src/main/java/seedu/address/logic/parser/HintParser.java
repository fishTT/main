package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.LIST_OF_PREFIXES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AUTHOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AUTHOR_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPTY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPTY_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ISBN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ISBN_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RATING_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT_BY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT_BY_STRING;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.SearchCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Class that is responsible for generating hints based on user input
 * Contains one public method generateHint which returns an appropriate hint based on input
 */
public class HintParser {

    private static String commandWord;
    private static String arguments;
    private static String userInput;

    /**
     * Parses {@code String input} and returns an appropriate hint
     */
    public static String generateHint(String input) {
        //the ordering matters as prefix hints are generated inorder
        assert LIST_OF_PREFIXES.equals(Arrays.asList(
                PREFIX_AUTHOR, PREFIX_CATEGORY, PREFIX_DESCRIPTION, PREFIX_ISBN,
                PREFIX_TITLE, PREFIX_STATUS, PREFIX_PRIORITY, PREFIX_RATING, PREFIX_SORT_BY));

        String[] command;

        try {
            command = ParserUtil.parseCommandAndArguments(input);
        } catch (ParseException e) {
            return " type help for guide";
        }

        userInput = input;
        commandWord = command[0];
        arguments = command[1];
        String hintContent = generateHintContent();

        return hintContent;
    }

    /**
     * returns an appropriate hint based on commandWord and arguments
     * userInput and arguments are referenced to decide whether whitespace should be added to
     * the front of the hint
     */
    private static String generateHintContent() {
        switch (commandWord) {
            case AddCommand.COMMAND_WORD:
                return generateAddHint();
            case EditCommand.COMMAND_WORD:
                return generateEditHint();
            case SelectCommand.COMMAND_WORD:
				return " Select a book";
            case DeleteCommand.COMMAND_WORD:
                return generateDeleteAndSelectHint();
            case ClearCommand.COMMAND_WORD:
                return " clears a book";
            case ListCommand.COMMAND_WORD:
                return " lists all books";
            case HistoryCommand.COMMAND_WORD:
                return " show command history";
            case ExitCommand.COMMAND_WORD:
                return " exits the app";
            case HelpCommand.COMMAND_WORD:
                return " shows user guide";
            case UndoCommand.COMMAND_WORD:
                return " undo command";
			case SearchCommand.COMMAND_WORD:
				return " search a book";
            default:
                return " type help for guide";
        }
    }

    /**
     * parses the end of arguments to check if user is currently typing a prefix that is not in ignoredPrefixes
     * returns hint if user is typing a prefix
     * returns empty Optional if user is not typing a prefix
     */
    private static Optional<String> generatePrefixHintBasedOnEndArgs(Prefix... ignoredPrefixes) {

        Set<Prefix> ignoredPrefixSet = Arrays.asList(ignoredPrefixes).stream().collect(Collectors.toSet());

        for (Prefix p : LIST_OF_PREFIXES) {
            if (ignoredPrefixSet.contains(p)) {
                continue;
            }
            String prefixLetter = " " + (p.getPrefix().toCharArray()[0]); // " n"
            String identifier = "" + (p.getPrefix().toCharArray()[1]); // "/"
            String parameter = prefixIntoParameter(p);

            if (arguments.endsWith(p.getPrefix())) {
                return Optional.of(parameter);
            } else if (arguments.endsWith(prefixLetter)) {
                return Optional.of(identifier + parameter);
            }
        }
        return Optional.empty();
    }

    /**
     * Currently this method is always called after generatePrefixHintBasedOnEndArgs
     * It parses arguments to check for parameters that have not been filled up
     * {@code ignoredPrefixes} are omitted during this check
     * returns hint for parameter that is not present
     * returns returns {@code defaultHint} if all parameters are present
     */
    private static String offerHint(String defaultHint, Prefix... ignoredPrefixes) {

        Set<Prefix> ignoredPrefixesSet = Arrays.asList(ignoredPrefixes).stream().collect(Collectors.toSet());

        //remove ignored prefixes without losing order
        List<Prefix> prefixList = new ArrayList<>();
        for (Prefix p : LIST_OF_PREFIXES) {
            if (!ignoredPrefixesSet.contains(p)) {
                prefixList.add(p);
            }
        }

        String whitespace = userInput.endsWith(" ") ? "" : " ";
        ArgumentMultimap argumentMultimap =
                ArgumentTokenizer.tokenize(arguments, prefixList.toArray(new Prefix[0]));

        for (Prefix p : prefixList) {

            Optional<String> parameterOptional = argumentMultimap.getValue(p);
            if (!parameterOptional.isPresent()) {
                return whitespace + p.getPrefix() + prefixIntoParameter(p);
            }
        }
        return whitespace + defaultHint;
    }

    /**
     * returns a parameter based on {@code prefix}
     */
    private static String prefixIntoParameter(Prefix prefix) {
        switch (prefix.toString()) {
            case PREFIX_AUTHOR_STRING:
                return "AUTHOR";
            case PREFIX_CATEGORY_STRING:
                return "CATEGORY";
            case PREFIX_DESCRIPTION_STRING:
                return "DESCRIPTION";
            case PREFIX_ISBN_STRING:
                return "ISBN";
            case PREFIX_TITLE_STRING:
                return "TITLE";
            case PREFIX_STATUS_STRING:
                return "STATUR";
            case PREFIX_PRIORITY_STRING:
                return "PRIORITY";
            case PREFIX_RATING_STRING:
                return "RATING";
            case PREFIX_SORT_BY_STRING:
                return "SORTBY";
            default:
                return "KEYWORD";
        }
    }

    /**
     * parses arguments to check if index is present
     * checks on userInput to handle whitespace
     * returns "index" if index is not present
     * else returns an empty Optional
     */
    private static Optional<String> generateIndexHint() {
        String whitespace = userInput.endsWith(" ") ? "" : " ";
        try {
            ParserUtil.parseIndex(arguments);
            return Optional.empty();
        } catch (IllegalValueException ive) {
            if (arguments.matches(".*\\s\\d+\\s.*")) {
                return Optional.empty();
            }
            return Optional.of(whitespace + "index");
        }
    }

    /**
     * returns a hint specific to the add command
     */
    private static String generateAddHint() {

        Optional<String> endHintOptional = generatePrefixHintBasedOnEndArgs(PREFIX_EMPTY, PREFIX_REMARK);
        if (endHintOptional.isPresent()) {
            return endHintOptional.get();
        }
        return offerHint("", PREFIX_EMPTY, PREFIX_REMARK);
    }

    /**
     * returns a hint specific to the edit command
     */
    private static String generateEditHint() {
        Optional<String> indexHintOptional = generateIndexHint();
        if (indexHintOptional.isPresent()) {
            return indexHintOptional.get();
        }

        Optional<String> endHintOptional = generatePrefixHintBasedOnEndArgs(PREFIX_EMPTY, PREFIX_REMARK);
        if (endHintOptional.isPresent()) {
            return endHintOptional.get();
        }

        return offerHint("prefix/KEYWORD", PREFIX_EMPTY, PREFIX_REMARK);
    }

    /**
     * returns a hint specific to the find command
     */
    private static String generateFindHint() {
        Optional<String> endHintOptional = generatePrefixHintBasedOnEndArgs(PREFIX_EMPTY, PREFIX_REMARK);

        if (endHintOptional.isPresent()) {
            return endHintOptional.get();
        }
        return offerHint("prefix/KEYWORD", PREFIX_EMPTY, PREFIX_REMARK);
    }

    /**
     * returns a hint specific to the select and delete command
     */
    private static String generateDeleteAndSelectHint() {
        Optional<String> indexHintOptional = generateIndexHint();
        if (indexHintOptional.isPresent()) {
            return indexHintOptional.get();
        }
        return "";
    }

}
