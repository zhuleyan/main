# Sheikh-Umar
###### /java/systemtests/ClearCommandSystemTest.java
``` java
        /* Case: clear non-empty address book using alias -> cleared
         */
        assertCommandSuccess("   " + ClearCommand.COMMAND_ALIAS + " ab12   ");
        assertSelectedCardUnchanged();
```
###### /java/systemtests/AddCommandSystemTest.java
``` java
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PERSON);
```
###### /java/systemtests/AddCommandSystemTest.java
``` java
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PERSON);
```
###### /java/systemtests/AddCommandSystemTest.java
``` java
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PERSON);
```
###### /java/systemtests/AddCommandSystemTest.java
``` java
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PERSON);
```
###### /java/systemtests/AddCommandSystemTest.java
``` java
        /* Case: missing name -> rejected */
        command = AddCommand.COMMAND_ALIAS + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
```
###### /java/systemtests/FindCommandSystemTest.java
``` java
        /* Case: find phone number of person in address book using alias command -> 1 person found */
        command = FindCommand.COMMAND_ALIAS + " " + BENSON.getPhone().value;
        ModelHelper.setFilteredList(expectedModel, BENSON);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find phone number of person in address book using alias command -> 1 person found */
        command = FindCommand.COMMAND_ALIAS + " " + FIONA.getPhone().value;
        ModelHelper.setFilteredList(expectedModel, FIONA);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find phone number of person in address book using alias command -> 1 person found */
        command = FindCommand.COMMAND_ALIAS + " " + GEORGE.getPhone().value;
        ModelHelper.setFilteredList(expectedModel, GEORGE);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find phone number of person in address book using alias command -> 1 person found */
        command = FindCommand.COMMAND_ALIAS + " " + ELLE.getPhone().value;
        ModelHelper.setFilteredList(expectedModel, ELLE);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find email address of person in address book using alias command -> 1 person found */
        command = FindCommand.COMMAND_ALIAS + " " + BENSON.getEmail().value;
        ModelHelper.setFilteredList(expectedModel, BENSON);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find email address of person in address book using alias command -> 1 person found */
        command = FindCommand.COMMAND_ALIAS + " " + FIONA.getEmail().value;
        ModelHelper.setFilteredList(expectedModel, FIONA);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find email address of person in address book using alias command -> 1 person found */
        command = FindCommand.COMMAND_ALIAS + " " + GEORGE.getEmail().value;
        ModelHelper.setFilteredList(expectedModel, GEORGE);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find email address of person in address book using alias command -> 1 person found */
        command = FindCommand.COMMAND_ALIAS + " " + ELLE.getEmail().value;
        ModelHelper.setFilteredList(expectedModel, ELLE);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();
```
###### /java/seedu/address/logic/parser/FindCommandParserTest.java
``` java
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
```
###### /java/seedu/address/logic/commands/DisplayCommandTest.java
``` java
package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.TypeMatchesKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class DisplayCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        TypeMatchesKeywordsPredicate firstPredicate =
                new TypeMatchesKeywordsPredicate(Collections.singletonList("first"));
        TypeMatchesKeywordsPredicate secondPredicate =
                new TypeMatchesKeywordsPredicate(Collections.singletonList("second"));

        DisplayCommand displayFirstCommand = new DisplayCommand(firstPredicate);

        // same object -> returns true
        assertTrue(displayFirstCommand.equals(displayFirstCommand));

        // same values -> returns true
        DisplayCommand displayFirstCommandCopy = new DisplayCommand(firstPredicate);
        assertTrue(displayFirstCommand.equals(displayFirstCommandCopy));

        // different types -> returns false
        assertFalse(displayFirstCommand.equals(1));

        // null -> returns false
        assertFalse(displayFirstCommand.equals(null));
    }

    @Test
    public void execute_zeroTypeEntered() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        DisplayCommand command = prepareCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_userEnteredLeads_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        DisplayCommand command = prepareCommand("Leads");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_userEnteredContacts_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        DisplayCommand command = prepareCommand("Contacts");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    /**
     * Parses {@code userInput} into a {@code FindCommand}.
     */
    private DisplayCommand prepareCommand(String userInput) {
        DisplayCommand command =
                new DisplayCommand(new TypeMatchesKeywordsPredicate(Arrays.asList(userInput.split("\\s+"))));
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<Person>} is equal to {@code expectedList}<br>
     *     - the {@code AddressBook} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(DisplayCommand command, String expectedMessage, List<Person> expectedList) {
        AddressBook expectedAddressBook = new AddressBook(model.getAddressBook());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredPersonList());
        assertEquals(expectedAddressBook, model.getAddressBook());
    }
}
```
###### /data/XmlUtilTest/validAddressBook.xml
``` xml
        <email isPrivate="false">martin@example.com</email>
```
