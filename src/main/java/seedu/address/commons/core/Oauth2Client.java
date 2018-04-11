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
    private static BrowserWindow bWindow;
    private static String secret;
    private static String authorizationCode;
    private static String redirectUri = "http://127.0.0.1:13370/test";
    private static String clientId;
    private static Logger logger = LogsCenter.getLogger(Oauth2Client.class);
    private static Config config;
    /**
     * Called when user types Linkedin_login
     * starts a webserver and opens a browser for Linkedin Authorization
     */
    public static void authenticateWithLinkedIn(Config configuration) throws IOException {
        config = configuration;
        startServer();

        clientId = config.getAppId();

        String urlString = "https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id="
            + clientId + "&redirect_uri=" + redirectUri + "&state=123";

        String fxmlString = "LinkedInLoginWindow.fxml";
        bWindow = new BrowserWindow(urlString, fxmlString);
        bWindow.show();

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
            }
        });
    }

    public static void getLinkedInS() {
        Decrypter a = new Decrypter();
        try {
            secret = a.getLinkedInS();
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
     * This method exchanges the authorization token for an accessToken
     */
    public static void getAccessToken() throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("https://www.linkedin.com/oauth/v2/accessToken");

        List<NameValuePair> params = new ArrayList<NameValuePair>(5);
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));
        params.add(new BasicNameValuePair("code", authorizationCode));
        params.add(new BasicNameValuePair("redirect_uri", redirectUri));
        params.add(new BasicNameValuePair("client_id", clientId));
        params.add(new BasicNameValuePair("client_secret", secret));
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            InputStream instream = entity.getContent();
            try {
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;
                while ((inputStr = streamReader.readLine()) != null) {
                    responseStrBuilder.append(inputStr);
                }


                JSONObject jsonObj = new JSONObject(responseStrBuilder.toString());

                String accessToken = jsonObj.getString("access_token");

                logger.info("Login to LinkedIn Successful" + responseStrBuilder.toString());
                logger.info("Access Token is " + accessToken);
                EventsCenter.getInstance().post(new NewResultAvailableEvent("Successfully logged in to LinkedIn"));
                config.setAppSecret(accessToken);
                ConfigUtil.saveConfig(config, config.DEFAULT_CONFIG_FILE);

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
            //t.getRequestURI().getQuery() receives the response from the server. Need to parse it
            EventsCenter.getInstance().post(new HideBrowserRequestEvent());

        }
    }

}
