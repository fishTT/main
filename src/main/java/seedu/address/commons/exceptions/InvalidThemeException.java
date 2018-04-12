package seedu.address.commons.exceptions;

/**
 * Represents an error due to an attempt to use an invalid application theme.
 */
public class InvalidThemeException extends IllegalValueException {

    public InvalidThemeException(String message) {
        super(message);
    }
}
