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
