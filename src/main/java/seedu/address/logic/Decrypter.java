//@@author davidten
package seedu.address.logic;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import seedu.address.commons.core.LogsCenter;



/**
 * Decrypts the s for use for OAuth
 * While this is not an ideal situation, LinkedIn's OAuth API does not have a client-side authentication flow.
 * This means that it will always require the app s for purposes of authentication.
 * Because of this, building a native (desktop) app that authenticates with LinkedIn is not ideal.
 *
 * However, a number of sites have agreed that if you have to store the key in the code, then obscuring it to make
 * it slightly more difficult for a potential hacker to get it is best. (They will need to run the app rather than
 * just reading the plain text version)
 *
 * This is especially so because a LinkedIn S is not especially valuable, since anyone can create a LinkedIn app.
 *
 * Furthermore the chances of competitors abusing the secret to disable this application is minimal, since it is
 * ultimately, a school project.
 *
 */
public class Decrypter {

    private final Logger logger = LogsCenter.getLogger(Decrypter.class);

    public String getLinkedInS(String encryptedByteCipher, String encryptedKey) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException,
            InvalidAlgorithmParameterException {

        String strDecryptedText = new String();

        //Secret key generation
        byte[] decodedKey = Base64.getDecoder().decode(encryptedKey);
        SecretKey sKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES");
        logger.info("Secret key for decryption is " + Base64.getEncoder().encodeToString(sKey.getEncoded()));

        //Create a Cipher
        Cipher desCipher = Cipher.getInstance("DES");

        //Decrypt the data
        byte[] byteCipherText = Base64.getDecoder().decode(encryptedByteCipher);
        desCipher.init(Cipher.DECRYPT_MODE, sKey, desCipher.getParameters());
        byte[] byteDecryptedText = desCipher.doFinal(byteCipherText);
        strDecryptedText = new String(byteDecryptedText);
        logger.info("Decrypted Text message is " + strDecryptedText);
        return strDecryptedText;
    }

}
