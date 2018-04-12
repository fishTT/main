package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_AUTHOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ISBN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;

import seedu.address.logic.commands.SearchCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SearchCommand object.
 */
public class SearchCommandParser implements Parser<SearchCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SearchCommand
     * and returns a SearchCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format.
     */
    @Override
    public SearchCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ISBN, PREFIX_TITLE, PREFIX_AUTHOR, PREFIX_CATEGORY);

        SearchCommand.SearchDescriptor searchDescriptor = new SearchCommand.SearchDescriptor();

        String keyWords = argMultimap.getPreamble();
        if (keyWords.length() > 0) {
            searchDescriptor.setKeyWords(keyWords);
        }

        argMultimap.getValue(PREFIX_ISBN).ifPresent(searchDescriptor::setIsbn);
        argMultimap.getValue(PREFIX_TITLE).ifPresent(searchDescriptor::setTitle);
        argMultimap.getValue(PREFIX_AUTHOR).ifPresent(searchDescriptor::setAuthor);
        argMultimap.getValue(PREFIX_CATEGORY).ifPresent(searchDescriptor::setCategory);

        if (!searchDescriptor.isValid()) {
            throw new ParseException(SearchCommand.MESSAGE_EMPTY_QUERY);
        }

        return new SearchCommand(searchDescriptor);
    }
}
