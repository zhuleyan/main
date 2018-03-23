package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * Event to respond to a request to hide browser
 */
public class HideBrowserRequestEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
