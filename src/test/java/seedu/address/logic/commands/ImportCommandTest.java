//@@author zhuleyan
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.exception.InputThemeEqualsCurrentThemeException;
import seedu.address.model.person.Contact;
import seedu.address.model.person.Lead;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.testutil.PersonBuilder;

public class ImportCommandTest {
    public static final String SAMPLE_NAME = "Misti Offen";
    public static final String SAMPLE_PHONE = "12345678";
    public static final String SAMPLE_EMAIL = "moffen2@unicef.org";
    public static final String SAMPLE_ADDRESS = "359, Milwaukee Terrace";
    public static final String SAMPLE_TAGS = "HR";
    public static final String SAMPLE_REMARK = "";
    public static final String SAMPLE_TYPE = "Lead";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new ImportCommand(null);
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        List<Lead> persons = new ArrayList<>();
        Lead firstValidPerson = (Lead) new PersonBuilder().build();
        Lead secondValidPerson = (Lead) new PersonBuilder().withName(SAMPLE_NAME).withPhone(SAMPLE_PHONE)
                .withEmail(SAMPLE_EMAIL).withAddress(SAMPLE_ADDRESS).withRemark(SAMPLE_REMARK).build();

        persons.add(firstValidPerson);
        persons.add(secondValidPerson);
        CommandResult commandResult = getImportCommandForPerson(persons, modelStub).execute();

        assertEquals(String.format(ImportCommand.MESSAGE_SUCCESS, persons), commandResult.feedbackToUser);
        assertEquals(persons, modelStub.personsAdded);
    }

    @Test
    public void equals() {
        Lead alice = (Lead) new PersonBuilder().withName("Alice").build();
        Lead bob = (Lead) new PersonBuilder().withName("Bob").build();
        Lead charlie = (Lead) new PersonBuilder().withName("Charlie").build();
        List<Lead> firstList = new ArrayList<>();
        List<Lead> secondList = new ArrayList<>();
        firstList.add(alice);
        firstList.add(bob);
        secondList.add(alice);
        secondList.add(charlie);
        ImportCommand addFirstListCommand = new ImportCommand(firstList);
        ImportCommand addSecondListCommand = new ImportCommand(secondList);

        // same object -> returns true
        assertTrue(addFirstListCommand.equals(addFirstListCommand));

        // same values -> returns true
        ImportCommand addFirstListCommandCopy = new ImportCommand(firstList);
        assertTrue(addFirstListCommand.equals(addFirstListCommandCopy));

        // different types -> returns false
        assertFalse(addFirstListCommand.equals(1));

        // null -> returns false
        assertFalse(addFirstListCommand.equals(null));

        // different person -> returns false
        assertFalse(addFirstListCommand.equals(addSecondListCommand));
    }

    /**
     * Generates a new ImportCommand with the details of the given person list.
     */
    private ImportCommand getImportCommandForPerson(List<Lead> persons, Model model) {
        ImportCommand command = new ImportCommand(persons);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {

        @Override
        public void addPerson(Person person) throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyAddressBook newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deletePerson(Person target) throws PersonNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void updatePerson(Person target, Person editedPerson)
                throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public void convertPerson(Lead lead, Contact contact)
                throws DuplicatePersonException, PersonNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void sortAllPersons() {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void updateTheme(String theme) throws InputThemeEqualsCurrentThemeException {
            fail("This method should not be called.");
        };

        @Override
        public String getThemeFilePath() {
            fail("This method should not be called.");
            return null;
        }
    }

    /**
     * A Model stub that always throw a DuplicatePersonException when trying to add a person list.
     */
    private class ModelStubThrowingDuplicatePersonException extends ModelStub {

        @Override
        public void addPerson(Person person) throws DuplicatePersonException {
            throw new DuplicatePersonException();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

    /**
     * A Model stub that always accept the person list being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        @Override
        public void addPerson(Person person) throws DuplicatePersonException {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
