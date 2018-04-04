package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

//@@author A0155428B
/**
 * Represents a theme in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTheme(String)}
 */
public class Theme {

    public static final String MESSAGE_THEME_CONSTRAINTS =
            "Theme should be either light or dark";

    public final String theme;

    /**
     * Constructs a {@code Theme}.
     *
     * @param theme A valid theme.
     */
    public Theme(String theme) {
        requireNonNull(theme);
        checkArgument(isValidTheme(theme), MESSAGE_THEME_CONSTRAINTS);
        this.theme = theme;
    }

    /**
     * Returns true if a given string is a valid theme.
     */
    public static boolean isValidTheme(String test) {
        if (test.equals("light") || test.equals("dark")) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public String toString() {
        return theme;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Theme // instanceof handles nulls
                && this.theme.equals(((Theme) other).theme)); // state check
    }

    @Override
    public int hashCode() {
        return theme.hashCode();
    }

}
