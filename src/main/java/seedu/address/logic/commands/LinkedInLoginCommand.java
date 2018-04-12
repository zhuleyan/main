//@@author davidten
package seedu.address.logic.commands;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ShowBrowserRequestEvent;

/**
 * Allows a user to login to their LinkedIn account
 */
public class LinkedInLoginCommand extends Command {
    public static final String COMMAND_WORD = "linkedin_login";
    //@@author Sheikh-Umar
    public static final String COMMAND_ALIAS = "linklog";
    //@@author davidten
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Logs in to your LinkedIn account ";

    public static final String MESSAGE_SUCCESS = "Browser Opened for LinkedIn Authentication";

    /**
     * Default constructor
     */
    public LinkedInLoginCommand(){

    }

    @Override
    public CommandResult execute() {
        EventsCenter.getInstance().post(new ShowBrowserRequestEvent());
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
