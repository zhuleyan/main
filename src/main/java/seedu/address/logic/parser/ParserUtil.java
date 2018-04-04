package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.Theme;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 * {@code ParserUtil} contains methods that take in {@code Optional} as parameters. However, it goes against Java's
 * convention (see https://stackoverflow.com/a/39005452) as {@code Optional} should only be used a return type.
 * Justification: The methods in concern receive {@code Optional} return values from other methods as parameters and
 * return {@code Optional} values based on whether the parameters were present. Therefore, it is redundant to unwrap the
 * initial {@code Optional} before passing to {@code ParserUtil} as a parameter and then re-wrap it into an
 * {@code Optional} return value inside {@code ParserUtil} methods.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_INSUFFICIENT_PARTS = "Number of parts must be more than 1.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws IllegalValueException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws IllegalValueException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new IllegalValueException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws IllegalValueException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code Optional<String> name} into an {@code Optional<Name>} if {@code name} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Name> parseName(Optional<String> name) throws IllegalValueException {
        requireNonNull(name);
        return name.isPresent() ? Optional.of(parseName(name.get())) : Optional.empty();
    }

    //@@author A0155428B
    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code name} is invalid.
     */
    public static Theme parseTheme(String theme) throws IllegalValueException {
        requireNonNull(theme);
        String trimmedTheme = theme.trim();
        if (!Theme.isValidTheme(trimmedTheme)) {
            throw new IllegalValueException(Theme.MESSAGE_THEME_CONSTRAINTS);
        }
        return new Theme(trimmedTheme);
    }

    //@@author
    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws IllegalValueException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new IllegalValueException(Phone.MESSAGE_PHONE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code Optional<String> phone} into an {@code Optional<Phone>} if {@code phone} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Phone> parsePhone(Optional<String> phone) throws IllegalValueException {
        requireNonNull(phone);
        return phone.isPresent() ? Optional.of(parsePhone(phone.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws IllegalValueException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new IllegalValueException(Address.MESSAGE_ADDRESS_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code Optional<String> address} into an {@code Optional<Address>} if {@code address} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Address> parseAddress(Optional<String> address) throws IllegalValueException {
        requireNonNull(address);
        return address.isPresent() ? Optional.of(parseAddress(address.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws IllegalValueException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new IllegalValueException(Email.MESSAGE_EMAIL_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code Optional<String> email} into an {@code Optional<Email>} if {@code email} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Email> parseEmail(Optional<String> email) throws IllegalValueException {
        requireNonNull(email);
        return email.isPresent() ? Optional.of(parseEmail(email.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws IllegalValueException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new IllegalValueException(Tag.MESSAGE_TAG_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws IllegalValueException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    //@@author davidten
    /**
     * Parses a {@code String data}.
     * Leading and trailing whitespaces will be trimmed.
     * General String parser that can be used
     */
    public static String parseString(String data) {
        requireNonNull(data);
        return data.trim();
    }

    //@@author WoodyLau
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
    //@@author
}
