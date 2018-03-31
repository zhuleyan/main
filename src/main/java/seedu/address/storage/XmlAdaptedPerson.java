package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Contact;
import seedu.address.model.person.Email;
import seedu.address.model.person.Lead;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Type;
import seedu.address.model.tag.Tag;

/**
 * JAXB-friendly version of the Person.
 */
public class XmlAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String phone;
    @XmlElement(required = true)
    private String email;
    @XmlElement(required = true)
    private String address;
    @XmlElement(required = true)
    private String remark;
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

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedPerson.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedPerson() {}

    /**
     * Constructs an {@code XmlAdaptedPerson} with the given person details.
     */
    public XmlAdaptedPerson(String name, String phone, String email, String address, List<XmlAdaptedTag> tagged) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.type = null;
        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
    }

    /**
     * Converts a given Person into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        remark = source.getRemark().value;
        type = source.getType().value;
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
        if (source instanceof Lead) {
            company = ((Lead) source).getCompany();
            industry = ((Lead) source).getIndustry();
            rating = ((Lead) source).getRating();
            title = ((Lead) source).getTitle();
            website = ((Lead) source).getWebsite();
        } else if (source instanceof Contact) {
            company = ((Contact) source).getCompany();
            department = ((Contact) source).getDepartment();
            title = ((Contact) source).getTitle();
            convertedDate = ((Contact) source).getConvertedDate();
        }
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
        return new Person(name, phone, email, address, remark, tags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedPerson)) {
            return false;
        }

        XmlAdaptedPerson otherPerson = (XmlAdaptedPerson) other;
        return Objects.equals(name, otherPerson.name)
                && Objects.equals(phone, otherPerson.phone)
                && Objects.equals(email, otherPerson.email)
                && Objects.equals(address, otherPerson.address)
                && tagged.equals(otherPerson.tagged);
    }
}
