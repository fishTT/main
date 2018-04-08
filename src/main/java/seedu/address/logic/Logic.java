package seedu.address.logic;

import javafx.collections.ObservableList;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.alias.Alias;
import seedu.address.model.book.Book;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /** Returns an unmodifiable view of the filtered and sorted list of books */
    ObservableList<Book> getDisplayBookList();

    /** Returns an unmodifiable view of the search results list. */
    ObservableList<Book> getSearchResultsList();

    /** Returns an unmodifiable view of the recently selected books list. */
    ObservableList<Book> getRecentBooksList();

    /** Returns an unmodifiable view of the list of aliases. */
    ObservableList<Alias> getDisplayAliasList();

    /** Returns the list of input entered by the user, encapsulated in a {@code ListElementPointer} object */
    ListElementPointer getHistorySnapshot();
}
