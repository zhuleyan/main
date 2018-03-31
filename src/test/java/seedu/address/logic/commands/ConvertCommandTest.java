//@@author WoodyLau
package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ConvertCommand.
 */
public class ConvertCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_unfilteredList_success() throws Exception {
        ConvertCommand convertCommand = prepareCommand(INDEX_FIRST_PERSON);
        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person convertedPerson = new PersonBuilder(personInFilteredList).withType("Contact").build();

        String expectedMessage = String.format(ConvertCommand.MESSAGE_CONVERT_PERSON_SUCCESS, personInFilteredList);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), convertedPerson);

        assertCommandSuccess(convertCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        ConvertCommand convertCommand = prepareCommand(INDEX_FIRST_PERSON);
        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person convertedPerson = new PersonBuilder(personInFilteredList).withType("Contact").build();

        String expectedMessage = String.format(ConvertCommand.MESSAGE_CONVERT_PERSON_SUCCESS, personInFilteredList);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), convertedPerson);

        assertCommandSuccess(convertCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Convert selection where person is already a Contact
     */
    @Test
    public void execute_invalidPersonTypeUnfilteredList_failure() {
        ConvertCommand convertCommand = prepareCommand(INDEX_SECOND_PERSON);
        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        String expectedMessage = String.format(ConvertCommand.MESSAGE_NOT_CONVERTED, personInFilteredList);

        assertCommandFailure(convertCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidPersonTypeFilteredList_failure() {
        showPersonAtIndex(model, INDEX_SECOND_PERSON);

        ConvertCommand convertCommand = prepareCommand(INDEX_FIRST_PERSON);
        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(ConvertCommand.MESSAGE_NOT_CONVERTED, personInFilteredList);

        assertCommandFailure(convertCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        ConvertCommand convertCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(convertCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Convert filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        ConvertCommand convertCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(convertCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() throws Exception {
        final ConvertCommand standardCommand = prepareCommand(INDEX_FIRST_PERSON);

        // same values -> returns true
        ConvertCommand commandWithSameValues = prepareCommand(INDEX_FIRST_PERSON);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // one command preprocessed when previously equal -> returns false
        commandWithSameValues.preprocessUndoableCommand();
        assertFalse(standardCommand.equals(commandWithSameValues));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new ConvertCommand(INDEX_SECOND_PERSON)));
    }

    /**
     * Returns an {@code ConvertCommand} with parameter {@code index}
     */
    private ConvertCommand prepareCommand(Index index) {
        ConvertCommand convertCommand = new ConvertCommand(index);
        convertCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return convertCommand;
    }
}
