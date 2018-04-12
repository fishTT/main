package seedu.address.logic.parser;

import static seedu.address.logic.commands.CommandTestUtil.AUTHOR_DESC_ARTEMIS;
import static seedu.address.logic.commands.CommandTestUtil.AUTHOR_DESC_BABYLON;
import static seedu.address.logic.commands.CommandTestUtil.CATEGORY_DESC_ARTEMIS;
import static seedu.address.logic.commands.CommandTestUtil.ISBN_DESC_ARTEMIS;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_ARTEMIS;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_BABYLON;
import static seedu.address.logic.commands.CommandTestUtil.VALID_AUTHOR_ARTEMIS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_AUTHOR_BABYLON;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CATEGORY_ARTEMIS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ISBN_ARTEMIS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_ARTEMIS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_BABYLON;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.SearchCommand;
import seedu.address.logic.commands.SearchCommand.SearchDescriptor;
import seedu.address.testutil.SearchDescriptorBuilder;

//@@author takuyakanbr
public class SearchCommandParserTest {
    private static final String DEFAULT_SEARCH_TERM = "search term";

    private SearchCommandParser parser = new SearchCommandParser();

    @Test
    public void parse_noFieldSpecified_failure() {
        // no search term and no parameters specified
        assertParseFailure(parser, "", SearchCommand.MESSAGE_EMPTY_QUERY);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        String userInput = DEFAULT_SEARCH_TERM + TITLE_DESC_ARTEMIS + CATEGORY_DESC_ARTEMIS
                + ISBN_DESC_ARTEMIS + AUTHOR_DESC_ARTEMIS;
        SearchDescriptor searchDescriptor = new SearchDescriptorBuilder().withTitle(VALID_TITLE_ARTEMIS)
                .withCategory(VALID_CATEGORY_ARTEMIS).withIsbn(VALID_ISBN_ARTEMIS)
                .withAuthor(VALID_AUTHOR_ARTEMIS).withSearchTerm(DEFAULT_SEARCH_TERM).build();
        SearchCommand expectedCommand = new SearchCommand(searchDescriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        String userInput = DEFAULT_SEARCH_TERM + TITLE_DESC_ARTEMIS + AUTHOR_DESC_ARTEMIS;
        SearchDescriptor searchDescriptor = new SearchDescriptorBuilder().withTitle(VALID_TITLE_ARTEMIS)
                .withAuthor(VALID_AUTHOR_ARTEMIS).withSearchTerm(DEFAULT_SEARCH_TERM).build();
        SearchCommand expectedCommand = new SearchCommand(searchDescriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        String userInput = DEFAULT_SEARCH_TERM;
        SearchDescriptor searchDescriptor = new SearchDescriptorBuilder().withSearchTerm(DEFAULT_SEARCH_TERM).build();
        SearchCommand expectedCommand = new SearchCommand(searchDescriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = AUTHOR_DESC_ARTEMIS;
        searchDescriptor = new SearchDescriptorBuilder().withAuthor(VALID_AUTHOR_ARTEMIS).build();
        expectedCommand = new SearchCommand(searchDescriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = CATEGORY_DESC_ARTEMIS;
        searchDescriptor = new SearchDescriptorBuilder().withCategory(VALID_CATEGORY_ARTEMIS).build();
        expectedCommand = new SearchCommand(searchDescriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = ISBN_DESC_ARTEMIS;
        searchDescriptor = new SearchDescriptorBuilder().withIsbn(VALID_ISBN_ARTEMIS).build();
        expectedCommand = new SearchCommand(searchDescriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = TITLE_DESC_ARTEMIS;
        searchDescriptor = new SearchDescriptorBuilder().withTitle(VALID_TITLE_ARTEMIS).build();
        expectedCommand = new SearchCommand(searchDescriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        String userInput = DEFAULT_SEARCH_TERM + TITLE_DESC_ARTEMIS + TITLE_DESC_ARTEMIS + AUTHOR_DESC_ARTEMIS
                + AUTHOR_DESC_ARTEMIS + TITLE_DESC_BABYLON + AUTHOR_DESC_BABYLON;
        SearchDescriptor searchDescriptor = new SearchDescriptorBuilder().withTitle(VALID_TITLE_BABYLON)
                .withAuthor(VALID_AUTHOR_BABYLON).withSearchTerm(DEFAULT_SEARCH_TERM).build();
        SearchCommand expectedCommand = new SearchCommand(searchDescriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

}
