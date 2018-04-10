package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AUTHOR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT_BY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.SwitchToBookListRequestEvent;
import seedu.address.model.book.Book;
import seedu.address.model.book.Priority;
import seedu.address.model.book.Rating;
import seedu.address.model.book.Status;

//@@author takuyakanbr
/**
 * Lists all books in the book shelf to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Shows a listing of your books, with optional filtering and sorting.\n"
            + "Parameters: "
            + "[" + PREFIX_TITLE + "TITLE] "
            + "[" + PREFIX_AUTHOR + "AUTHOR] "
            + "[" + PREFIX_CATEGORY + "CATEGORY]"
            + "[" + PREFIX_STATUS + "STATUS]"
            + "[" + PREFIX_PRIORITY + "PRIORITY]"
            + "[" + PREFIX_RATING + "RATING]"
            + "[" + PREFIX_SORT_BY + "SORT_BY]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_STATUS + " reading "
            + PREFIX_SORT_BY + "priority";

    public static final String MESSAGE_SUCCESS = "Listed %s books.";

    private final FilterDescriptor filterDescriptor;
    private final Comparator<Book> bookComparator;

    /**
     * Creates a ListCommand to show a listing of books.
     *
     * @param filterDescriptor the filters used for filtering the books to be displayed.
     * @param bookComparator the comparator used for sorting the books to be displayed.
     */
    public ListCommand(FilterDescriptor filterDescriptor, Comparator<Book> bookComparator) {
        requireAllNonNull(filterDescriptor, bookComparator);
        this.filterDescriptor = filterDescriptor;
        this.bookComparator = bookComparator;
    }

    @Override
    public CommandResult execute() {
        EventsCenter.getInstance().post(new SwitchToBookListRequestEvent());
        model.updateBookListFilter(filterDescriptor.buildCombinedFilter());
        model.updateBookListSorter(bookComparator);
        return new CommandResult(String.format(MESSAGE_SUCCESS, model.getDisplayBookList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ListCommand // instanceof handles nulls
                && this.filterDescriptor.equals(((ListCommand) other).filterDescriptor) // state check
                && this.bookComparator.equals(((ListCommand) other).bookComparator));
    }

    /**
     * Stores the filters used for filtering the books to be displayed.
     */
    public static class FilterDescriptor {
        private final List<Predicate<Book>> filters;
        private Set<String> filterCodes; // used internally for equality testing

        public FilterDescriptor() {
            filters = new LinkedList<>();
            filterCodes = new HashSet<>();
        }

        public FilterDescriptor(FilterDescriptor descriptor) {
            filters = new LinkedList<>(descriptor.filters);
            filterCodes = new HashSet<>(descriptor.filterCodes);
        }

        /** Adds a filter that selects books with titles containing the given {@code title}. */
        public void addTitleFilter(String title) {
            filters.add(book -> book.getTitle().title.toLowerCase().contains(title.toLowerCase()));
            filterCodes.add("t[" + title.toLowerCase() + "]");
        }

        /** Adds a filter that selects books with authors containing the given {@code author}. */
        public void addAuthorFilter(String author) {
            filters.add(book -> book.getAuthors().stream()
                    .anyMatch(bookAuthor -> bookAuthor.fullName.toLowerCase().contains(author.toLowerCase())));
            filterCodes.add("a[" + author.toLowerCase() + "]");
        }

        /** Adds a filter that selects books with categories containing the given {@code category}. */
        public void addCategoryFilter(String category) {
            filters.add(book -> book.getCategories().stream()
                    .anyMatch(bookCategory -> bookCategory.category.toLowerCase().contains(category.toLowerCase())));
            filterCodes.add("c[" + category.toLowerCase() + "]");
        }

        /** Adds a filter that selects books with status matching the given {@code status}. */
        public void addStatusFilter(Status status) {
            filters.add(book -> status.equals(book.getStatus()));
            filterCodes.add("s[" + status + "]");
        }

        /** Adds a filter that selects books with priority matching the given {@code priority}. */
        public void addPriorityFilter(Priority priority) {
            filters.add(book -> priority.equals(book.getPriority()));
            filterCodes.add("p[" + priority + "]");
        }

        /** Adds a filter that selects books with ratings matching the given {@code rating}. */
        public void addRatingFilter(Rating rating) {
            filters.add(book -> rating.equals(book.getRating()));
            filterCodes.add("r[" + rating + "]");
        }

        protected Predicate<Book> buildCombinedFilter() {
            List<Predicate<Book>> partialFilters = new LinkedList<>(filters);
            return book -> partialFilters.stream().allMatch(filter -> filter.test(book));
        }

        @Override
        public boolean equals(Object other) {
            return other == this // short circuit if same object
                    || (other instanceof FilterDescriptor // instanceof handles nulls
                    && this.filterCodes.equals(((FilterDescriptor) other).filterCodes)); // state check
        }
    }
}
