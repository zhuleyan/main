# zhuleyan
###### /java/seedu/address/logic/parser/RemarkCommandParserTest.java
``` java
//reused
public class RemarkCommandParserTest {
    private RemarkCommandParser parser = new RemarkCommandParser();
    private final String someRemark = "This is a remark.";

    @Test
    public void parse_indexSpecified_success() throws Exception {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_REMARK.toString() + someRemark;

        RemarkCommand expectedCommand = new RemarkCommand(targetIndex, new Remark(someRemark));
        assertParseSuccess(parser, userInput, expectedCommand);

        userInput = targetIndex.getOneBased() + " " + PREFIX_REMARK.toString();
        expectedCommand = new RemarkCommand(targetIndex, new Remark(""));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingParts_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE);

        // no index specified
        assertParseFailure(parser, RemarkCommand.COMMAND_WORD + " " + someRemark, expectedMessage);


        // no prefix specified
        assertParseFailure(parser, RemarkCommand.COMMAND_WORD, expectedMessage);
    }
}
```
###### /java/seedu/address/logic/commands/ImportCommandTest.java
``` java
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
```
###### /java/seedu/address/logic/commands/SortCommandTest.java
``` java
package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class SortCommandTest {

    private Model model;
    private Model expectedModel;
    private SortCommand sortCommand;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        sortCommand = new SortCommand();
        sortCommand.setData(model, new CommandHistory(), new UndoRedoStack());
    }

    @Test
    public void execute_sortList_success() {
        assertCommandSuccess(sortCommand, model, SortCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
```
###### /java/seedu/address/logic/commands/RemarkCommandTest.java
``` java
//reused
public class RemarkCommandTest {

    public static final String REMARK_EXAMPLE = "This is a remark";
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addRemarkUnfilteredList_leadSuccess() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person firstEditedPerson = new PersonBuilder(firstPerson).withRemark(REMARK_EXAMPLE).build();

        RemarkCommand remarkCommand = prepareCommand(INDEX_FIRST_PERSON, firstEditedPerson.getRemark().value);

        String expectedMessage = String.format(RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS, firstEditedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(firstPerson, firstEditedPerson);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addRemarkUnfilteredList_contactSuccess() throws Exception {
        Person secondPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Person secondEditedPerson = new PersonBuilder(secondPerson).withRemark(REMARK_EXAMPLE).build();

        RemarkCommand remarkCommand = prepareCommand(INDEX_SECOND_PERSON, secondEditedPerson.getRemark().value);

        String expectedMessage = String.format(RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS, secondEditedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(secondPerson, secondEditedPerson);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteRemarkUnfilteredList_leadSuccess() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person firstEditedPerson = new PersonBuilder(firstPerson).withRemark("").build();

        RemarkCommand remarkCommand = prepareCommand(INDEX_FIRST_PERSON, firstEditedPerson.getRemark().toString());

        String expectedMessage = String.format(RemarkCommand.MESSAGE_DELETE_REMARK_SUCCESS, firstEditedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(firstPerson, firstEditedPerson);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteRemarkUnfilteredList_contactSuccess() throws Exception {
        Person secondPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Person secondEditedPerson = new PersonBuilder(secondPerson).withRemark("").build();

        RemarkCommand remarkCommand = prepareCommand(INDEX_SECOND_PERSON, secondEditedPerson.getRemark().toString());

        String expectedMessage = String.format(RemarkCommand.MESSAGE_DELETE_REMARK_SUCCESS, secondEditedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(secondPerson, secondEditedPerson);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person firstPersonInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .withRemark(REMARK_EXAMPLE).build();
        RemarkCommand remarkCommand = prepareCommand(INDEX_FIRST_PERSON,
                editedPerson.getRemark().value);

        String expectedMessage = String.format(RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(firstPersonInFilteredList, editedPerson);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() throws Exception {
        Index invalidIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        RemarkCommand remarkCommand = prepareCommand(invalidIndex, VALID_REMARK_BOB);

        assertCommandFailure(remarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() throws Exception {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index invalidIndex = INDEX_SECOND_PERSON;
        // ensures that invalidIndex is still in bounds of address book list
        assertTrue(invalidIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        RemarkCommand remarkCommand = prepareCommand(invalidIndex, VALID_REMARK_BOB);

        assertCommandFailure(remarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personToEdit).withRemark(REMARK_EXAMPLE).build();
        RemarkCommand remarkCommand = prepareCommand(INDEX_FIRST_PERSON, REMARK_EXAMPLE);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        // remark -> first person remark edited
        remarkCommand.execute();
        undoRedoStack.push(remarkCommand);

        // undo -> reverts addressbook back to previous state and filtered person list to show all persons
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first person edited again
        expectedModel.updatePerson(personToEdit, editedPerson);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index invalidIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        RemarkCommand remarkCommand = prepareCommand(invalidIndex, "");

        // execution failed -> remarkCommand not pushed into undoRedoStack
        assertCommandFailure(remarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Edits a {@code Person#remark} from a filtered list.
     * 2. Undo the edit.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously edited person in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the edit. This ensures {@code RedoCommand} edits the person object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_samePersonDeleted() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        RemarkCommand remarkCommand = prepareCommand(INDEX_FIRST_PERSON, REMARK_EXAMPLE);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        showPersonAtIndex(model, INDEX_SECOND_PERSON);
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personToEdit).withRemark(REMARK_EXAMPLE).build();
        // remark -> edits second person remark in unfiltered person list / first person in filtered person list
        remarkCommand.execute();
        undoRedoStack.push(remarkCommand);

        // undo -> reverts addressbook back to previous state and filtered person list to show all persons
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.updatePerson(personToEdit, editedPerson);
        assertNotEquals(personToEdit, model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()));
        // redo -> edits same second person in unfiltered person list
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() throws Exception {
        final RemarkCommand standardCommand = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(VALID_REMARK_AMY));

        // same values -> returns true
        RemarkCommand commandWithSameValues = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(VALID_REMARK_AMY));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new RemarkCommand(INDEX_SECOND_PERSON, new Remark(VALID_REMARK_AMY))));

        // different remark -> returns false
        assertFalse(standardCommand.equals(new RemarkCommand(INDEX_FIRST_PERSON, new Remark(VALID_REMARK_BOB))));
    }

    /**
     * Returns an {@code RemarkCommand} with parameters {@code index} and {@code remark}
     */
    private RemarkCommand prepareCommand(Index index, String remark) {
        RemarkCommand remarkCommand = new RemarkCommand(index, new Remark(remark));
        remarkCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return remarkCommand;
    }
}
```
###### /java/seedu/address/model/person/RemarkTest.java
``` java
//reused
public class RemarkTest {
    @Test
    public void isEqualRemark() {
        Remark remark = new Remark("Test");

        // same object -> returns true
        assertTrue(remark.equals(remark));

        //same values -> returns true
        Remark sameRemark = new Remark(remark.value);
        assertTrue(remark.equals(sameRemark));

        //different types -> returns false
        assertFalse(remark.equals(1));

        //null -> returns false
        assertFalse(remark.equals(null));

        //different remarks -> returns false
        Remark differentRemark = new Remark("Different");
        assertFalse(remark.equals(differentRemark));

    }
}
```
