package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;

import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.DeselectBookRequestEvent;
import seedu.address.commons.events.ui.ReselectBookRequestEvent;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.CommandUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ActiveListType;
import seedu.address.model.book.Book;
import seedu.address.model.book.Priority;
import seedu.address.model.book.Rating;
import seedu.address.model.book.Status;
import seedu.address.model.book.exceptions.BookNotFoundException;
import seedu.address.model.book.exceptions.DuplicateBookException;

//@@author 592363789
/**
 * Edits the status, priority, and rating of an existing book.
 */
public class EditCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the rating, status, and priority"
            + " of the book identified by the index number.\n"
            + "Parameters: INDEX [s/STATUS] [p/PRIORITY] [r/RATING] "
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_RATING + "-1 " + PREFIX_PRIORITY + "low " + PREFIX_STATUS + "unread";

    public static final String MESSAGE_SUCCESS = "Edited Book: %1$s";
    public static final String MESSAGE_NO_PARAMETERS = "At least one field to edit must be provided.";
    public static final String MESSAGE_WRONG_ACTIVE_LIST = "Items from the current list cannot be edited.";

    public static final String UNDO_SUCCESS = "Successfully undone editing of %s.";
    public static final String UNDO_FAILURE = "Failed to undo editing of %s.";

    private final Index index;
    private final EditDescriptor editDescriptor;

    private Book bookToEdit;
    private Book editedBook;

    /**
     * @param index of the book in the filtered book list to edit the rating.
     * @param editDescriptor details to edit the book with.
     */
    public EditCommand(Index index, EditDescriptor editDescriptor) {
        requireAllNonNull(index, editDescriptor);

        this.index = index;
        this.editDescriptor = editDescriptor;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireAllNonNull(bookToEdit, editedBook);

        try {
            EventsCenter.getInstance().post(new DeselectBookRequestEvent());
            model.updateBook(bookToEdit, editedBook);
            EventsCenter.getInstance().post(new ReselectBookRequestEvent());
        } catch (DuplicateBookException dpe) {
            throw new AssertionError("Editing target book should not result in a duplicate");
        } catch (BookNotFoundException pnfe) {
            throw new AssertionError("The target book should not be missing");
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, editedBook));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        requireNonNull(model);

        checkActiveListType();
        CommandUtil.checkValidIndex(model, index);

        bookToEdit = CommandUtil.getBook(model, index);
        editedBook = createEditedBook(bookToEdit, editDescriptor);
    }

    /**
     * Throws a {@link CommandException} if the active list type is not supported by this command.
     */
    private void checkActiveListType() throws CommandException {
        if (model.getActiveListType() != ActiveListType.BOOK_SHELF) {
            throw new CommandException(MESSAGE_WRONG_ACTIVE_LIST);
        }
    }

    //@@author
    /**
     * Creates and returns a {@code Book} with the details of {@code bookToEdit}
     * edited with {@code editDescriptor}.
     */
    private static Book createEditedBook(Book bookToEdit, EditDescriptor editDescriptor) {
        requireAllNonNull(bookToEdit, editDescriptor);

        Status updatedStatus = editDescriptor.getStatus().orElse(bookToEdit.getStatus());
        Priority updatedPriority = editDescriptor.getPriority().orElse(bookToEdit.getPriority());
        Rating updatedRating = editDescriptor.getRating().orElse(bookToEdit.getRating());

        return new Book(bookToEdit.getGid(), bookToEdit.getIsbn(), bookToEdit.getAuthors(),
                bookToEdit.getTitle(), bookToEdit.getCategories(), bookToEdit.getDescription(),
                updatedStatus, updatedPriority, updatedRating,
                bookToEdit.getPublisher(), bookToEdit.getPublicationDate());
    }

    @Override
    protected String undo() {
        requireAllNonNull(model, editedBook, bookToEdit);

        try {
            model.updateBook(editedBook, bookToEdit);
            return String.format(UNDO_SUCCESS, editedBook);
        } catch (DuplicateBookException | BookNotFoundException e) {
            // Should never end up here
            return String.format(UNDO_FAILURE, editedBook);
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editDescriptor.equals(e.editDescriptor)
                && Objects.equals(bookToEdit, e.bookToEdit);
    }
    //@@author 592363789
    /**
     * Stores the details to edit the book with. Each non-empty field value will replace the
     * corresponding field value of the book.
     */
    public static class EditDescriptor {
        private Status status;
        private Priority priority;
        private Rating rating;

        public EditDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditDescriptor(EditDescriptor toCopy) {
            setStatus(toCopy.status);
            setPriority(toCopy.priority);
            setRating(toCopy.rating);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isValid() {
            return CollectionUtil.isAnyNonNull(this.status, this.priority, this.rating);
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public Optional<Status> getStatus() {
            return Optional.ofNullable(status);
        }

        public void setPriority(Priority priority) {
            this.priority = priority;
        }

        public Optional<Priority> getPriority() {
            return Optional.ofNullable(priority);
        }

        public void setRating(Rating rating) {
            this.rating = rating;
        }

        public Optional<Rating> getRating() {
            return Optional.ofNullable(rating);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditDescriptor)) {
                return false;
            }

            // state check
            EditDescriptor e = (EditDescriptor) other;

            return getStatus().equals(e.getStatus())
                    && getPriority().equals(e.getPriority())
                    && getRating().equals(e.getRating());
        }
    }

}
