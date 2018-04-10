package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Theme;
import seedu.address.model.exception.InputThemeEqualsCurrentThemeException;

//@@author A0155428B
/**
 * Changes theme of CRM Book to the specified theme.
 */
public class ChangeThemeCommand extends Command {

    public static final String COMMAND_WORD = "changetheme";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Changes to the specified theme.\n"
            + "Example: " + COMMAND_WORD + " light";

    public static final String MESSAGE_CHANGE_THEME_SUCCESS =
            "Theme changed to %1$s. Please restart to effect the change.";
    public static final String MESSAGE_CHANGE_THEME_FAIL = "Current theme is %1$s.";
    private final Theme targetTheme;

    public ChangeThemeCommand(Theme targetTheme) {
        this.targetTheme = targetTheme;
    }

    @Override
    public CommandResult execute() {
        requireNonNull(targetTheme);
        try {
            model.updateTheme(targetTheme.theme);
        } catch (InputThemeEqualsCurrentThemeException e) {
            return new CommandResult(String.format(MESSAGE_CHANGE_THEME_FAIL, targetTheme));
        }
        return new CommandResult(String.format(MESSAGE_CHANGE_THEME_SUCCESS, targetTheme));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ChangeThemeCommand // instanceof handles nulls
                && this.targetTheme.equals(((ChangeThemeCommand) other).targetTheme)); // state check
    }
}
