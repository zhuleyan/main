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

    public static final String DEFAULT_THEME_FILE_PATH = "/view/BlueTheme.css";
    public static final String DARK_THEME_FILE_PATH = "/view/DarkTheme.css";
    public static final String LIGHT_THEME_FILE_PATH = "/view/LightTheme.css";
    public static final String BLUE_THEME_FILE_PATH = "/view/BlueTheme.css";
    public static final String LIGHT_THEME = "light";
    public static final String DARK_THEME = "dark";
    public static final String BLUE_THEME = "blue";

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
        if (test.equals(LIGHT_THEME) || test.equals(DARK_THEME) || test.equals(BLUE_THEME)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Converts theme string to file path
     */
    public String convertThemeToFilePath() {
        switch (this.theme) {
        case DARK_THEME:
            return DARK_THEME_FILE_PATH;
        case LIGHT_THEME:
            return LIGHT_THEME_FILE_PATH;
        case BLUE_THEME:
            return BLUE_THEME_FILE_PATH;
        default:
            return DEFAULT_THEME_FILE_PATH;
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
