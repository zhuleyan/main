# WoodyLau
###### /java/seedu/address/logic/parser/EditDetailsCommandParser.java
``` java
package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMPANY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPARTMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDUSTRY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEBSITE;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.EditDetailsCommand;
import seedu.address.logic.commands.EditDetailsCommand.EditContactDescriptor;
import seedu.address.logic.commands.EditDetailsCommand.EditLeadDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditDetailsCommandParser implements Parser<EditDetailsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditDetailsCommand
     * and returns an EditDetailsCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditDetailsCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argLeadMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_COMPANY, PREFIX_INDUSTRY, PREFIX_RATING, PREFIX_TITLE, PREFIX_WEBSITE);
        ArgumentMultimap argContactMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_COMPANY, PREFIX_DEPARTMENT, PREFIX_TITLE);

        Index index;

        try {
            index = ParserUtil.parseIndex(argLeadMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            try {
                index = ParserUtil.parseIndex(argContactMultimap.getPreamble());
            } catch (IllegalValueException ive2) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        EditDetailsCommand.MESSAGE_USAGE));
            }
        }

        EditLeadDescriptor editLeadDescriptor = new EditLeadDescriptor();
        EditContactDescriptor editContactDescriptor = new EditContactDescriptor();
        try {
            ParserUtil.parseCompany(argLeadMultimap.getValue(PREFIX_COMPANY)).ifPresent(editLeadDescriptor::setCompany);
            ParserUtil.parseIndustry(argLeadMultimap.getValue(PREFIX_INDUSTRY))
                    .ifPresent(editLeadDescriptor::setIndustry);
            ParserUtil.parseRating(argLeadMultimap.getValue(PREFIX_RATING)).ifPresent(editLeadDescriptor::setRating);
            ParserUtil.parseTitle(argLeadMultimap.getValue(PREFIX_TITLE)).ifPresent(editLeadDescriptor::setTitle);
            ParserUtil.parseWebsite(argLeadMultimap.getValue(PREFIX_WEBSITE)).ifPresent(editLeadDescriptor::setWebsite);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
        ParserUtil.parseCompany(argContactMultimap.getValue(PREFIX_COMPANY))
                .ifPresent(editContactDescriptor::setCompany);
        ParserUtil.parseDepartment(argContactMultimap.getValue(PREFIX_DEPARTMENT))
                .ifPresent(editContactDescriptor::setDepartment);
        ParserUtil.parseTitle(argContactMultimap.getValue(PREFIX_TITLE)).ifPresent(editContactDescriptor::setTitle);

        if (!editLeadDescriptor.isAnyFieldEdited() && !editContactDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditDetailsCommand.MESSAGE_NOT_EDITED);
        }

        return new EditDetailsCommand(index, editLeadDescriptor, editContactDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws IllegalValueException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
```
###### /java/seedu/address/logic/parser/ParserUtil.java
``` java
    /**
     * Parses a {@code String company}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static String parseCompany(String company) {
        requireNonNull(company);
        return company.trim();
    }

    /**
     * Parses a {@code Optional<String> company} into an {@code Optional<String>} if {@code company} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<String> parseCompany(Optional<String> company) {
        requireNonNull(company);
        return company.isPresent() ? Optional.of(parseCompany(company.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String industry}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static String parseIndustry(String industry) {
        requireNonNull(industry);
        return industry.trim();
    }

    /**
     * Parses a {@code Optional<String> industry} into an {@code Optional<String>} if {@code industry} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<String> parseIndustry(Optional<String> industry) {
        requireNonNull(industry);
        return industry.isPresent() ? Optional.of(parseIndustry(industry.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String rating}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static int parseRating(String rating) throws IllegalValueException {
        requireNonNull(rating);
        int intRating = 0;
        try {
            intRating = Integer.parseInt(rating);
        } catch (NumberFormatException nfe) {
            throw new IllegalValueException("Rating was not an integer");
        }
        return intRating;
    }

    /**
     * Parses a {@code Optional<String> rating} into an {@code OptionalInt} if {@code rating} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static OptionalInt parseRating(Optional<String> rating) throws IllegalValueException {
        requireNonNull(rating);
        return rating.isPresent() ? OptionalInt.of(parseRating(rating.get())) : OptionalInt.empty();
    }

    /**
     * Parses a {@code String title}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static String parseTitle(String title) {
        requireNonNull(title);
        return title.trim();
    }

    /**
     * Parses a {@code Optional<String> title} into an {@code Optional<String>} if {@code title} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<String> parseTitle(Optional<String> title) {
        requireNonNull(title);
        return title.isPresent() ? Optional.of(parseTitle(title.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String website}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static String parseWebsite(String website) {
        requireNonNull(website);
        return website.trim();
    }

    /**
     * Parses a {@code Optional<String> website} into an {@code Optional<String>} if {@code website} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<String> parseWebsite(Optional<String> website) {
        requireNonNull(website);
        return website.isPresent() ? Optional.of(parseWebsite(website.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String department}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static String parseDepartment(String department) {
        requireNonNull(department);
        return department.trim();
    }

    /**
     * Parses a {@code Optional<String> department} into an {@code Optional<String>} if {@code department} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<String> parseDepartment(Optional<String> department) {
        requireNonNull(department);
        return department.isPresent() ? Optional.of(parseDepartment(department.get())) : Optional.empty();
    }
```
###### /java/seedu/address/logic/parser/CliSyntax.java
``` java
    public static final Prefix PREFIX_COMPANY = new Prefix("c/");
    public static final Prefix PREFIX_INDUSTRY = new Prefix("i/");
    public static final Prefix PREFIX_RATING = new Prefix("r/");
    public static final Prefix PREFIX_TITLE = new Prefix("t/");
    public static final Prefix PREFIX_WEBSITE = new Prefix("w/");
    public static final Prefix PREFIX_DEPARTMENT = new Prefix("d/");
```
###### /java/seedu/address/logic/parser/ConvertCommandParser.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.ConvertCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ConvertCommand object
 */
