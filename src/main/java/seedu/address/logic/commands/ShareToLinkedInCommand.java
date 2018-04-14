//@@author davidten
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
    public static final String MESSAGE_FAILURE = "Failed to post to LinkedIn";
    public static final String LINKEDIN_SHARE_API_URL = "https://api.linkedin.com/v1/people/~/shares?format=json";

    private static Logger logger = LogsCenter.getLogger(Oauth2Client.class);
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
        Config config = Oauth2Client.setupConfig();
        postSuccess = false;

        String accessToken = config.getAppSecret();
        if (!accessTokenValid(accessToken)) { //no valid accessToken, return failure.
            return;
        }

        String jsonToSend = getLinkedInJsonObject();

        logger.info("SENDING TO THE SERVER: " + jsonToSend);

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
