//@@author davidten
package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * Event to respond to request to share to LinkedIn
 */
public class ShareToLinkedInEvent extends BaseEvent {
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