public class ConvertCommandParser implements Parser<ConvertCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ConvertCommand
     * and returns an ConvertCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */

    public ConvertCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new ConvertCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ConvertCommand.MESSAGE_USAGE));
        }
    }

}
```
###### /java/seedu/address/logic/commands/ConvertCommand.java
``` java
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
```
###### /java/seedu/address/logic/commands/EditDetailsCommand.java
``` java
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
```
###### /java/seedu/address/model/person/Type.java
``` java
package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's Type in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidType(String)}
 */
public class Type {

    public static final String MESSAGE_ADDRESS_CONSTRAINTS =
            "Types can only be Lead or Contact";

    /*
     * Can only either be Lead or Contact.
     */
    public static final String TYPE_VALIDATION_REGEX = "Contact|Lead";

    public final String value;

    /**
     * Constructs a {@code Type}.
     *
     * @param type A valid type.
     */
    public Type(String type) {
        requireNonNull(type);
        checkArgument(isValidType(type), MESSAGE_ADDRESS_CONSTRAINTS);
        this.value = type;
    }

    /**
     * Returns true if a given string is a valid type.
     */
    public static boolean isValidType(String test) {
        return test.matches(TYPE_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Type // instanceof handles nulls
                && this.value.equals(((Type) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
```
###### /java/seedu/address/model/person/Contact.java
``` java
package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Contact extends Person {

    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Address address;
    private final Remark remark;
    private final Type type;

    private final UniqueTagList tags;

    private String company = null;
    private String department = null;
    private String title = null;
    private String convertedDate = null;

    /**
     * Every field must be present and not null.
     */
    public Contact(Name name, Phone phone, Email email, Address address, Remark remark, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.remark = remark;
        this.type = new Type("Contact");
        // protect internal tags from changes in the arg list
        this.tags = new UniqueTagList(tags);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Remark getRemark() {
        return remark;
    }

    public Type getType() {
        return type;
    }

    public void setCompany(String newCompany) {
        this.company = newCompany;
    }

    public void setDepartment(String newDepartment) {
        this.department = newDepartment;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public void setConvertedDate(String newConvertedDate) {
        this.convertedDate = newConvertedDate;
    }

    public String getCompany() {
        return this.company;
    }

    public String getDepartment() {
        return this.department;
    }

    public String getTitle() {
        return this.title;
    }

    public String getConvertedDate() {
        return this.convertedDate;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags.toSet());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Contact)) {
            return false;
        }

        Contact otherPerson = (Contact) other;
        return otherPerson.getName().equals(this.getName())
                && otherPerson.getPhone().equals(this.getPhone())
                && otherPerson.getEmail().equals(this.getEmail())
                && otherPerson.getAddress().equals(this.getAddress());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Type: ")
                .append(getType())
                .append(" Phone: ")
                .append(getPhone())
                .append(" Email: ")
                .append(getEmail())
                .append(" Address: ")
                .append(getAddress())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}
```
###### /java/seedu/address/model/person/Lead.java
``` java
package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Lead extends Person {

    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Address address;
    private final Remark remark;
    private final Type type;

    private final UniqueTagList tags;

    private String company = null;
    private String industry = null;
    private int rating = 0;
    private String title = null;
    private String website = null;

    /**
     * Every field must be present and not null.
     */
    public Lead(Name name, Phone phone, Email email, Address address, Remark remark, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.remark = remark;
        this.type = new Type("Lead");
        // protect internal tags from changes in the arg list
        this.tags = new UniqueTagList(tags);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Remark getRemark() {
        return remark;
    }

    public Type getType() {
        return type;
    }

    public void setCompany(String newCompany) {
        this.company = newCompany;
    }

    public void setIndustry(String newIndustry) {
        this.industry = newIndustry;
    }

    public void setRating(int newRating) {
        if (newRating > 0 && newRating < 6) {
            this.rating = newRating;
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCompany() {
        return this.company;
    }

    public String getIndustry() {
        return this.industry;
    }

    public int getRating() {
        return this.rating;
    }

    public String getTitle() {
        return this.title;
    }

    public String getWebsite() {
        return this.website;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags.toSet());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Lead)) {
            return false;
        }

        Lead otherPerson = (Lead) other;
        return otherPerson.getName().equals(this.getName())
                && otherPerson.getPhone().equals(this.getPhone())
                && otherPerson.getEmail().equals(this.getEmail())
                && otherPerson.getAddress().equals(this.getAddress());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Type: ")
                .append(getType())
                .append(" Phone: ")
                .append(getPhone())
                .append(" Email: ")
                .append(getEmail())
                .append(" Address: ")
                .append(getAddress())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }
}
```
###### /java/seedu/address/model/ModelManager.java
``` java
    @Override
    public void convertPerson(Lead lead, Contact contact)
            throws DuplicatePersonException, PersonNotFoundException {
        requireAllNonNull(lead, contact);

        addressBook.convertPerson(lead, contact);
        indicateAddressBookChanged();
    }
```
