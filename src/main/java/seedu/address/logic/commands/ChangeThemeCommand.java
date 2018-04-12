package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ChangeThemeRequestEvent;
import seedu.address.model.Theme;

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
            "Theme has been changed to %1$s.";
    public static final String MESSAGE_CHANGE_THEME_FAIL = "Current theme is %1$s.";
    private final Theme targetTheme;

    public ChangeThemeCommand(Theme targetTheme) {
        this.targetTheme = targetTheme;
    }

    @Override
    public CommandResult execute() {
        requireNonNull(targetTheme);
        if (isCurrentThemeEqualToTargetTheme()) {
            return new CommandResult(String.format(MESSAGE_CHANGE_THEME_FAIL, targetTheme));
        }
        EventsCenter.getInstance().post(new ChangeThemeRequestEvent(targetTheme.toString()));
        return new CommandResult(String.format(MESSAGE_CHANGE_THEME_SUCCESS, targetTheme));
    }

    private boolean isCurrentThemeEqualToTargetTheme() {
        return targetTheme.convertThemeToFilePath().equals(model.getThemeFilePath());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ChangeThemeCommand // instanceof handles nulls
                && this.targetTheme.equals(((ChangeThemeCommand) other).targetTheme)); // state check
    }
}
