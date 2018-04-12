package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

//@@author A0155428B
/**
 * An event requesting to change theme.
 */
public class ChangeThemeRequestEvent extends BaseEvent {

    public final String theme;

    public ChangeThemeRequestEvent (String theme) {
        this.theme = theme;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
