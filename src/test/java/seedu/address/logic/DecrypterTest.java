//@@author davidten
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
