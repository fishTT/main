package seedu.address.model.book;

import static java.util.Objects.requireNonNull;

/**
 * Represents a book's publication date.
 * Guarantees: immutable.
 */
public class PublicationDate {

    public final String date;

    /**
     * Constructs a {@code Description}.
     *
     * @param date A book's publication date.
     */
    public PublicationDate(String date) {
        requireNonNull(date);
        this.date = date;
    }

    @Override
    public String toString() {
        return date;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PublicationDate // instanceof handles nulls
                && this.date.equals(((PublicationDate) other).date)); // state check
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }
}
