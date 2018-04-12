//@@author zhuleyan-reused
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Contact;
import seedu.address.model.person.Lead;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Edits the remark of an existing person in the address book.
 */
public class RemarkCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "remark";
    //@@author Sheikh-Umar
    public static final String COMMAND_ALIAS = "rem";
    //@@author

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the remark of the Lead/Contact identified "
            + "by the index number used in the last Leads-Contacts listing. "
            + "Existing remark will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_REMARK + "[REMARK]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_REMARK + "Likes to drink coffee.";

    //@@author Sheikh-Umar
    public static final String MESSAGE_ADD_REMARK_SUCCESS = "Added remark to Lead/Contact: %1$s";
    public static final String MESSAGE_DELETE_REMARK_SUCCESS = "Removed remark from Lead/Contact: %1$s";
    //@@author

    private final Index index;
    private final Remark remark;

    private Person personToEdit;
    private Person editedPerson;

    /**
     * @param index of the person in the filtered person list to edit
     * @param remark the remark to be updated to the person
     */
    public RemarkCommand(Index index, Remark remark) {
        requireNonNull(index);
        requireNonNull(remark);

        this.index = index;
        this.remark = remark;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(personToEdit);
        requireNonNull(editedPerson);

        try {
            model.updatePerson(personToEdit, editedPerson);
        } catch (DuplicatePersonException dpe) {
            throw new AssertionError("The changing of the remark should not result in a duplicate");
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        personToEdit = lastShownList.get(index.getZeroBased());
        if (personToEdit.getType().value.equals("Lead")) {
            editedPerson = new Lead(personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                    personToEdit.getAddress(), remark, personToEdit.getTags());
        } else {
            editedPerson = new Contact(personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                    personToEdit.getAddress(), remark, personToEdit.getTags());
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RemarkCommand)) {
            return false;
        }

        // state check
        RemarkCommand r = (RemarkCommand) other;

        return index.equals(r.index) && remark.equals(r.remark);
    }

    /**
     * Generate a success message according to whether the remark is added or removed
     */
    private String generateSuccessMessage(Person personToEdit) {
        String message;
        if (remark.value.isEmpty()) {
            message = MESSAGE_DELETE_REMARK_SUCCESS;
        } else {
            message = MESSAGE_ADD_REMARK_SUCCESS;
        }
        return String.format(message, personToEdit);
    }
}
