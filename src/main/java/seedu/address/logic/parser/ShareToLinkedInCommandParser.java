//@@author davidten
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
