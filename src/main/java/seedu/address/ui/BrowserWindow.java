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
     * <ul>
     *     <li>
     *         if this method is called on a thread other than the JavaFX Application Thread.
     *     </li>
     *     <li>
     *         if this method is called during animation or layout processing.
     *     </li>
     *     <li>
     *         if this method is called on the primary stage.
     *     </li>
     *     <li>
     *         if {@code dialogStage} is already showing.
     *     </li>
     * </ul>
     */
    public void show() {
        logger.info("Opening a browser for the application.");
        getRoot().show();
    }


}
