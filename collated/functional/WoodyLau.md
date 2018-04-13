# WoodyLau
###### /java/seedu/address/ui/PersonCard.java
``` java
    @FXML
    private Label company;
    @FXML
    private Label industry;
    @FXML
    private Label rating;
    @FXML
    private Label website;
    @FXML
    private Label department;
```
###### /java/seedu/address/ui/PersonCard.java
``` java
    private void setLead(Lead person) {
        department.setVisible(false);
        department.setManaged(false);

        if (person.getCompany() == null) {
            company.setText("Company: Not Given");
        } else {
            company.setText("Company: " + person.getCompany());
        }
        if (person.getIndustry() == null) {
            industry.setVisible(false);
            industry.setManaged(false);
        } else {
            industry.setText("Industry: " + person.getIndustry());
        }
        if (person.getRating() == 0) {
            rating.setText("Rating: Not Given");
        } else {
            rating.setText("Rating: " + person.getRating() + "/5");
        }
        if (person.getWebsite() == null) {
            website.setVisible(false);
            website.setManaged(false);
        } else {
            website.setText("Website: " + person.getWebsite());
        }
    }

    private void setContact(Contact person) {
        industry.setVisible(false);
        industry.setManaged(false);
        rating.setVisible(false);
        rating.setManaged(false);
        website.setVisible(false);
        website.setManaged(false);

        if (person.getCompany() == null) {
            company.setText("Company: Not Given");
        } else {
            company.setText("Company: " + person.getCompany());
        }
        if (person.getDepartment() == null) {
            department.setText("Department: Not Given");
        } else {
            department.setText("Department: " + person.getDepartment());
        }
    }

```
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
        try {
            ParserUtil.parseCompany(argContactMultimap.getValue(PREFIX_COMPANY))
                    .ifPresent(editContactDescriptor::setCompany);
            ParserUtil.parseDepartment(argContactMultimap.getValue(PREFIX_DEPARTMENT))
                    .ifPresent(editContactDescriptor::setDepartment);
            ParserUtil.parseTitle(argContactMultimap.getValue(PREFIX_TITLE)).ifPresent(editContactDescriptor::setTitle);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

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
###### /java/seedu/address/logic/parser/AccountCommandParser.java
``` java
package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ACCOUNT;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AccountCommand;
import seedu.address.logic.commands.AccountCommand.AccountDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class AccountCommandParser implements Parser<AccountCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditDetailsCommand
     * and returns an EditDetailsCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AccountCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ACCOUNT);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AccountCommand.MESSAGE_USAGE));
        }

        AccountDescriptor accountDescriptor = new AccountDescriptor();
        try {
            ParserUtil.parseCompany(argMultimap.getValue(PREFIX_ACCOUNT)).ifPresent(accountDescriptor::setAccount);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        return new AccountCommand(index, accountDescriptor);
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
###### /java/seedu/address/logic/parser/AddressBookParser.java
``` java
        case EditDetailsCommand.COMMAND_WORD:
        case EditDetailsCommand.COMMAND_ALIAS:
            return new EditDetailsCommandParser().parse(arguments);

        case ConvertCommand.COMMAND_WORD:
        case ConvertCommand.COMMAND_ALIAS:
            return new ConvertCommandParser().parse(arguments);

        case AccountCommand.COMMAND_WORD:
            return new AccountCommandParser().parse(arguments);
```
###### /java/seedu/address/logic/parser/ParserUtil.java
``` java
    /**
     * Parses a {@code String company}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static String parseCompany(String company) throws IllegalValueException {
        requireNonNull(company);
        if (!Account.isValidAccountName(company)) {
            throw new IllegalValueException(Account.MESSAGE_ACCOUNT_CONSTRAINTS);
        }
        return company.trim();
    }

    /**
     * Parses a {@code Optional<String> company} into an {@code Optional<String>} if {@code company} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<String> parseCompany(Optional<String> company) throws IllegalValueException {
        requireNonNull(company);
        return company.isPresent() ? Optional.of(parseCompany(company.get())) : Optional.empty();
    }

    /**
     * Parses an {@code String account}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static String parseAccount(String account) throws IllegalValueException {
        requireNonNull(account);
        if (!Account.isValidAccountName(account)) {
            throw new IllegalValueException(Account.MESSAGE_ACCOUNT_CONSTRAINTS);
        }
        return account.trim();
    }

    /**
     * Parses a {@code Optional<String> account} into an {@code Optional<String>} if {@code account} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<String> parseAccount(Optional<String> account) throws IllegalValueException {
        requireNonNull(account);
        return account.isPresent() ? Optional.of(parseAccount(account.get())) : Optional.empty();
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
    public static final Prefix PREFIX_ACCOUNT = new Prefix("an/");
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
import seedu.address.model.account.Account;
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
###### /java/seedu/address/logic/commands/ConvertCommand.java
``` java
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

        if (oldLead.getCompany() != null) {
            contact.setCompany(new Account(oldLead.getCompany()));
        }
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
import seedu.address.model.account.Account;
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
    public static final String COMMAND_ALIAS = "adddetails";

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
            editedPerson.setCompany(editContactDescriptor.getCompany()
                    .orElse(((Contact) personToEdit).getCompany()));
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
        private Account company = null;
        private String department = null;
        private String title = null;

        public EditContactDescriptor() {}

        /**
         * Copy constructor.
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
            this.company = new Account(company);
        }

        public void setCompany(Account company) {
            this.company = company;
        }

        public Optional<Account> getCompany() {
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
###### /java/seedu/address/logic/commands/AccountCommand.java
``` java
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ACCOUNT;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.account.Account;
import seedu.address.model.person.Contact;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Edits the details of an existing person in the address book.
 */
public class AccountCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "account";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an Account (company) to the Contact indicated "
            + "by the index number used in the last Leads-Contacts listing. "
            + "Existing Account will be overwritten by the input.\n"
            + "The parameters are: INDEX (must be a positive integer) "
            + PREFIX_ACCOUNT + "ACCOUNT...\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_ACCOUNT + "Macrosoft Inc";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Added Account to Contact: %1$s";
    public static final String MESSAGE_NOT_EDITED = "An Account must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This Contact already exists in the CRM Book.";
    public static final String MESSAGE_NOT_CONTACT = "Provided person is not a Contact.";

    private final Index index;
    private final AccountDescriptor accountDescriptor;

    private Person personToEdit;

    /**
     * @param index of the person in the filtered person list to edit
     * @param accountDescriptor details for editing Contacts
     */
    public AccountCommand(Index index,
                          AccountDescriptor accountDescriptor) {
        requireNonNull(index);
        CollectionUtil.isAnyNonNull(accountDescriptor);

        this.index = index;
        this.accountDescriptor = new AccountDescriptor(accountDescriptor);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        if (!accountDescriptor.isAnyFieldEdited()) {
            throw new CommandException(MESSAGE_NOT_EDITED);
        }
        if (!(personToEdit instanceof Contact)) {
            throw new CommandException(MESSAGE_NOT_CONTACT);
        }
        Contact editedPerson = (Contact) personToEdit;
        editedPerson.setCompany(accountDescriptor.getAccount());
        try {
            model.updatePerson(editedPerson, editedPerson);
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, personToEdit));

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
        if (!(other instanceof AccountCommand)) {
            return false;
        }

        // state check
        AccountCommand e = (AccountCommand) other;
        return index.equals(e.index)
                && accountDescriptor.equals(e.accountDescriptor)
                && Objects.equals(personToEdit, e.personToEdit);
    }

    /**
     * Stores the Account to edit the person with. It will replace the existing Account of the person.
     */
    public static class AccountDescriptor {
        private Account account = null;

        public AccountDescriptor() {}

        /**
         * Copy constructor.
         */
        public AccountDescriptor(AccountDescriptor toCopy) {
            setAccount(toCopy.account);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.account);
        }

        public void setAccount(String account) {
            this.account = new Account(account);
        }

        public void setAccount(Account account) {
            this.account = account;
        }

        public Account getAccount() {
            return account;
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof AccountDescriptor)) {
                return false;
            }

            // state check
            AccountDescriptor e = (AccountDescriptor) other;

            return getAccount().equals(e.getAccount());
        }
    }
}
```
###### /java/seedu/address/storage/XmlAdaptedAccount.java
``` java
package seedu.address.storage;

