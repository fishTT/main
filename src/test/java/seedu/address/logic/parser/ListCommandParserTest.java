package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListCommand.FilterDescriptor;
import seedu.address.logic.parser.ListCommandParser.SortMode;
import seedu.address.model.Model;
import seedu.address.model.book.Priority;
import seedu.address.model.book.Rating;
import seedu.address.model.book.Status;
import seedu.address.testutil.FilterDescriptorBuilder;

//@@author takuyakanbr
public class ListCommandParserTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ListCommandParser parser = new ListCommandParser();

    @Test
    public void parse_noParameterSpecified_success() {
        ListCommand expectedCommand = new ListCommand(new FilterDescriptor(), Model.DEFAULT_BOOK_COMPARATOR);
        assertParseSuccess(parser, "", expectedCommand);
    }

    @Test
    public void parse_allParametersSpecified_success() {
        String userInput = " t/t1 a/a1 c/c1 s/read p/low r/0 by/title";
        FilterDescriptor descriptor = new FilterDescriptorBuilder().withTitleFilter("t1")
                .withAuthorFilter("a1").withCategoryFilter("c1").withStatusFilter(Status.READ)
                .withPriorityFilter(Priority.LOW).withRatingFilter(new Rating(0)).build();
        ListCommand expectedCommand = new ListCommand(descriptor, SortMode.TITLE.getComparator());
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someParametersSpecified_success() {
        String userInput = " by/ratingd s/read p/low t/t1";
        FilterDescriptor descriptor = new FilterDescriptorBuilder().withTitleFilter("t1")
                .withStatusFilter(Status.READ).withPriorityFilter(Priority.LOW).build();
        ListCommand expectedCommand = new ListCommand(descriptor, SortMode.RATINGD.getComparator());
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFilterSpecified_success() {
        String userInput = " t/t1";
        FilterDescriptor descriptor = new FilterDescriptorBuilder().withTitleFilter("t1").build();
        ListCommand expectedCommand = new ListCommand(descriptor, Model.DEFAULT_BOOK_COMPARATOR);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = " a/a1";
        descriptor = new FilterDescriptorBuilder().withAuthorFilter("a1").build();
        expectedCommand = new ListCommand(descriptor, Model.DEFAULT_BOOK_COMPARATOR);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = " c/c1";
        descriptor = new FilterDescriptorBuilder().withCategoryFilter("c1").build();
        expectedCommand = new ListCommand(descriptor, Model.DEFAULT_BOOK_COMPARATOR);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = " s/read";
        descriptor = new FilterDescriptorBuilder().withStatusFilter(Status.READ).build();
        expectedCommand = new ListCommand(descriptor, Model.DEFAULT_BOOK_COMPARATOR);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = " p/low";
        descriptor = new FilterDescriptorBuilder().withPriorityFilter(Priority.LOW).build();
        expectedCommand = new ListCommand(descriptor, Model.DEFAULT_BOOK_COMPARATOR);
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = " r/0";
        descriptor = new FilterDescriptorBuilder().withRatingFilter(new Rating(0)).build();
        expectedCommand = new ListCommand(descriptor, Model.DEFAULT_BOOK_COMPARATOR);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFilters_acceptsLast() {
        String userInput = " s/unread s/rd t/hello t/world t/t1 s/read";
        FilterDescriptor descriptor = new FilterDescriptorBuilder()
                .withTitleFilter("t1").withStatusFilter(Status.READ).build();
        ListCommand expectedCommand = new ListCommand(descriptor, Model.DEFAULT_BOOK_COMPARATOR);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_validSortBy_success() {
        // select sorting mode using its name
        String userInput = " by/title";
        ListCommand expectedCommand = new ListCommand(new FilterDescriptor(), SortMode.TITLE.getComparator());
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = " by/ratingd";
        expectedCommand = new ListCommand(new FilterDescriptor(), SortMode.RATINGD.getComparator());
        assertParseSuccess(parser, userInput, expectedCommand);

        // select sorting mode using its alias
        userInput = " by/p";
        expectedCommand = new ListCommand(new FilterDescriptor(), SortMode.PRIORITY.getComparator());
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = " by/rd";
        expectedCommand = new ListCommand(new FilterDescriptor(), SortMode.RATINGD.getComparator());
        assertParseSuccess(parser, userInput, expectedCommand);

        // sorting mode selection should be case insensitive
        userInput = " by/StatusD";
        expectedCommand = new ListCommand(new FilterDescriptor(), SortMode.STATUSD.getComparator());
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = " by/PA";
        expectedCommand = new ListCommand(new FilterDescriptor(), SortMode.PRIORITY.getComparator());
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedSortBy_acceptsLast() {
        String userInput = " by/title by/rating by/priority by/status";
        ListCommand expectedCommand = new ListCommand(new FilterDescriptor(), SortMode.STATUS.getComparator());
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidFilterOrSortBy_failure() {
        assertParseFailure(parser, " s/readd", Messages.MESSAGE_INVALID_STATUS);
        assertParseFailure(parser, " t/title p/", Messages.MESSAGE_INVALID_PRIORITY);
        assertParseFailure(parser, " p/123", Messages.MESSAGE_INVALID_PRIORITY);
        assertParseFailure(parser, " r/-2", Messages.MESSAGE_INVALID_RATING);
        assertParseFailure(parser, " r/zero c/category", Messages.MESSAGE_INVALID_RATING);

        assertParseFailure(parser, " by/author", Messages.MESSAGE_INVALID_SORT_BY);
        assertParseFailure(parser, " by/", Messages.MESSAGE_INVALID_SORT_BY);
        assertParseFailure(parser, " by/1", Messages.MESSAGE_INVALID_SORT_BY);

        // multiple invalid items -> show first error message from list [status, priority, rating, sort by]
        assertParseFailure(parser, " s/readd p/ r/zero p/123 s/123 by/1", Messages.MESSAGE_INVALID_STATUS);
        assertParseFailure(parser, " by/1 r/zero r/-2 p/123 p/", Messages.MESSAGE_INVALID_PRIORITY);
    }
}
