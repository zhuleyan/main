//@@author davidten
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
