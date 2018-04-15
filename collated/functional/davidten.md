# davidten
###### /java/seedu/address/ui/BrowserWindow.java
``` java
//reused
package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Opens a new browser window, HelpWindow style
 */
public class BrowserWindow extends UiPart<Stage> {

    private static final Logger logger = LogsCenter.getLogger(BrowserWindow.class);
    private static String FXML = "HelpWindow.fxml";

    @FXML
    private WebView browser;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public BrowserWindow(Stage root, String url) {
        super(FXML, root);
        logger.info("Starting a web page at URL: " + url);
        browser.getEngine().load(url);
        logger.info("Loading a web page");

    }

    /**
     * Creates a new BrowserWindow with specified fxml.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public BrowserWindow(Stage root, String url, String fxml) {
        super(fxml, root);
        logger.info("Starting a web page with fxml: " + fxml);
        logger.info("Starting a web page at URL: " + url);
        browser.getEngine().load(url);
        logger.info("Loading a web page");
    }

    /**
     * Creates a new BrowserWindow.
     */
    public BrowserWindow(String url) {
        this(new Stage(), url);
    }

    /**
     * Creates a new BrowserWindow.
     */
    public BrowserWindow() {
        this(new Stage(), "");
    }

    /**
     * Creates a new BrowserWindow.
     */
    public BrowserWindow(String url, String fxml) {
        this(new Stage(), url, fxml);
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     * If one of the following happens:
     * 1) This method is called on a thread other than the JavaFX Application Thread.
     * 2) This method is called during animation or layout processing.
     * 3) This method is called on the primary stage.
     * 4) if {@code dialogStage} is already showing
     */
    public void show() {
        logger.info("Opening a browser for the application.");
        getRoot().show();
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     * If one of the following happens:
     * 1) This method is called on a thread other than the JavaFX Application Thread.
     * 2) This method is called during animation or layout processing.
     * 3) This method is called on the primary stage.
     */
    public void hide() {
        logger.info("Closing a browser for the application.");
        getRoot().close();
    }


}
```
###### /java/seedu/address/ui/BrowserPanel.java
``` java
    /**
     * Gets configuration to be used when showing google maps
     */
    public static void getConfig() {
        config = Config.setupConfig();
    }

    /**
     * Generates the google maps url to be shown in the browser
     */
    public static String generateUrl(String from, String to) {
        String url = "https://www.google.com/maps/dir/?api=1&origin=";
        String encodedUserLocation = "";
        String encodedDestinationLocation = "";
        try {
            encodedUserLocation = URLEncoder.encode(from, "UTF-8");
            encodedDestinationLocation = URLEncoder.encode(to, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        url += encodedUserLocation + "&destination=" + encodedDestinationLocation;

        return url;
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        getConfig();
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        //if person has no home location set
        if (config.getUserLocation() == null || config.getUserLocation().length() == 0) {
            logger.info("No office location set, doing Google search");
            loadPersonPage(event.getNewSelection().person);
        } else {
            String url = generateUrl(config.getUserLocation(), event.getNewSelection().person.getAddress().toString());
            logger.info("Office location set, Load Google Maps. URL IS " + url);
            loadPage(url);
        }
    }
}
```
###### /java/seedu/address/ui/MainWindow.java
``` java
    /**
     * Opens the help window.
     */
    @FXML
    public void handleHelp() {
        HelpWindow helpWindow = new HelpWindow();
        helpWindow.show();
    }

    /**
     * Opens the linkedin authentication window.
     */
    public void handleLinkedInAuthentication() {
        try {
            Oauth2Client.authenticateWithLinkedIn();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the linkedin authentication window.
     */
    public void handleHideBrowser() {
        Oauth2Client.closeBrowser();
        Oauth2Client.getLinkedInS();
    }
```
###### /java/seedu/address/ui/MainWindow.java
``` java
    @Subscribe
    private void handleCloseBrowserEvent(HideBrowserRequestEvent event) {
        try {
            logger.info(LogsCenter.getEventHandlingLogMessage(event));
            handleHideBrowser();
        } catch (Exception e) {
            logger.info(e.toString());
            EventsCenter.getInstance().post(new NewResultAvailableEvent("Login Failed."));
        }
    }

    @Subscribe
    private void handleLinkedInAuthenticationEvent(ShowBrowserRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        handleLinkedInAuthentication();
    }

    @Subscribe
    private void handleShowHelpEvent(ShowHelpRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        handleHelp();
    }

```
###### /java/seedu/address/commons/core/Oauth2Client.java
``` java
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
```
###### /java/seedu/address/commons/core/Config.java
``` java
    private String appId = "78ameftoz7yvk4";
    private String appSecret;
    private String userLocation;

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String address) {
        userLocation = address;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAppId() {
        return appId;
    }
```
###### /java/seedu/address/commons/core/Config.java
``` java
        sb.append("\nApp Id: " + appId);
        sb.append("\nApp Secret: " + appSecret);
        sb.append("\nUser Location: " + userLocation);
```
###### /java/seedu/address/commons/core/Config.java
``` java
    //reused
    /**
     * Called to start reading the configuration file so that we get the most updated values
     */
    public static Config setupConfig() {
        Config initializedConfig;
        String configFilePathUsed = Config.DEFAULT_CONFIG_FILE;
        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                    + "Using default config properties");
            initializedConfig = new Config();
        }
        return initializedConfig;
    }

    /**
     * Used by testcases so that Google maps does not interfere with select command
     */
    public static void clearUserLocation() {
        Config preConfig = Config.setupConfig();
        preConfig.setUserLocation(null);
        try {
            ConfigUtil.saveConfig(preConfig, preConfig.DEFAULT_CONFIG_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```
