package seedu.address.model.book;

import static java.util.Objects.requireNonNull;

/**
 * Represents a book's priority or importance.
 */
public enum Priority {
    NONE("\uD83D\uDEA9 None", "priority-none", "n"),
    LOW("\uD83D\uDEA9 Low", "priority-low", "l"),
    MEDIUM("\uD83D\uDEA9 Medium", "priority-medium", "m"),
    HIGH("\uD83D\uDEA9 High", "priority-high", "h");

    public static final Priority DEFAULT_PRIORITY = NONE;

    private final String displayText;
    private final String styleClass;
    private final String alias;

    Priority(String displayText, String styleClass, String alias) {
        this.displayText = displayText;
        this.styleClass = styleClass;
        this.alias = alias;
    }

    public String getDisplayText() {
        return displayText;
    }

    public String getStyleClass() {
        return styleClass;
    }

    /**
     * Returns the {@code Priority} with a name or alias that matches the specified {@code searchTerm}.
     * Returns {@code null} if no match was found.
     */
    public static Priority findPriority(String searchTerm) {
        requireNonNull(searchTerm);

        for (Priority priority : values()) {
            if (searchTerm.equalsIgnoreCase(priority.alias) || searchTerm.equalsIgnoreCase(priority.toString())) {
                return priority;
            }
        }

        return null;
    }

}
