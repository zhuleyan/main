# WoodyLau
###### /java/seedu/address/logic/commands/EditDetailsCommandTest.java
``` java
package seedu.address.logic.commands;

import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.EditDetailsCommand.EditContactDescriptor;
import seedu.address.logic.commands.EditDetailsCommand.EditLeadDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Contact;
import seedu.address.model.person.Lead;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class EditDetailsCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_leadCompany_success() throws Exception {
        Lead personInFilteredList = (Lead) model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        EditLeadDescriptor descriptor = new EditLeadDescriptor();
        descriptor.setCompany("Hello Inc");

        Lead editedPerson = (Lead) new PersonBuilder(personInFilteredList).withType("Lead").build();
        editedPerson.setCompany("Hello Inc");

        String expectedMessage = String.format(EditDetailsCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedPerson);

        EditDetailsCommand editDetailsCommand = prepareLeadCommand(INDEX_FIRST_PERSON, descriptor);

        assertCommandSuccess(editDetailsCommand, model, expectedMessage, expectedModel);
        assertTrue(personInFilteredList.getCompany().equals(editedPerson.getCompany()));
    }

    @Test
    public void execute_leadRating_success() throws Exception {
        Lead personInFilteredList = (Lead) model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        EditLeadDescriptor descriptor = new EditLeadDescriptor();
        descriptor.setRating(1);

        Lead editedPerson = (Lead) new PersonBuilder(personInFilteredList).withType("Lead").build();
        editedPerson.setRating(1);

        String expectedMessage = String.format(EditDetailsCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedPerson);

        EditDetailsCommand editDetailsCommand = prepareLeadCommand(INDEX_FIRST_PERSON, descriptor);

        assertCommandSuccess(editDetailsCommand, model, expectedMessage, expectedModel);
        assertTrue(((Lead) model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased())).getRating()
                == editedPerson.getRating());

        descriptor.setRating(5);

        editedPerson.setRating(5);

        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedPerson);

        editDetailsCommand = prepareLeadCommand(INDEX_FIRST_PERSON, descriptor);

        assertCommandSuccess(editDetailsCommand, model, expectedMessage, expectedModel);
        assertTrue(((Lead) model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased())).getRating()
                == editedPerson.getRating());
    }

    @Test
    public void execute_leadWebsite_success() throws Exception {
        Lead personInFilteredList = (Lead) model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        EditLeadDescriptor descriptor = new EditLeadDescriptor();
        descriptor.setWebsite("gagle.com");

        Lead editedPerson = (Lead) new PersonBuilder(personInFilteredList).withType("Lead").build();
        editedPerson.setWebsite("gagle.com");

        String expectedMessage = String.format(EditDetailsCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedPerson);

        EditDetailsCommand editDetailsCommand = prepareLeadCommand(INDEX_FIRST_PERSON, descriptor);

        assertCommandSuccess(editDetailsCommand, model, expectedMessage, expectedModel);
        assertTrue(personInFilteredList.getWebsite().equals(editedPerson.getWebsite()));
    }

    @Test
    public void execute_leadIndustry_success() throws Exception {
        Lead personInFilteredList = (Lead) model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        EditLeadDescriptor descriptor = new EditLeadDescriptor();
        descriptor.setIndustry("Software");

        Lead editedPerson = (Lead) new PersonBuilder(personInFilteredList).withType("Lead").build();
        editedPerson.setIndustry("Software");

        String expectedMessage = String.format(EditDetailsCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedPerson);

        EditDetailsCommand editDetailsCommand = prepareLeadCommand(INDEX_FIRST_PERSON, descriptor);

        assertCommandSuccess(editDetailsCommand, model, expectedMessage, expectedModel);
        assertTrue(personInFilteredList.getIndustry().equals(editedPerson.getIndustry()));
    }

    @Test
    public void execute_leadRating_failure() throws Exception {
        EditLeadDescriptor descriptor = new EditLeadDescriptor();
        descriptor.setRating(0);

        EditDetailsCommand editDetailsCommand = prepareLeadCommand(INDEX_FIRST_PERSON, descriptor);

        assertCommandFailure(editDetailsCommand, model, "Rating only goes from 1 to 5. Rating was not updated");

        descriptor.setRating(6);

        editDetailsCommand = prepareLeadCommand(INDEX_FIRST_PERSON, descriptor);

        assertCommandFailure(editDetailsCommand, model, "Rating only goes from 1 to 5. Rating was not updated");
    }

    @Test
    public void execute_leadNoArg_failure() throws Exception {
        EditLeadDescriptor descriptor = new EditLeadDescriptor();

        EditDetailsCommand editDetailsCommand = prepareLeadCommand(INDEX_FIRST_PERSON, descriptor);

        assertCommandFailure(editDetailsCommand, model, EditDetailsCommand.MESSAGE_NOT_EDITED_FOR_LEAD);
    }

    @Test
    public void execute_contactCompany_success() throws Exception {
        Contact personInFilteredList = (Contact) model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        EditContactDescriptor descriptor = new EditContactDescriptor();
        descriptor.setCompany("Hello Inc");

        Contact editedPerson = (Contact) new PersonBuilder(personInFilteredList).withType("Contact").build();
        editedPerson.setCompany("Hello Inc");

        String expectedMessage = String.format(EditDetailsCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased()), editedPerson);

        EditDetailsCommand editDetailsCommand = prepareContactCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandSuccess(editDetailsCommand, model, expectedMessage, expectedModel);
        assertTrue(personInFilteredList.getCompany().equals(editedPerson.getCompany()));
    }

    @Test
    public void execute_contactDepartment_success() throws Exception {
        Contact personInFilteredList = (Contact) model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        EditContactDescriptor descriptor = new EditContactDescriptor();
        descriptor.setDepartment("IT");

        Contact editedPerson = (Contact) new PersonBuilder(personInFilteredList).withType("Contact").build();
        editedPerson.setDepartment("IT");

        String expectedMessage = String.format(EditDetailsCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased()), editedPerson);

        EditDetailsCommand editDetailsCommand = prepareContactCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandSuccess(editDetailsCommand, model, expectedMessage, expectedModel);
        assertTrue(personInFilteredList.getDepartment().equals(editedPerson.getDepartment()));
    }

    @Test
    public void execute_contactNoArg_failure() throws Exception {
        EditContactDescriptor descriptor = new EditContactDescriptor();

        EditDetailsCommand editDetailsCommand = prepareContactCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editDetailsCommand, model, EditDetailsCommand.MESSAGE_NOT_EDITED_FOR_CONTACT);
    }

    /**
     * Returns an {@code EditCommand} with parameters {@code index} and {@code descriptor}
     */
    private EditDetailsCommand prepareLeadCommand(Index index, EditLeadDescriptor descriptor) {
        EditContactDescriptor nullDescriptor = new EditContactDescriptor();
        EditDetailsCommand editCommand = new EditDetailsCommand(index, descriptor, nullDescriptor);
        editCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return editCommand;
    }
    /**
     * Returns an {@code EditCommand} with parameters {@code index} and {@code descriptor}
     */
    private EditDetailsCommand prepareContactCommand(Index index, EditContactDescriptor descriptor) {
        EditLeadDescriptor nullDescriptor = new EditLeadDescriptor();
        EditDetailsCommand editCommand = new EditDetailsCommand(index, nullDescriptor, descriptor);
        editCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return editCommand;
    }
}
```
###### /java/seedu/address/logic/commands/ConvertCommandTest.java
``` java
package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Contact;
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

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        assertTrue(((Contact) model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .getConvertedDate().equals(dateFormat.format(date)));
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

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        assertTrue(((Contact) model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .getConvertedDate().equals(dateFormat.format(date)));
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
```
