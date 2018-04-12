//@@author Sheikh-Umar
package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class TypeMatchesKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public TypeMatchesKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getType().value, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TypeMatchesKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((TypeMatchesKeywordsPredicate) other).keywords)); // state check
    }

}
//@@author
