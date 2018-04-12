//@@author WoodyLau
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