import javax.xml.bind.annotation.XmlValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.account.Account;

/**
 * JAXB-friendly adapted version of the Account.
 */
public class XmlAdaptedAccount {

    @XmlValue
    private String accountName;

    /**
     * Constructs an XmlAdaptedAccount.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedAccount() {}

    /**
     * Constructs a {@code XmlAdaptedAccount} with the given {@code accountName}.
     */
    public XmlAdaptedAccount(String accountName) {
        this.accountName = accountName;
    }

    /**
     * Converts a given Account into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created
     */
    public XmlAdaptedAccount(Account source) {
        accountName = source.accountName;
    }

    /**
     * Converts this jaxb-friendly adapted account object into the model's Account object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Account toModelType() throws IllegalValueException {
        if (!Account.isValidAccountName(accountName)) {
            throw new IllegalValueException(Account.MESSAGE_ACCOUNT_CONSTRAINTS);
        }
        return new Account(accountName);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedAccount)) {
            return false;
        }

        return accountName.equals(((XmlAdaptedAccount) other).accountName);
    }
}
```
###### /java/seedu/address/storage/XmlAdaptedPerson.java
``` java
    @XmlElement(required = true)
    private String type;
    @XmlElement(required = false)
    private String company;
    @XmlElement(required = false)
    private String title;
    // Fields included for Leads
    @XmlElement(required = false)
    private String industry;
    @XmlElement(required = false)
    private int rating;
    @XmlElement(required = false)
    private String website;
    // Fields included for Contacts
    @XmlElement(required = false)
    private String department;
    @XmlElement(required = false)
    private String convertedDate;
```
###### /java/seedu/address/storage/XmlAdaptedPerson.java
``` java
        if (source instanceof Lead) {
            company = ((Lead) source).getCompany();
            industry = ((Lead) source).getIndustry();
            rating = ((Lead) source).getRating();
            title = ((Lead) source).getTitle();
            website = ((Lead) source).getWebsite();
        } else if (source instanceof Contact) {
            if (((Contact) source).getCompany() != null) {
                company = ((Contact) source).getCompany().toString();
            }
            department = ((Contact) source).getDepartment();
            title = ((Contact) source).getTitle();
            convertedDate = ((Contact) source).getConvertedDate();
        }
```
###### /java/seedu/address/storage/XmlAdaptedPerson.java
``` java
    }

    /**
     * Converts this jaxb-friendly adapted person object into the model's Person object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }

        if (this.name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(this.name)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        final Name name = new Name(this.name);

        if (this.phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(this.phone)) {
            throw new IllegalValueException(Phone.MESSAGE_PHONE_CONSTRAINTS);
        }
        final Phone phone = new Phone(this.phone);

        if (this.email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(this.email)) {
            throw new IllegalValueException(Email.MESSAGE_EMAIL_CONSTRAINTS);
        }
        final Email email = new Email(this.email);

        if (this.address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(this.address)) {
            throw new IllegalValueException(Address.MESSAGE_ADDRESS_CONSTRAINTS);
        }
        final Address address = new Address(this.address);

        final Remark remark = new Remark(this.remark);

        if (this.type == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Type.class.getSimpleName()));
        }
        if (!Type.isValidType(this.type)) {
            throw new IllegalValueException(Type.MESSAGE_ADDRESS_CONSTRAINTS);
        }
        final Type type = new Type(this.type);

        final Set<Tag> tags = new HashSet<>(personTags);
```
###### /java/seedu/address/storage/XmlAdaptedPerson.java
``` java
        if (type.value.equals("Lead")) {
            Lead lead = new Lead(name, phone, email, address, remark, tags);
            if (this.company != null) {
                lead.setCompany(this.company);
            }
            if (this.industry != null) {
                lead.setIndustry(this.industry);
            }
            lead.setRating(this.rating);
            if (this.title != null) {
                lead.setTitle(this.title);
            }
            if (this.website != null) {
                lead.setWebsite(this.website);
            }
            return lead;
        }
        if (type.value.equals("Contact")) {
            Contact contact = new Contact(name, phone, email, address, remark, tags);
            if (this.company != null) {
                contact.setCompany(this.company);
            }
            if (this.department != null) {
                contact.setDepartment(this.department);
            }
            if (this.title != null) {
                contact.setTitle(this.title);
            }
            if (this.convertedDate != null) {
                contact.setConvertedDate(this.convertedDate);
            }
            return contact;
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

import seedu.address.model.account.Account;
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

    private Account company = null;
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

    public void setCompany(Account newCompany) {
        this.company = newCompany;
    }

    public void setCompany(String newCompany) {
        this.company = new Account(newCompany);
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

    public Account getCompany() {
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
###### /java/seedu/address/model/AddressBook.java
``` java
    /**
     *  Updates the master account list to include account in {@code person} that is not in the list.
     *  Updates the person to point to an Account object in the master list.
     */
    private void syncWithMasterAccountList(Contact person) {
        final UniqueAccountList personAccounts = new UniqueAccountList(person.getCompany());
        accounts.mergeFrom(personAccounts);

        // Create map with values = tag object references in the master list
        // used for checking person tag references
        final Map<Account, Account> masterAccountObjects = new HashMap<>();
        accounts.forEach(account -> masterAccountObjects.put(account, account));

        // Rebuild the list of person tags to point to the relevant tags in the master tag list.
        final Account correctAccountReferences = masterAccountObjects.get(person.getCompany());
        person.setCompany(correctAccountReferences);
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
###### /java/seedu/address/model/account/UniqueAccountList.java
``` java
package seedu.address.model.account;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import seedu.address.commons.exceptions.DuplicateDataException;
import seedu.address.commons.util.CollectionUtil;

/**
 * A list of accounts that enforces no nulls and uniqueness between its elements.
 *
 * Supports minimal set of list operations for the app's features.
 *
 * @see Account#equals(Object)
 */
public class UniqueAccountList implements Iterable<Account> {

    private final ObservableList<Account> internalList = FXCollections.observableArrayList();

    /**
     * Constructs empty AccountList.
     */
    public UniqueAccountList() {}

    /**
     * Creates a UniqueAccountList using given account.
     * Enforces no nulls.
     */
    public UniqueAccountList(Set<Account> accounts) {
        requireAllNonNull(accounts);
        internalList.addAll(accounts);

        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Creates a UniqueAccountList using given account.
     * Enforces no nulls.
     */
    public UniqueAccountList(Account account) {
        requireNonNull(account);
        internalList.add(account);

        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Returns all accounts in this list as a Set.
     * This set is mutable and change-insulated against the internal list.
     */
    public Set<Account> toSet() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return new HashSet<>(internalList);
    }

    /**
     * Replaces the Accounts in this list with those in the argument account list.
     */
    public void setAccounts(Set<Account> accounts) {
        requireAllNonNull(accounts);
        internalList.setAll(accounts);
        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Ensures every account in the argument list exists in this object.
     */
    public void mergeFrom(UniqueAccountList from) {
        final Set<Account> alreadyInside = this.toSet();
        from.internalList.stream()
                .filter(account -> !alreadyInside.contains(account))
                .forEach(internalList::add);

        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Returns true if the list contains an equivalent Account as the given argument.
     */
    public boolean contains(Account toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a Account to the list.
     *
     * @throws DuplicateAccountException if the Account to add is a duplicate of an existing Account in the list.
     */
    public void add(Account toAdd) throws DuplicateAccountException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateAccountException();
        }
        internalList.add(toAdd);

        assert CollectionUtil.elementsAreUnique(internalList);
    }

    @Override
    public Iterator<Account> iterator() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return internalList.iterator();
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Account> asObservableList() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public boolean equals(Object other) {
        assert CollectionUtil.elementsAreUnique(internalList);
        return other == this // short circuit if same object
                || (other instanceof UniqueAccountList // instanceof handles nulls
                        && this.internalList.equals(((UniqueAccountList) other).internalList));
    }

    /**
     * Returns true if the element in this list is equal to the elements in {@code other}.
     * The elements do not have to be in the same order.
     */
    public boolean equalsOrderInsensitive(UniqueAccountList other) {
        assert CollectionUtil.elementsAreUnique(internalList);
        assert CollectionUtil.elementsAreUnique(other.internalList);
        return this == other || new HashSet<>(this.internalList).equals(new HashSet<>(other.internalList));
    }

    @Override
    public int hashCode() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return internalList.hashCode();
    }

    /**
     * Signals that an operation would have violated the 'no duplicates' property of the list.
     */
    public static class DuplicateAccountException extends DuplicateDataException {
        protected DuplicateAccountException() {
            super("Operation would result in duplicate accounts");
        }
    }

}
```
###### /java/seedu/address/model/account/Account.java
``` java
package seedu.address.model.account;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents an Account in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidAccountName(String)}
 */
public class Account {

    public static final String MESSAGE_ACCOUNT_CONSTRAINTS = "Account names should be alphanumeric";
    public static final String ACCOUNT_VALIDATION_REGEX = "[\\p{Alnum} ]+";

    public final String accountName;

    /**
     * Constructs a {@code Tag}.
     *
     * @param accountName A valid tag name.
     */
    public Account(String accountName) {
        requireNonNull(accountName);
        checkArgument(isValidAccountName(accountName), MESSAGE_ACCOUNT_CONSTRAINTS);
        this.accountName = accountName;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidAccountName(String test) {
        return test.matches(ACCOUNT_VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Account // instanceof handles nulls
                && this.accountName.equals(((Account) other).accountName)); // state check
    }

    @Override
    public int hashCode() {
        return accountName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return accountName;
    }

}
```
