# Sheikh-Umar
###### /src/test/java/systemtests/ClearCommandSystemTest.java
``` java
        /* Case: clear non-empty address book using alias -> cleared
         */
        assertCommandSuccess("   " + ClearCommand.COMMAND_ALIAS + " ab12   ");
        assertSelectedCardUnchanged();
```
###### /src/test/java/systemtests/AddCommandSystemTest.java
``` java
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PERSON);
```
###### /src/test/java/systemtests/AddCommandSystemTest.java
``` java
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PERSON);
```
###### /src/test/java/systemtests/AddCommandSystemTest.java
``` java
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PERSON);
```
###### /src/test/java/systemtests/AddCommandSystemTest.java
``` java
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_PERSON);
```
###### /src/test/java/systemtests/AddCommandSystemTest.java
``` java
        /* Case: missing name -> rejected */
        command = AddCommand.COMMAND_ALIAS + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
```
###### /src/test/java/seedu/address/logic/commands/DisplayCommandTest.java
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
###### /src/main/java/seedu/address/logic/parser/DisplayCommandParser.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.DisplayCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.TypeMatchesKeywordsPredicate;

/**
 * Parses input arguments and creates a new DisplayCommand object
 */
public class DisplayCommandParser implements Parser<DisplayCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DisplayCommand
     * and returns an DisplayCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DisplayCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DisplayCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new DisplayCommand(new TypeMatchesKeywordsPredicate(Arrays.asList(nameKeywords)));
    }

}
```
###### /src/main/java/seedu/address/logic/parser/AddressBookParser.java
``` java
        case DisplayCommand.COMMAND_WORD:
        case DisplayCommand.COMMAND_ALIAS:
            return new DisplayCommandParser().parse(arguments);
```
###### /src/main/java/seedu/address/logic/commands/DeleteCommand.java
``` java
    public static final String COMMAND_ALIAS = "d";
```
###### /src/main/java/seedu/address/logic/commands/DeleteCommand.java
``` java
    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Lead/Contact: %1$s";
```
###### /src/main/java/seedu/address/logic/commands/ListCommand.java
``` java
    public static final String COMMAND_ALIAS = "l";
```
###### /src/main/java/seedu/address/logic/commands/RedoCommand.java
``` java
    public static final String COMMAND_ALIAS = "r";
```
###### /src/main/java/seedu/address/logic/commands/ConvertCommand.java
``` java
    public static final String COMMAND_ALIAS = "con";
```
###### /src/main/java/seedu/address/logic/commands/ConvertCommand.java
``` java
    public static final String MESSAGE_DUPLICATE_PERSON = "This Lead/Contact already exists in the CRM Book.";
```
###### /src/main/java/seedu/address/logic/commands/SortCommand.java
``` java
    public static final String COMMAND_ALIAS = "st";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts and lists all Leads and Contacts by name in alphabetical order.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Sorted all Leads or Contacts by name";
```
###### /src/main/java/seedu/address/logic/commands/ClearCommand.java
``` java
    public static final String COMMAND_ALIAS = "c";

    public static final String MESSAGE_SUCCESS = "CRM Book has been cleared!";
```
###### /src/main/java/seedu/address/logic/commands/DisplayCommand.java
``` java
package seedu.address.logic.commands;

import seedu.address.model.person.TypeMatchesKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class DisplayCommand extends Command {

    public static final String COMMAND_WORD = "display";
    public static final String COMMAND_ALIAS = "disp";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Displays List list or Contacts list.\n"
            + "Parameters: [KEYWORD]\n"
            + "Example: " + COMMAND_WORD + " Lead";

    private final TypeMatchesKeywordsPredicate predicate;

    public DisplayCommand(TypeMatchesKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(predicate);
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DisplayCommand // instanceof handles nulls
                && this.predicate.equals(((DisplayCommand) other).predicate)); // state check
    }
}
```
###### /src/main/java/seedu/address/logic/commands/LinkedInLoginCommand.java
``` java
    public static final String COMMAND_ALIAS = "linklog";
```
###### /src/main/java/seedu/address/logic/commands/AddCommand.java
``` java
    public static final String COMMAND_ALIAS = "a";
```
###### /src/main/java/seedu/address/logic/commands/AddCommand.java
``` java
    public static final String MESSAGE_SUCCESS = "New Lead added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This Lead or Contact is already in the CRM Book.";
```
###### /src/main/java/seedu/address/logic/commands/HelpCommand.java
``` java
    public static final String COMMAND_ALIAS = "h";
```
###### /src/main/java/seedu/address/logic/commands/RemarkCommand.java
``` java
    public static final String COMMAND_ALIAS = "rem";
```
###### /src/main/java/seedu/address/logic/commands/RemarkCommand.java
``` java
    public static final String MESSAGE_ADD_REMARK_SUCCESS = "Added remark to Lead/Contact: %1$s";
    public static final String MESSAGE_DELETE_REMARK_SUCCESS = "Removed remark from Lead/Contact: %1$s";
```
###### /src/main/java/seedu/address/logic/commands/HistoryCommand.java
``` java
    public static final String COMMAND_ALIAS = "hist";
```
###### /src/main/java/seedu/address/logic/commands/EditCommand.java
``` java
    public static final String COMMAND_ALIAS = "e";
```
###### /src/main/java/seedu/address/logic/commands/EditCommand.java
``` java
    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Lead/Contact: %1$s";
```
###### /src/main/java/seedu/address/logic/commands/EditCommand.java
``` java
    public static final String MESSAGE_DUPLICATE_PERSON = "This Lead/Contact already exists in the CRM Book.";
```
###### /src/main/java/seedu/address/logic/commands/SelectCommand.java
``` java
    public static final String COMMAND_ALIAS = "s";
```
###### /src/main/java/seedu/address/logic/commands/SelectCommand.java
``` java
    public static final String MESSAGE_SELECT_PERSON_SUCCESS = "Selected Lead or Contact: %1$s";
```
###### /src/main/java/seedu/address/logic/commands/UndoCommand.java
``` java
    public static final String COMMAND_ALIAS = "u";
```
###### /src/main/java/seedu/address/logic/commands/FindCommand.java
``` java
    public static final String COMMAND_ALIAS = "f";
```
###### /src/main/java/seedu/address/logic/commands/ExitCommand.java
``` java
    public static final String COMMAND_ALIAS = "ex";

    public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Exiting CRM Book as requested ...";
```
###### /src/main/java/seedu/address/model/person/TypeMatchesKeywordsPredicate.java
``` java
package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class TypeMatchesKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public TypeMatchesKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getType().value, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TypeMatchesKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((TypeMatchesKeywordsPredicate) other).keywords)); // state check
    }

}
```
###### /src/main/java/seedu/address/model/person/UniquePersonList.java
``` java
    /**
     * Returns true if the list contains either the phone number or email address
     * of the given argument.
     */
    public boolean contains(Person toCheck) {
        requireNonNull(toCheck);
        for (int i = 0; i < internalList.size(); i++) {
            Person current = internalList.get(i);
            if (current.getPhone().equals(toCheck.getPhone())
                    || current.getEmail().equals(toCheck.getEmail())) {
                return true;
            }
        }
        return false;
    }
```
