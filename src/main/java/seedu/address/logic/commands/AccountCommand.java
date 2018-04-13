//@@author WoodyLau
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
