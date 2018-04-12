package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * An event requesting to view the result showing the availability of some book in the library.
 */
public class ShowLibraryResultRequestEvent extends BaseEvent {

    private final String result;

    public ShowLibraryResultRequestEvent(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public String getResult() {
        return result;
    }
}
