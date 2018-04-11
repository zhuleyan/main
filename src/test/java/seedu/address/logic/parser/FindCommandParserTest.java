package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.NameContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);
    }

    //@@author Sheikh-Umar
    @Test
    public void parse_validArgs_returnsFindCommandForPhoneNumber() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("99991234", "88776655")));
        assertParseSuccess(parser, "99991234 88776655", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n 99991234 \n \t 88776655  \t", expectedFindCommand);
    }

    @Test
    public void parse_validArgs_returnsFindCommandForEmailAddress() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("johntan@gmail.com")));
        assertParseSuccess(parser, "johntan@gmail.com", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n johntan@gmail.com  \t", expectedFindCommand);
    }
    //@@author
}
