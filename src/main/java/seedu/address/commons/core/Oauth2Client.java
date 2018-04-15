//@@author davidten
package seedu.address.commons.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javafx.application.Platform;
import seedu.address.commons.events.ui.HideBrowserRequestEvent;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.commons.util.ConfigUtil;
import seedu.address.logic.Decrypter;
import seedu.address.ui.BrowserWindow;


/**
 * Class that handles the OAuth2 call with LiknedIn
 * Acts as the client in the client-server scheme
 */
public class Oauth2Client {
    private static final String linkedInAccessTokenURL = "https://www.linkedin.com/oauth/v2/accessToken";
    private static final String redirectUri = "http://127.0.0.1:13370/test";
    private static final Logger logger = LogsCenter.getLogger(Oauth2Client.class);
    private static BrowserWindow bWindow;
    private static String secret;
    private static String authorizationCode;
    private static String clientId;
    private static Config config;
    /**
     * Called when user types Linkedin_login
     * starts a webserver and opens a browser for Linkedin Authorization
     */
    public static void authenticateWithLinkedIn() throws IOException {
        config = Config.setupConfig();
        startServer();

        clientId = config.getAppId();

        String urlString = "https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id="
            + clientId + "&redirect_uri=" + redirectUri + "&state=123";

        String fxmlString = "LinkedInLoginWindow.fxml";
        bWindow = new BrowserWindow(urlString, fxmlString);
        logger.info("Showing browserWindow for logging in to LinkedIn");
        bWindow.show();
    }

    /**
     * Called to save whatever config we write into the config file
     */
    public static void saveConfig() {
        try {
            ConfigUtil.saveConfig(config, config.DEFAULT_CONFIG_FILE);
            logger.info("Configuration saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts a webserver and allows it to expect a response at the context specified
     */
    public static void startServer() {
        try {
            int port = 13370;
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/test", new MyHandler());
            server.setExecutor(null);
            server.start();
            logger.info("Server started at port 13370, listening for /test");
        } catch (IOException e) {
            logger.info("Server likely to have been started already " + e.toString());
        }
    }

    /**
     * Closes a browser window.
     * Platform.runLater method is needed to avoid 'Not on FX application thread' error
     */
    public static void closeBrowser() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                bWindow.hide();
                logger.info("Browser Closed");
            }
        });
    }

    public static void getLinkedInS() {
        String encryptedByteCipher = "nvu3QZLMqueiNkyaaOJQmz7Bzrk+Fk+P";
        String encryptedKey = "qI8aUtN6zZI=";

        Decrypter a = new Decrypter();
        try {
            secret = a.getLinkedInS(encryptedByteCipher, encryptedKey);
            logger.info("Secret obtained");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        try {
            getAccessToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method creates and returns the parameters used in httpEntity
     */
    public static List<NameValuePair> getParams() {
        List<NameValuePair> params = new ArrayList<NameValuePair>(5);
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));
        params.add(new BasicNameValuePair("code", authorizationCode));
        params.add(new BasicNameValuePair("redirect_uri", redirectUri));
        params.add(new BasicNameValuePair("client_id", clientId));
        params.add(new BasicNameValuePair("client_secret", secret));
        return params;
    }

    /**
     * This method creates and returns the httpEntity object used for requesting to LinkedIn
     */
    public static HttpEntity getHttpEntity() throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(linkedInAccessTokenURL);

        List<NameValuePair> params = getParams();
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        HttpResponse response = httpclient.execute(httppost);

        return response.getEntity();
    }

    /**
     * This method reads the input stream to get the accessToken for the user
     */
    public static String extractAccessTokenFromResponse(InputStream instream)
            throws IOException {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }

        JSONObject jsonObj = new JSONObject(responseStrBuilder.toString());
        return jsonObj.getString("access_token");
    }

    /**
     * This method exchanges the authorization token for an accessToken
     */
    public static void getAccessToken() throws IOException {
        HttpEntity entity = getHttpEntity();

        if (entity != null) {
            InputStream instream = entity.getContent();
            try {
                String accessToken = extractAccessTokenFromResponse(instream);

                logger.info("Access Token is " + accessToken);
                EventsCenter.getInstance().post(new NewResultAvailableEvent("Successfully logged in to LinkedIn"));
                config.setAppSecret(accessToken);
                saveConfig();

                //Login Successful
            } finally {
                instream.close();
            }
        }
    }

    /**
     * Allows the server to handle responses made to the context created in startServer
     */
    static class MyHandler implements HttpHandler {
        private final Logger logger = LogsCenter.getLogger(Oauth2Client.class);

        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "This is the response";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            logger.info("RECEIVED A RESPONSE FROM THE SERVER: " + t.getRequestURI().getQuery());

            String authorizationCodeandState = t.getRequestURI().getQuery();
            authorizationCode = authorizationCodeandState.substring(5, authorizationCodeandState.length() - 10);
            logger.info("Auth code is: " + authorizationCode);

            EventsCenter.getInstance().post(new HideBrowserRequestEvent());

        }
    }

}
