//@@author davidten
package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.Test;

import seedu.address.commons.core.Config;
import seedu.address.logic.commands.GoogleSetLocationCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;

public class GoogleSetLocationCommandParserTest {
    private String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            GoogleSetLocationCommand.MESSAGE_USAGE);
    private GoogleSetLocationCommandParser parser = new GoogleSetLocationCommandParser();

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        //missing address prefix
        assertParseFailure(parser, VALID_ADDRESS_AMY, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        //tests invalid addressValue
        assertParseFailure(parser, INVALID_ADDRESS_DESC, Address.MESSAGE_ADDRESS_CONSTRAINTS);
    }

    @Test
    public void parseSuccess() {
        //We cannot use the traditional assertParseSuccess because the address is created in the parser, and
        //even with the same input it will return a different command object.
        //Therefore, we check with the config file instead.

        Config.clearUserLocation();
        Config preConfig = Config.setupConfig();
        assertNull(preConfig.getUserLocation());

        GoogleSetLocationCommand command = null;
        try {
            command = parser.parse(ADDRESS_DESC_AMY);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertNotNull(command);
        command.execute();

        Config postConfig = Config.setupConfig();
        assertEquals(VALID_ADDRESS_AMY, postConfig.getUserLocation());
        Config.clearUserLocation();
    }
}
