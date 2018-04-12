package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static seedu.address.logic.commands.CommandTestUtil.showBookAtIndex;
import static seedu.address.testutil.TypicalBooks.ARTEMIS;
import static seedu.address.testutil.TypicalBooks.BABYLON_ASHES;
import static seedu.address.testutil.TypicalBooks.COLLAPSING_EMPIRE;
import static seedu.address.testutil.TypicalBooks.CONSIDER_PHLEBAS;
import static seedu.address.testutil.TypicalBooks.WAKING_GODS;
import static seedu.address.testutil.TypicalBooks.getTypicalBookShelf;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_BOOK;

import java.util.Comparator;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoStack;
import seedu.address.logic.commands.ListCommand.FilterDescriptor;
import seedu.address.logic.parser.ListCommandParser.SortMode;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.book.Book;
import seedu.address.model.book.Priority;
import seedu.address.model.book.Rating;
import seedu.address.model.book.Status;
import seedu.address.network.NetworkManager;
import seedu.address.testutil.FilterDescriptorBuilder;

//@@author takuyakanbr
/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalBookShelf(), new UserPrefs());
    }

    @Test
    public void execute_noParameterSpecified_success() {
        // empty filter, default comparator -> shows everything
        assertExecutionSuccess(new FilterDescriptor(), Model.DEFAULT_BOOK_COMPARATOR, 5);
        assertEquals(BABYLON_ASHES, model.getDisplayBookList().get(0));
        assertEquals(COLLAPSING_EMPIRE, model.getDisplayBookList().get(1));
        assertEquals(CONSIDER_PHLEBAS, model.getDisplayBookList().get(2));
        assertEquals(WAKING_GODS, model.getDisplayBookList().get(3));
        assertEquals(ARTEMIS, model.getDisplayBookList().get(4));

        // filtered list -> shows everything
        showBookAtIndex(model, INDEX_FIRST_BOOK);
        assertExecutionSuccess(new FilterDescriptor(), Model.DEFAULT_BOOK_COMPARATOR, 5);
        assertEquals(BABYLON_ASHES, model.getDisplayBookList().get(0));
        assertEquals(COLLAPSING_EMPIRE, model.getDisplayBookList().get(1));
        assertEquals(CONSIDER_PHLEBAS, model.getDisplayBookList().get(2));
        assertEquals(WAKING_GODS, model.getDisplayBookList().get(3));
        assertEquals(ARTEMIS, model.getDisplayBookList().get(4));
    }

    @Test
    public void execute_allParametersSpecified_success() {
        // matches 1 book: ARTEMIS
        FilterDescriptor descriptor = new FilterDescriptorBuilder().withTitleFilter(ARTEMIS.getTitle().title)
                .withAuthorFilter("andy weir").withCategoryFilter("fiction").withStatusFilter(Status.READ)
                .withPriorityFilter(Priority.LOW).withRatingFilter(new Rating(5)).build();
        assertExecutionSuccess(descriptor, SortMode.STATUSD.getComparator(), 1);
        assertEquals(ARTEMIS, model.getDisplayBookList().get(0));

        // matches 0 books
        descriptor = new FilterDescriptorBuilder().withTitleFilter(ARTEMIS.getTitle().title)
                .withAuthorFilter("andy weir").withCategoryFilter("fiction").withStatusFilter(Status.UNREAD)
                .withPriorityFilter(Priority.HIGH).withRatingFilter(new Rating(-1)).build();
        assertExecutionSuccess(descriptor, SortMode.PRIORITYD.getComparator(), 0);
    }

    @Test
    public void execute_someParametersSpecified_success() {
        // matches 2 books: CONSIDER_PHLEBAS, WAKING_GODS
        FilterDescriptor descriptor = new FilterDescriptorBuilder()
                .withStatusFilter(Status.UNREAD).withRatingFilter(new Rating(-1)).build();
        assertExecutionSuccess(descriptor, SortMode.TITLE.getComparator(), 2);
        assertEquals(CONSIDER_PHLEBAS, model.getDisplayBookList().get(0));
        assertEquals(WAKING_GODS, model.getDisplayBookList().get(1));

        // matches 2 books: WAKING_GODS, CONSIDER_PHLEBAS
        descriptor = new FilterDescriptorBuilder()
                .withRatingFilter(new Rating(-1)).withStatusFilter(Status.UNREAD).build();
        assertExecutionSuccess(descriptor, SortMode.TITLED.getComparator(), 2);
        assertEquals(WAKING_GODS, model.getDisplayBookList().get(0));
        assertEquals(CONSIDER_PHLEBAS, model.getDisplayBookList().get(1));

        // matches 2 books: ARTEMIS, COLLAPSING_EMPIRE
        descriptor = new FilterDescriptorBuilder().withTitleFilter("em").build();
        assertExecutionSuccess(descriptor, SortMode.RATINGD.getComparator(), 2);
        assertEquals(ARTEMIS, model.getDisplayBookList().get(0));
        assertEquals(COLLAPSING_EMPIRE, model.getDisplayBookList().get(1));

        // matches 0 books
        descriptor = new FilterDescriptorBuilder()
                .withCategoryFilter("space").withStatusFilter(Status.READING).withRatingFilter(new Rating(3)).build();
        assertExecutionSuccess(descriptor, Model.DEFAULT_BOOK_COMPARATOR, 0);
    }

    @Test
    public void equals() {
        FilterDescriptor descriptorA = new FilterDescriptorBuilder()
                .withAuthorFilter("a1").withStatusFilter(Status.UNREAD).build();
        FilterDescriptor descriptorB = new FilterDescriptorBuilder()
                .withTitleFilter("t1").withAuthorFilter("a2").withPriorityFilter(Priority.HIGH).build();
        ListCommand standardCommand = prepareCommand(descriptorA, SortMode.STATUS.getComparator());

        // same values -> returns true
        FilterDescriptor sameDescriptor = new FilterDescriptorBuilder()
                .withAuthorFilter("a1").withStatusFilter(Status.UNREAD).build();
        ListCommand commandWithSameValues = prepareCommand(sameDescriptor, SortMode.STATUS.getComparator());
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different filter descriptor -> returns false
        assertFalse(standardCommand.equals(new ListCommand(descriptorB, SortMode.STATUS.getComparator())));

        // different comparator -> returns false
        assertFalse(standardCommand.equals(new ListCommand(descriptorA, SortMode.RATING.getComparator())));
    }

    /**
     * Executes a {@code ListCommand} with the given {@code descriptor} and {@code comparator}, and checks that
     * the resulting display book list contains the expected number of books.
     */
    private void assertExecutionSuccess(FilterDescriptor descriptor, Comparator<Book> comparator, int expectedBooks) {
        ListCommand command = prepareCommand(descriptor, comparator);
        CommandResult result = command.execute();
        assertEquals(expectedBooks, model.getDisplayBookList().size());
        assertEquals(String.format(ListCommand.MESSAGE_SUCCESS, expectedBooks), result.feedbackToUser);
    }

    private ListCommand prepareCommand(FilterDescriptor descriptor, Comparator<Book> comparator) {
        ListCommand listCommand = new ListCommand(descriptor, comparator);
        listCommand.setData(model, mock(NetworkManager.class), new CommandHistory(), new UndoStack());
        return listCommand;
    }
}
