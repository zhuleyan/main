//@@author WoodyLau
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
