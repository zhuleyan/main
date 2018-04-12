package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.ChangeThemeCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Theme;

//@@author A0155428B
/**
 * Parses input arguments and creates a new ChangeThemeCommand object
 */
public class ChangeThemeCommandParser implements Parser<ChangeThemeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ChangeThemeCommand
     * and returns an ChangeThemeCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ChangeThemeCommand parse(String args) throws ParseException {
        try {
            Theme theme = ParserUtil.parseTheme(args);
            return new ChangeThemeCommand(theme);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangeThemeCommand.MESSAGE_USAGE));
        }
    }

}
