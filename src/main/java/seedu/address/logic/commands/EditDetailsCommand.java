//@@author WoodyLau
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMPANY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPARTMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDUSTRY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEBSITE;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Contact;
import seedu.address.model.person.Lead;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditDetailsCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "editdetails";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the fine details of the Lead/Contact identified "
            + "by the index number used in the last Leads-Contacts listing. "
            + "Existing values will be overwritten by the input values.\n"
            + "For Leads, the parameters are: INDEX (must be a positive integer) "
            + "[" + PREFIX_COMPANY + "COMPANY] "
            + "[" + PREFIX_INDUSTRY + "INDUSTRY] "
            + "[" + PREFIX_RATING + "RATING (number from 1 to 5)] "
            + "[" + PREFIX_TITLE + "TITLE] "
            + "[" + PREFIX_WEBSITE + "WEBSITE]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_INDUSTRY + "Software Engineering "
            + PREFIX_RATING + "4\n"
            + "For Contacts, the parameters are: INDEX (must be a positive integer) "
            + "[" + PREFIX_COMPANY + "COMPANY] "
            + "[" + PREFIX_DEPARTMENT + "DEPARTMENT] "
            + "[" + PREFIX_TITLE + "TITLE]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_COMPANY + "Macrosoft "
            + PREFIX_TITLE + "Mr.";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Lead/Contact: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_NOT_EDITED_FOR_LEAD =
            "At least one field to edit for a Lead must be provided.";
    public static final String MESSAGE_NOT_EDITED_FOR_CONTACT =
            "At least one field to edit for a Contact must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This Lead/Contact already exists in the CRM Book.";

    private final Index index;
    private final EditLeadDescriptor editLeadDescriptor;
    private final EditContactDescriptor editContactDescriptor;

    private Person personToEdit;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editLeadDescriptor details for editing Leads
     * @param editContactDescriptor details for editing Contacts
     */
    public EditDetailsCommand(Index index,
                              EditLeadDescriptor editLeadDescriptor, EditContactDescriptor editContactDescriptor) {
        requireNonNull(index);
        CollectionUtil.isAnyNonNull(editLeadDescriptor, editContactDescriptor);

        this.index = index;
        this.editLeadDescriptor = new EditLeadDescriptor(editLeadDescriptor);
        this.editContactDescriptor = new EditContactDescriptor(editContactDescriptor);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        if (personToEdit instanceof Lead) {
            if (!editLeadDescriptor.isAnyFieldEdited()) {
                throw new CommandException(MESSAGE_NOT_EDITED_FOR_LEAD);
            }
            Lead editedPerson = (Lead) personToEdit;
            editedPerson.setCompany(editLeadDescriptor.getCompany().orElse(((Lead) personToEdit).getCompany()));
            editedPerson.setIndustry(editLeadDescriptor.getIndustry().orElse(((Lead) personToEdit).getIndustry()));
            editedPerson.setRating(editLeadDescriptor.getRating().orElse(((Lead) personToEdit).getRating()));
            editedPerson.setTitle(editLeadDescriptor.getTitle().orElse(((Lead) personToEdit).getTitle()));
            editedPerson.setWebsite(editLeadDescriptor.getWebsite().orElse(((Lead) personToEdit).getWebsite()));
            if (editLeadDescriptor.getRating().isPresent()
                    && (editLeadDescriptor.getRating().getAsInt() < 1
                    || editLeadDescriptor.getRating().getAsInt() > 5)) {
                throw new CommandException("Rating only goes from 1 to 5. Rating was not updated");
            }
            try {
                model.updatePerson(editedPerson, editedPerson);
            } catch (DuplicatePersonException dpe) {
                throw new CommandException(MESSAGE_DUPLICATE_PERSON);
            } catch (PersonNotFoundException pnfe) {
                throw new AssertionError("The target person cannot be missing");
            }
            return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, personToEdit));
        } else {
            if (!editContactDescriptor.isAnyFieldEdited()) {
                throw new CommandException(MESSAGE_NOT_EDITED_FOR_CONTACT);
            }
            Contact editedPerson = (Contact) personToEdit;
            editedPerson.setCompany(editContactDescriptor.getCompany().orElse(((Contact) personToEdit).getCompany()));
            editedPerson.setDepartment(editContactDescriptor.getDepartment()
                            .orElse(((Contact) personToEdit).getDepartment()));
            editedPerson.setTitle(editContactDescriptor.getTitle().orElse(((Contact) personToEdit).getTitle()));
            try {
                model.updatePerson(editedPerson, editedPerson);
            } catch (DuplicatePersonException dpe) {
                throw new CommandException(MESSAGE_DUPLICATE_PERSON);
            } catch (PersonNotFoundException pnfe) {
                throw new AssertionError("The target person cannot be missing");
            }
            return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, personToEdit));
        }
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        personToEdit = lastShownList.get(index.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditDetailsCommand)) {
            return false;
        }

        // state check
        EditDetailsCommand e = (EditDetailsCommand) other;
        return index.equals(e.index)
                && editLeadDescriptor.equals(e.editLeadDescriptor)
                && editContactDescriptor.equals(e.editContactDescriptor)
                && Objects.equals(personToEdit, e.personToEdit);
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditLeadDescriptor {
        private String company = null;
        private String industry = null;
        private OptionalInt rating = OptionalInt.empty();
        private String title = null;
        private String website = null;

        public EditLeadDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditLeadDescriptor(EditLeadDescriptor toCopy) {
            setCompany(toCopy.company);
            setIndustry(toCopy.industry);
            if (toCopy.rating != null && toCopy.rating.isPresent()) {
                setRating(toCopy.rating.getAsInt());
            }
            setTitle(toCopy.title);
            setWebsite(toCopy.website);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return (CollectionUtil.isAnyNonNull(this.company, this.industry, this.title, this.website)
                    || this.rating.isPresent());
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public Optional<String> getCompany() {
            return Optional.ofNullable(company);
        }

        public void setIndustry(String industry) {
            this.industry = industry;
        }

        public Optional<String> getIndustry() {
            return Optional.ofNullable(industry);
        }

        public void setRating(int rating) {
            this.rating = OptionalInt.of(rating);
        }

        public OptionalInt getRating() {
            return rating;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Optional<String> getTitle() {
            return Optional.ofNullable(title);
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public Optional<String> getWebsite() {
            return Optional.ofNullable(website);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditLeadDescriptor)) {
                return false;
            }

            // state check
            EditLeadDescriptor e = (EditLeadDescriptor) other;

            return getCompany().equals(e.getCompany())
                    && getIndustry().equals(e.getIndustry())
                    && getRating() == e.getRating()
                    && getTitle().equals(e.getTitle())
                    && getWebsite().equals(e.getWebsite());
        }
    }
    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditContactDescriptor {
        private String company = null;
        private String department = null;
        private String title = null;

        public EditContactDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditContactDescriptor(EditContactDescriptor toCopy) {
            setCompany(toCopy.company);
            setDepartment(toCopy.department);
            setTitle(toCopy.title);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.company, this.department, this.title);
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public Optional<String> getCompany() {
            return Optional.ofNullable(company);
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public Optional<String> getDepartment() {
            return Optional.ofNullable(department);
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Optional<String> getTitle() {
            return Optional.ofNullable(title);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditContactDescriptor)) {
                return false;
            }

            // state check
            EditContactDescriptor e = (EditContactDescriptor) other;

            return getCompany().equals(e.getCompany())
                    && getDepartment().equals(e.getDepartment())
                    && getTitle().equals(e.getTitle());
        }
    }
}
