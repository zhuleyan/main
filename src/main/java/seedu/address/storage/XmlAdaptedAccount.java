//@@author WoodyLau
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
