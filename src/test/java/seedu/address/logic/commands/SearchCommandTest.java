package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalBooks.getTypicalBookShelf;

import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoStack;
import seedu.address.logic.commands.SearchCommand.SearchDescriptor;
import seedu.address.model.BookShelf;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.network.NetworkManager;
import seedu.address.testutil.SearchDescriptorBuilder;

//@@author takuyakanbr
/**
 * Contains integration tests (interaction with the Model) and unit tests for SearchCommand.
 */
public class SearchCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalBookShelf(), new UserPrefs());
    }

    @Test
    public void constructor_nullDescriptor_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new SearchCommand(null);
    }

    @Test
    public void execute_allFieldsSpecifiedWithSearchTerm_success() {
        SearchDescriptor searchDescriptor = new SearchDescriptorBuilder().withTitle("1")
                .withCategory("1").withIsbn("1").withAuthor("1").withSearchTerm("searchterm").build();
        assertExecutionSuccess(searchDescriptor);
    }

    @Test
    public void execute_allFieldsSpecifiedNoSearchTerm_success() {
        SearchDescriptor searchDescriptor = new SearchDescriptorBuilder().withTitle("1")
                .withCategory("1").withIsbn("1").withAuthor("1").build();
        assertExecutionSuccess(searchDescriptor);
    }

    @Test
    public void execute_someFieldsSpecifiedNoSearchTerm_success() {
        SearchDescriptor searchDescriptor = new SearchDescriptorBuilder().withTitle("1").withIsbn("1").build();
        assertExecutionSuccess(searchDescriptor);
    }

    @Test
    public void execute_noFieldSpecifiedNoSearchTerm_success() {
        SearchDescriptor searchDescriptor = new SearchDescriptorBuilder().build();
        assertExecutionSuccess(searchDescriptor);
    }

    @Test
    public void equals() {
        SearchDescriptor descriptorA =
                new SearchDescriptorBuilder().withAuthor("author1").withIsbn("12345").build();
        SearchDescriptor descriptorB =
                new SearchDescriptorBuilder().withAuthor("author2").withIsbn("12345").withTitle("title2").build();
        final SearchCommand standardCommand = prepareCommand(descriptorA);

        // same values -> returns true
        SearchDescriptorBuilder copyDescriptor = new SearchDescriptorBuilder(descriptorA);
        SearchCommand commandWithSameValues = prepareCommand(copyDescriptor.build());
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new SearchCommand(descriptorB)));
    }

    /**
     * Executes a {@code SearchCommand} with the given {@code descriptor}, and checks that
     * {@code network.searchBooks(params)} is being called with the correct search parameters.
     */
    private void assertExecutionSuccess(SearchDescriptor descriptor) {
        SearchCommand searchCommand = new SearchCommand(descriptor, false);

        NetworkManager networkManagerMock = mock(NetworkManager.class);
        when(networkManagerMock.searchBooks(descriptor.toSearchString()))
                .thenReturn(CompletableFuture.completedFuture(new BookShelf()));

        searchCommand.setData(model, networkManagerMock, new CommandHistory(), new UndoStack());
        Model expectedModel = new ModelManager(new BookShelf(model.getBookShelf()), new UserPrefs());
        assertCommandSuccess(searchCommand, model, SearchCommand.MESSAGE_SEARCHING, expectedModel);

        verify(networkManagerMock).searchBooks(descriptor.toSearchString());
    }

    private SearchCommand prepareCommand(SearchDescriptor descriptor) {
        SearchCommand searchCommand = new SearchCommand(descriptor, false);
        searchCommand.setData(model, mock(NetworkManager.class), new CommandHistory(), new UndoStack());
        return searchCommand;
    }
}
