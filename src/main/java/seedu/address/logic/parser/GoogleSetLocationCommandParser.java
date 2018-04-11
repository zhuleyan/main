//@@author davidten
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;

import java.util.stream.Stream;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.GoogleSetLocationCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class GoogleSetLocationCommandParser implements Parser<GoogleSetLocationCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an GoogleSetLocationCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public GoogleSetLocationCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ADDRESS);

        if (!arePrefixesPresent(argMultimap, PREFIX_ADDRESS)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                GoogleSetLocationCommand.MESSAGE_USAGE));
        }

        try {
            Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS)).get();

            return new GoogleSetLocationCommand(address);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }
    //@@author
    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
