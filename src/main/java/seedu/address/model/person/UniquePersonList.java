package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * A list of persons that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Person#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniquePersonList implements Iterable<Person> {

    private final ObservableList<Person> internalList = FXCollections.observableArrayList();

    //@@author Sheikh-Umar
    /**
     * Returns true if the list contains either the phone number or email address
     * of the given argument.
     */
    public boolean contains(Person toCheck) {
        requireNonNull(toCheck);
        for (int i = 0; i < internalList.size(); i++) {
            Person current = internalList.get(i);
            if (current.getPhone().equals(toCheck.getPhone())
                    || current.getEmail().equals(toCheck.getEmail())) {
                return true;
            }
        }
        return false;
    }
    //@@author

    /**
     * Adds a person to the list.
     *
     * @throws DuplicatePersonException if the person to add is a duplicate of an existing person in the list.
     */
    public void add(Person toAdd) throws DuplicatePersonException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicatePersonException();
        }
        internalList.add(toAdd);
    }

    //@@author zhuleyan
    /**
     * Sorts all persons by name in alphabetical order.
     */
    public void sort() {
        internalList.sort(
            Comparator.comparing((
                Person p) -> p.getName().toString(), (
                s1, s2) -> (s1.compareToIgnoreCase(s2) == 0) ? s1.compareTo(s2) : s1.compareToIgnoreCase(s2))
        );
    }
    //@@author

    /**
     * Replaces the person {@code target} in the list with {@code editedPerson}.
     *
     * @throws DuplicatePersonException if the replacement is equivalent to another existing person in the list.
     * @throws PersonNotFoundException if {@code target} could not be found in the list.
     */
    public void setPerson(Person target, Person editedPerson)
            throws DuplicatePersonException, PersonNotFoundException {
        requireNonNull(editedPerson);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PersonNotFoundException();
        }

        if (!target.equals(editedPerson) && internalList.contains(editedPerson)) {
            throw new DuplicatePersonException();
        }

        internalList.set(index, editedPerson);
    }

    /**
     * Removes the equivalent person from the list.
     *
     * @throws PersonNotFoundException if no such person could be found in the list.
     */
    public boolean remove(Person toRemove) throws PersonNotFoundException {
        requireNonNull(toRemove);
        final boolean personFoundAndDeleted = internalList.remove(toRemove);
        if (!personFoundAndDeleted) {
            throw new PersonNotFoundException();
        }
        return personFoundAndDeleted;
    }

    public void setPersons(UniquePersonList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setPersons(List<Person> persons) throws DuplicatePersonException {
        requireAllNonNull(persons);
        final UniquePersonList replacement = new UniquePersonList();
        for (final Person person : persons) {
            replacement.add(person);
        }
        setPersons(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Person> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<Person> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniquePersonList // instanceof handles nulls
                        && this.internalList.equals(((UniquePersonList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
