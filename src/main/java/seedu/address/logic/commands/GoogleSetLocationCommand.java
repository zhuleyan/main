//@@author davidten
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.Config;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.ConfigUtil;
import seedu.address.model.person.Address;

/**
 * Allows a user to set their location for Google Maps
 */
public class GoogleSetLocationCommand extends Command {
    public static final String COMMAND_WORD = "set_office_address";
    public static final String COMMAND_ALIAS = "setA";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets your office address for Google Maps ";
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
        String configFilePathUsed = Config.DEFAULT_CONFIG_FILE;
        Config initializedConfig;

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                    + "Using default config properties");
            initializedConfig = new Config();
        }

        initializedConfig.setUserLocation(address.toString());
        try {
            ConfigUtil.saveConfig(initializedConfig, initializedConfig.DEFAULT_CONFIG_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //sets the office address as a string in the config file.

        return new CommandResult(MESSAGE_SUCCESS);
    }

}
