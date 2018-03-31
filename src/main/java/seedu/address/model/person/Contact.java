//@@author WoodyLau
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
