package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;

/** Indicates that the application was unlocked. */
public class AppUnlockedEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