###### /java/seedu/address/commons/events/ui/ShareToLinkedInEvent.java
``` java
package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * Event to respond to request to share to LinkedIn
 */
public class ShareToLinkedInEvent extends BaseEvent {
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### /java/seedu/address/commons/events/ui/ShowBrowserRequestEvent.java
``` java
package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * An event requesting to view the browser.
 */
public class ShowBrowserRequestEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
```
###### /java/seedu/address/commons/events/ui/HideBrowserRequestEvent.java
``` java
package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * Event to respond to a request to hide browser
 */
public class HideBrowserRequestEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
```
###### /java/seedu/address/logic/parser/GoogleSetLocationCommandParser.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;

import java.util.stream.Stream;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.GoogleSetLocationCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;

/**
 * Parses input arguments and creates a new GoogleSetLocationCommand object
 */
public class GoogleSetLocationCommandParser implements Parser<GoogleSetLocationCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the GoogleSetLocationCommand
     * and returns an GoogleSetLocationCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public GoogleSetLocationCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ADDRESS);
        if (!arePrefixesPresent(argMultimap, PREFIX_ADDRESS)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                GoogleSetLocationCommand.MESSAGE_USAGE));
        }

        try {
            Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS)).get();

            return new GoogleSetLocationCommand(address);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }
```
###### /java/seedu/address/logic/parser/ShareToLinkedInCommandParser.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.ShareToLinkedInCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input
 */
public class ShareToLinkedInCommandParser implements Parser<ShareToLinkedInCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ShareToLinkedInCommand
     * and returns an ShareToLinkedInCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ShareToLinkedInCommand parse(String args) throws ParseException {
        if (args == null || args.length() == 0) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ShareToLinkedInCommand.MESSAGE_USAGE));
        }
        Logger logger = LogsCenter.getLogger(ShareToLinkedInCommand.class);
        logger.info("SHARE TO LINKEDIN PARSER RUN" + args);
        return new ShareToLinkedInCommand(args);

    }

}
```
###### /java/seedu/address/logic/parser/AddressBookParser.java
``` java
        case ShareToLinkedInCommand.COMMAND_WORD:
        case ShareToLinkedInCommand.COMMAND_ALIAS:
            return new ShareToLinkedInCommandParser().parse(arguments);

        case LinkedInLoginCommand.COMMAND_WORD:
        case LinkedInLoginCommand.COMMAND_ALIAS:
            return new LinkedInLoginCommand();

        case GoogleSetLocationCommand.COMMAND_WORD:
        case GoogleSetLocationCommand.COMMAND_ALIAS:
            return new GoogleSetLocationCommandParser().parse(arguments);
```
###### /java/seedu/address/logic/parser/ParserUtil.java
``` java
    /**
     * Parses a {@code String data}.
     * Leading and trailing whitespaces will be trimmed.
     * General String parser that can be used
     */
    public static String parseString(String data) {
        requireNonNull(data);
        return data.trim();
    }

```
###### /java/seedu/address/logic/Decrypter.java
``` java
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
```
###### /java/seedu/address/logic/commands/GoogleSetLocationCommand.java
``` java
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;

import java.io.IOException;
import java.util.logging.Logger;

import seedu.address.commons.core.Config;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.ConfigUtil;
import seedu.address.model.person.Address;

/**
 * Allows a user to set their location for Google Maps
 */
public class GoogleSetLocationCommand extends Command {
    public static final String COMMAND_WORD = "set_office_address";
    public static final String COMMAND_ALIAS = "setA";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets your office address for Google Maps "
            + "Parameters: "
            + PREFIX_ADDRESS + "ADDRESS "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ADDRESS + "6 College Avenue East, Singapore 138614";
    public static final String MESSAGE_SUCCESS = "Office address set!";

    private final Address address;

    /**
     * Default constructor
     */
    public GoogleSetLocationCommand(Address address) {
        requireNonNull(address);
        this.address = address;
    }

