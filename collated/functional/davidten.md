# davidten
###### /java/seedu/address/ui/BrowserWindow.java
``` java
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

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

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
     * Sends the config to shareToLinkedIn Command
     */
    public void shareToLinkedIn() {
        ShareToLinkedInCommand newShare = new ShareToLinkedInCommand();
        newShare.postToLinkedIn(config);
    }

    /**
     * Opens the linkedin authentication window.
     */
    public void handleLinkedInAuthentication() {
        try {
            Oauth2Client.authenticateWithLinkedIn(config);
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

    @Subscribe
    private void handleShareToLinkedInEvent(ShareToLinkedInEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        shareToLinkedIn();
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

        bWindow = new BrowserWindow(urlString);
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
```
###### /java/seedu/address/commons/core/Config.java
``` java
    private String appId = "78ameftoz7yvk4";
    private String appSecret;

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
###### /java/seedu/address/logic/parser/ShareToLinkedInCommandParser.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Oauth2Client;
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
        Logger logger = LogsCenter.getLogger(Oauth2Client.class);
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
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Oauth2Client;
import seedu.address.commons.events.ui.ShareToLinkedInEvent;

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
        //send event To Main to obtain the configuration file.
        EventsCenter.getInstance().post(new ShareToLinkedInEvent());
        //post success
        if (postSuccess) {
            return new CommandResult(MESSAGE_SUCCESS);
        } else {
            return new CommandResult("Failed to post to LinkedIn");
        }


    }

    /**
     * Called by an event. This method takes in the config to get the access_token
     * This method posts the post to LinkedIn.
     */
    public static void postToLinkedIn(Config config) {
        postSuccess = false;
        Logger logger = LogsCenter.getLogger(Oauth2Client.class);
        String accessToken = config.getAppSecret();
        if (accessToken == null || accessToken.length() == 0) {
            //was unable to share..
            return;
        }
        //use the linkedin api to send to linkedin
        HttpClient httpclient = HttpClients.custom()
                                .setDefaultRequestConfig(RequestConfig.custom()
                                .setCookieSpec(CookieSpecs.STANDARD).build())
                                .build();
        HttpPost httppost = new HttpPost("https://api.linkedin.com/v1/people/~/shares?format=json");

        JSONObject visibilityJsonObj = new JSONObject();
        visibilityJsonObj.put("code", new String("anyone"));

        JSONObject mainJsonObj = new JSONObject();
        mainJsonObj.put("comment", post);
        mainJsonObj.put("visibility", visibilityJsonObj);
        String jsonToSend = mainJsonObj.toString();

        logger.info("SENDING TO THE SERVER: " + jsonToSend);

        try {
            StringEntity params = new StringEntity(jsonToSend);
            httppost.addHeader("Content-Type", "application/json");
            httppost.addHeader("x-li-format", "json");
            httppost.addHeader("Authorization", "Bearer " + config.getAppSecret());
            httppost.setEntity(params);
            HttpResponse response = httpclient.execute(httppost);

            HttpEntity entity = response.getEntity();

            // Read the contents of an entity and return it as a String.
            String content = EntityUtils.toString(entity);

            logger.info("RECEIVED A RESPONSE FROM THE SERVER: " + content);
            postSuccess = true;
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
