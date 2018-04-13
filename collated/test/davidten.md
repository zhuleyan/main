# davidten
###### /java/seedu/address/commons/core/ConfigTest.java
``` java
                + "App Id: 78ameftoz7yvk4\n"
                + "App Secret: null\n"
                + "User Location: null";
```
###### /java/seedu/address/logic/commands/LinkedInLoginCommandTest.java
``` java
package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.LinkedInLoginCommand.MESSAGE_SUCCESS;

import org.junit.Rule;
import org.junit.Test;

import seedu.address.commons.events.ui.ShowBrowserRequestEvent;
import seedu.address.ui.testutil.EventsCollectorRule;

public class LinkedInLoginCommandTest {

    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    @Test
    public void execute_help_success() {
        CommandResult result = new LinkedInLoginCommand().execute();
        assertEquals(MESSAGE_SUCCESS, result.feedbackToUser);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof ShowBrowserRequestEvent);
        assertTrue(eventsCollectorRule.eventsCollector.getSize() == 1);
    }

}
```
