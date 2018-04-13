package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.LIST_OF_PREFIXES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AUTHOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AUTHOR_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPTY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ISBN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ISBN_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RATING_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT_BY_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS_STRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE_STRING;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.Logic;
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
            return " type help for guide";
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
        case AddCommand.COMMAND_WORD:
            return generateAddHint();
        case EditCommand.COMMAND_WORD:
            return generateEditHint();
        case SelectCommand.COMMAND_WORD:
            return generateSelectHint();
        case DeleteCommand.COMMAND_WORD:
            return generateDeleteHint();
        case ClearCommand.COMMAND_WORD:
            return " clear book shelf";
        case ListCommand.COMMAND_WORD:
            return " list books";
        case HistoryCommand.COMMAND_WORD:
            return " show command history";
        case ExitCommand.COMMAND_WORD:
            return " exit the app";
        case HelpCommand.COMMAND_WORD:
            return " show user guide";
        case UndoCommand.COMMAND_WORD:
            return " undo last modification";
        case SearchCommand.COMMAND_WORD:
            return generateSearchHint();
        default:
            return " type help for guide";
        }
    }

    /**
     * Parses the end of arguments to check if user is currently typing a prefix that is in prefixes.
     * Returns hint if user is typing a prefix.
     * Returns empty Optional if user is not typing a prefix.
     */
    private Optional<String> generatePrefixHintBasedOnEndArgs(Prefix... prefixes) {

        Set<Prefix> prefixSet = Arrays.stream(prefixes).collect(Collectors.toSet());

        for (Prefix p : LIST_OF_PREFIXES) {
            if (!prefixSet.contains(p)) {
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
     * Currently this method is always called after generatePrefixHintBasedOnEndArgs.
     * It parses arguments to check for parameters that have not been filled up.
     * Only the specified {@code prefixes} will be checked.
     * Returns hint for parameter that is not present.
     * Returns returns {@code defaultHint} if all parameters are present.
     */
    private String offerHint(String defaultHint, Prefix... prefixes) {

        Set<Prefix> prefixSet = Arrays.stream(prefixes).collect(Collectors.toSet());

        List<Prefix> prefixList = new ArrayList<>();
        for (Prefix p : LIST_OF_PREFIXES) {
            if (prefixSet.contains(p)) {
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
            return Optional.of(whitespace + "index");
        }
    }

    /**
     * Returns a hint specific to the add command.
     */
    private String generateAddHint() {
        Optional<String> indexHintOptional = generateIndexHint();
        return indexHintOptional.orElse(" add a book");
    }

    /**
     * Returns a hint specific to the delete command.
     */
    private String generateDeleteHint() {
        Optional<String> indexHintOptional = generateIndexHint();
        return indexHintOptional.orElse(" delete a book");
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
        return endHintOptional.orElseGet(() -> offerHint(" edit a book",
                PREFIX_STATUS, PREFIX_PRIORITY, PREFIX_RATING));

    }

    /**
     * Returns a hint specific to the select command.
     */
    private String generateSelectHint() {
        Optional<String> indexHintOptional = generateIndexHint();
        return indexHintOptional.orElse(" select a book");
    }

    /**
     * Returns a hint specific to the search command.
     */
    private String generateSearchHint() {
        Optional<String> endHintOptional = generatePrefixHintBasedOnEndArgs(PREFIX_EMPTY, PREFIX_ISBN,
                PREFIX_TITLE, PREFIX_AUTHOR, PREFIX_CATEGORY);
        return endHintOptional.orElseGet(() -> offerHint(" search for books", PREFIX_EMPTY, PREFIX_ISBN,
                PREFIX_TITLE, PREFIX_AUTHOR, PREFIX_CATEGORY));
    }
}
