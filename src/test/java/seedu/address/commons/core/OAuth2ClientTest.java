//@@author davidten
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
