package seedu.address.ui;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import seedu.address.MainApp;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Oauth2Client;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.Person;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    public static final String DEFAULT_PAGE = "default.html";
    public static final String SEARCH_PAGE_URL =
            "https://www.google.com.sg/search?ei=EmypWtGyJsiEvQSsnbGwDQ&q=";

    private static final String FXML = "BrowserPanel.fxml";

    private static Config config;

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private WebView browser;

    public BrowserPanel() {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        loadDefaultPage();
        registerAsAnEventHandler(this);
    }

    private void loadPersonPage(Person person) {
        loadPage(SEARCH_PAGE_URL + person.getName().fullName);
    }

    public void loadPage(String url) {
        Platform.runLater(() -> browser.getEngine().load(url));
    }

    /**
     * Loads a default HTML file with a background that matches the general theme.
     */
    private void loadDefaultPage() {
        URL defaultPage = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE);
        loadPage(defaultPage.toExternalForm());
    }

    /**
     * Frees resources allocated to the browser.
     */
    public void freeResources() {
        browser = null;
    }
    //@@author davidten
    /**
     * Gets configuration to be used when showing google maps
     */
    public static void getConfig() {
        config = Oauth2Client.setupConfig();
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
            loadPersonPage(event.getNewSelection().person);
        } else {
            //also need to check that URL is limited to 2048 characters
            //person has home location set up
            String url = generateUrl(config.getUserLocation(), event.getNewSelection().person.getAddress().toString());
            logger.info("URL IS " + url);
            loadPage(url);
        }
    }
}
