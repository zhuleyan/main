//@@author WoodyLau
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
