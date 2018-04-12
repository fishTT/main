package seedu.address.model.book;

import static java.util.Objects.requireNonNull;

/**
 * Represents a single book author.
 * Guarantees: immutable.
 */
public class Author {
    private static final String DISPLAY_PREFIX = "\uD83D\uDC64 ";

    public final String fullName;

    /**
     * Constructs a {@code Author}.
     *
     * @param fullName An author name.
     */
    public Author(String fullName) {
        requireNonNull(fullName);
        this.fullName = fullName;
    }

    public String getDisplayText() {
        return DISPLAY_PREFIX + fullName;
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Author // instanceof handles nulls
                && this.fullName.equals(((Author) other).fullName)); // state check
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
