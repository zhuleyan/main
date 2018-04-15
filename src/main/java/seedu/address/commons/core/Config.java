package seedu.address.commons.core;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.ConfigUtil;

/**
 * Config values used by the app
 */
public class Config {

    public static final String DEFAULT_CONFIG_FILE = "config.json";

    private static final Logger logger = LogsCenter.getLogger(Config.class);
    // Config values customizable through config file
    private String appTitle = "CRM Book";
    private Level logLevel = Level.INFO;
    private String userPrefsFilePath = "preferences.json";
    //@@author davidten
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
    //@@author
    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public Level getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(Level logLevel) {
        this.logLevel = logLevel;
    }

    public String getUserPrefsFilePath() {
        return userPrefsFilePath;
    }

    public void setUserPrefsFilePath(String userPrefsFilePath) {
        this.userPrefsFilePath = userPrefsFilePath;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Config)) { //this handles null as well.
            return false;
        }

        Config o = (Config) other;

        return Objects.equals(appTitle, o.appTitle)
                && Objects.equals(logLevel, o.logLevel)
                && Objects.equals(userPrefsFilePath, o.userPrefsFilePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appTitle, logLevel, userPrefsFilePath);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("App title : " + appTitle);
        sb.append("\nCurrent log level : " + logLevel);
        sb.append("\nPreference file Location : " + userPrefsFilePath);
        //@@author davidten
        sb.append("\nApp Id: " + appId);
        sb.append("\nApp Secret: " + appSecret);
        sb.append("\nUser Location: " + userLocation);
        //@@author
        return sb.toString();
    }
    //@@author davidten-resued
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