    @Override
    public CommandResult execute() {
        //should be able to just create a new instance of config since it's the same config.json file
        Logger logger = LogsCenter.getLogger(GoogleSetLocationCommand.class);
        Config initializedConfig = Config.setupConfig();

        initializedConfig.setUserLocation(address.toString());
        try {
            ConfigUtil.saveConfig(initializedConfig, initializedConfig.DEFAULT_CONFIG_FILE);
            logger.info("Successfully saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //sets the office address as a string in the config file.

        return new CommandResult(MESSAGE_SUCCESS);
    }

}
```
###### /java/seedu/address/logic/commands/LinkedInLoginCommand.java
``` java
package seedu.address.logic.commands;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ShowBrowserRequestEvent;

/**
 * Allows a user to login to their LinkedIn account
 */
public class LinkedInLoginCommand extends Command {
    public static final String COMMAND_WORD = "linkedin_login";
```
###### /java/seedu/address/logic/commands/LinkedInLoginCommand.java
``` java
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
```
###### /java/seedu/address/logic/commands/ShareToLinkedInCommand.java
``` java
package seedu.address.logic.commands;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import seedu.address.commons.core.Config;
import seedu.address.commons.core.LogsCenter;

/**
 * Shares a post to the logged in LinkedIn accountx
 */
public class ShareToLinkedInCommand extends Command {
    public static final String COMMAND_WORD = "linkedin_share";
    public static final String COMMAND_ALIAS = "linkshare";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shares a post to your LinkedIn Account. "
            + "With at least one character as the parameter \n"
            + "Example: " + COMMAND_WORD
            + " I think www.google.com is a great search engine";

    public static final String MESSAGE_SUCCESS = "Post shared to your linkedIn account";
    public static final String MESSAGE_FAILURE = "Failed to post to LinkedIn";
    public static final String LINKEDIN_SHARE_API_URL = "https://api.linkedin.com/v1/people/~/shares?format=json";

    private static final Logger logger = LogsCenter.getLogger(ShareToLinkedInCommand.class);
    private static String post;
    private static boolean postSuccess = false;

    /**
     * Default constructor
     */
    public ShareToLinkedInCommand() {

    }

    /**
     * Constructor to accept a post
     */
    public ShareToLinkedInCommand(String post) {
        this.post = post;
    }

    @Override
    public CommandResult execute() {
        postToLinkedIn();
        //post success
        logger.info("CHECKING POST SUCCESS NOW");
        if (postSuccess) {
            return new CommandResult(MESSAGE_SUCCESS);
        } else {
            return new CommandResult(MESSAGE_FAILURE);
        }
    }

    /**
     * This method checks if the supplied accessToken is not empty.
     */
    public static boolean accessTokenValid(String accessToken) {
        if (accessToken == null || accessToken.length() == 0) {
            return false;
        }
        return true;
    }

    /**
     * This method creates a JsonObject that sets the following parameters for the LinkedIn post
     * Visibility: 'anyone' (public)
     * Post: The user input
     * The method then returns this JsonObject as a String
     */
    public static String getLinkedInJsonObject() {
        JSONObject visibilityJsonObj = new JSONObject();
        visibilityJsonObj.put("code", new String("anyone"));

        JSONObject mainJsonObj = new JSONObject();
        mainJsonObj.put("comment", post);
        mainJsonObj.put("visibility", visibilityJsonObj);
        return mainJsonObj.toString();
    }

    /**
     * This method creates a HttpClient object
     */
    public static HttpClient getHttpClientObject() {
        HttpClient httpclient = HttpClients.custom()
                        .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                        .build();
        return httpclient;
    }

    /**
     * This method creates a HttpPost object using a supplied Json(in String form) and accessToken
     */
    public static HttpPost getHttpPostObject(String jsonToSend, String accessToken)
            throws UnsupportedEncodingException {
        HttpPost httppost = new HttpPost(LINKEDIN_SHARE_API_URL);
        StringEntity params = new StringEntity(jsonToSend);
        httppost.addHeader("Content-Type", "application/json");
        httppost.addHeader("x-li-format", "json");
        httppost.addHeader("Authorization", "Bearer " + accessToken);
        httppost.setEntity(params);
        return httppost;
    }

    /**
     * This method sends a httpPost request, parses the response and returns the response in JSONObject format.
     */
    public static JSONObject sendHttpRequestToLinkedIn(HttpPost httppost, HttpClient httpclient) throws IOException {
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();

        // Read the contents of an entity and return it as a String.
        String content = EntityUtils.toString(entity);

        logger.info("RECEIVED A RESPONSE FROM THE SERVER: " + content);

        JSONObject jsnobject = new JSONObject(content);
        return jsnobject;
    }

    /**
     * Called by an event. This method takes in the config to get the access_token
     * This method posts the post to LinkedIn.
     */
    public static void postToLinkedIn() {
        Config config = Config.setupConfig();
        postSuccess = false;

        String accessToken = config.getAppSecret();
        if (!accessTokenValid(accessToken)) { //no valid accessToken, return failure.
            return;
        }

        String jsonToSend = getLinkedInJsonObject();

        logger.info("Access token is valid, sending the json to server: " + jsonToSend);

        try {
            HttpPost httppost = getHttpPostObject(jsonToSend, accessToken);
            HttpClient httpclient = getHttpClientObject();
            JSONObject linkedInResponse = sendHttpRequestToLinkedIn(httppost, httpclient);
            logger.info("LinkedIn Response is : " + linkedInResponse.toString());
            if (linkedInResponse.has("updateUrl") || linkedInResponse.has("updateURL")) {
                //if has updateURL then it successfully got posted
                logger.info("Post has been successfully posted");
                postSuccess = true;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```
