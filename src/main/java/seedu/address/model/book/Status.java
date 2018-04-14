package seedu.address.model.book;

import static java.util.Objects.requireNonNull;

/**
 * Represents a book's status (whether it is read, being read, or unread).
 */
public enum Status {
    READ("\uD83D\uDCD6 Read", "status-read", "r"),
    UNREAD("\uD83D\uDCD7 Unread", "status-unread", "u"),
    READING("\uD83D\uDCD6 Reading", "status-reading", "rd");

    public static final Status DEFAULT_STATUS = UNREAD;

    private final String displayText;
    private final String styleClass;
    private final String alias;

    Status(String displayText, String styleClass, String alias) {
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
     * Returns the {@code Status} with a name or alias that matches the specified {@code searchTerm}.
     * Returns {@code null} if no match was found.
     */
    public static Status findStatus(String searchTerm) {
        requireNonNull(searchTerm);

        for (Status status : values()) {
            if (searchTerm.equalsIgnoreCase(status.alias) || searchTerm.equalsIgnoreCase(status.toString())) {
                return status;
            }
        }

        return null;
    }

}
