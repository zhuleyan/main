//@@author WoodyLau
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Contact;
import seedu.address.model.person.Email;
import seedu.address.model.person.Lead;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;

/**
 * Converts an existing Lead in the address book to a Contact.
 */
public class ConvertCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "convert";
    //@@author Sheikh-Umar
    public static final String COMMAND_ALIAS = "con";
    //@@author

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Converts the selected Lead to a Contact "
            + "by the index number used in the last person listing. "
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 ";

    public static final String MESSAGE_CONVERT_PERSON_SUCCESS = "Converted Person: %1$s";
    public static final String MESSAGE_NOT_CONVERTED = "Person is already a Contact.";
    //@@author Sheikh-Umar
    public static final String MESSAGE_DUPLICATE_PERSON = "This Lead/Contact already exists in the CRM Book.";
    //@@author

    private final Index index;

    private Lead oldLead;
    private Contact newContact;

    /**
     * @param index of the person in the filtered person list to edit
     */
    public ConvertCommand(Index index) {
        requireNonNull(index);

        this.index = index;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.convertPerson(oldLead, newContact);
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_CONVERT_PERSON_SUCCESS, oldLead));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        try {
            oldLead = (Lead) lastShownList.get(index.getZeroBased());
        } catch (ClassCastException cce) {
            throw new CommandException(MESSAGE_NOT_CONVERTED);
        }
        newContact = createContact(oldLead);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Contact createContact(Lead oldLead) {
        assert oldLead != null;

        Name updatedName = oldLead.getName();
        Phone updatedPhone = oldLead.getPhone();
        Email updatedEmail = oldLead.getEmail();
        Address updatedAddress = oldLead.getAddress();
        Remark updatedRemark = oldLead.getRemark();
        Set<Tag> updatedTags = oldLead.getTags();

        Contact contact = new Contact(updatedName, updatedPhone,
                updatedEmail, updatedAddress, updatedRemark, updatedTags);

        contact.setCompany(oldLead.getCompany());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        contact.setConvertedDate(dateFormat.format(date));

        return contact;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ConvertCommand)) {
            return false;
        }

        // state check
        ConvertCommand e = (ConvertCommand) other;
        return index.equals(e.index)
                && Objects.equals(oldLead, e.oldLead);
    }

}
