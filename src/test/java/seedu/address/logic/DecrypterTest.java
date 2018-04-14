package seedu.address.logic;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by davidten on 4/15/18.
 */
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
