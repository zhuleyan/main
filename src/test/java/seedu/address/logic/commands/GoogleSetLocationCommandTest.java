//@@author davidten
package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.Config;
import seedu.address.model.person.Address;



public class GoogleSetLocationCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private GoogleSetLocationCommand prepareCommand(Address address) {
        GoogleSetLocationCommand command = new GoogleSetLocationCommand(address);
        return command;
    }

    @Test
    public void constructor_nullAddress_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        prepareCommand(null);
    }

    @Test
    public void testGoogleExecuteCommand() throws IOException {
        Config.clearUserLocation();
        Config preConfig = Config.setupConfig();
        assertNull(preConfig.getUserLocation());

        String addressString = "6 College Avenue East, Singapore 138614";
        Address myAddress = new Address(addressString);

        GoogleSetLocationCommand command = prepareCommand(myAddress);
        CommandResult cr = command.execute();
        assertEquals(cr.feedbackToUser, GoogleSetLocationCommand.MESSAGE_SUCCESS);

        Config postConfig = Config.setupConfig();
        assertEquals(addressString, postConfig.getUserLocation());
        Config.clearUserLocation();
    }



}
