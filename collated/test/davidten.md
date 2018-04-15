# davidten
###### /java/seedu/address/commons/core/ConfigTest.java
``` java
                + "App Id: 78ameftoz7yvk4\n"
                + "App Secret: null\n"
                + "User Location: null";
```
###### /java/seedu/address/commons/core/OAuth2ClientTest.java
``` java
package seedu.address.commons.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


/**
 * Created to test some methods in OAuth2ClientTest
 * This class is better tested manually, as it depends heavily on a valid
 * access token which is dependent on a valid LinkedIn Account
 */
public class OAuth2ClientTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testStartServer() {
        assertFalse(serverListening("127.0.0.1", 13370));
        Oauth2Client.startServer();
        assertTrue(serverListening("127.0.0.1", 13370));
    }

    @Test
    public void testGetParams() {
        //just to check that we get the correct number of values.
        List<NameValuePair> testList = Oauth2Client.getParams();
        assertEquals(testList.size(), 5);
    }

    @Test
    public void testHttpEntity() throws IOException {
        HttpEntity testHttpEntity = Oauth2Client.getHttpEntity();
        assertNotNull(testHttpEntity);
    }

    /**
     * This method is used to check if there is a server listening to the given port and host.
     */
    public static boolean serverListening(String host, int port)  {
        Socket s;
        try {
            s = new Socket(host, port);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
```
###### /java/seedu/address/logic/DecrypterTest.java
``` java
package seedu.address.logic;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DecrypterTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testStartServer() throws NoSuchPaddingException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Decrypter d = new Decrypter();
        String secret = d.getLinkedInS("nvu3QZLMqueiNkyaaOJQmz7Bzrk+Fk+P", "qI8aUtN6zZI=");
        assertNotNull(secret);
        assertNotEquals(secret, "nvu3QZLMqueiNkyaaOJQmz7Bzrk+Fk+P");
    }
}
```
###### /java/seedu/address/logic/parser/GoogleSetLocationCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.Test;

import seedu.address.commons.core.Config;
import seedu.address.logic.commands.GoogleSetLocationCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;

public class GoogleSetLocationCommandParserTest {
    private String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            GoogleSetLocationCommand.MESSAGE_USAGE);
    private GoogleSetLocationCommandParser parser = new GoogleSetLocationCommandParser();

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        //missing address prefix
        assertParseFailure(parser, VALID_ADDRESS_AMY, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        //tests invalid addressValue
        assertParseFailure(parser, INVALID_ADDRESS_DESC, Address.MESSAGE_ADDRESS_CONSTRAINTS);
    }

    @Test
    public void parseSuccess() {
        //We cannot use the traditional assertParseSuccess because the address is created in the parser, and
        //even with the same input it will return a different command object.
        //Therefore, we check with the config file instead.

        Config.clearUserLocation();
        Config preConfig = Config.setupConfig();
        assertNull(preConfig.getUserLocation());

        GoogleSetLocationCommand command = null;
        try {
            command = parser.parse(ADDRESS_DESC_AMY);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertNotNull(command);
        command.execute();

        Config postConfig = Config.setupConfig();
        assertEquals(VALID_ADDRESS_AMY, postConfig.getUserLocation());
        Config.clearUserLocation();
    }
}
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
###### /java/seedu/address/logic/commands/GoogleSetLocationCommandTest.java
``` java
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
```
