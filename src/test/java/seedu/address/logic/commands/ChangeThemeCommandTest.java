package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.Theme;
import seedu.address.model.UserPrefs;
import seedu.address.model.exception.InputThemeEqualsCurrentThemeException;


public class ChangeThemeCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validTheme_success() throws InputThemeEqualsCurrentThemeException {
        Theme theme = new Theme("light");
        ChangeThemeCommand changeThemeCommand = prepareCommand(theme);

        String expectedMessage = String.format(ChangeThemeCommand.MESSAGE_CHANGE_THEME_SUCCESS, theme);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateTheme("light");

        assertCommandSuccess(changeThemeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_currentThemeEqualsInputTheme_failure() {
        Theme theme = new Theme("dark");
        ChangeThemeCommand changeThemeCommand = prepareCommand(theme);
        String expectedMessage = String.format(ChangeThemeCommand.MESSAGE_CHANGE_THEME_FAIL, "dark");
        assertCommandSuccess(changeThemeCommand, model, expectedMessage, model);
    }

    /**
     * Returns a {@code ChangeThemeCommand} with the parameter {@code theme}.
     */
    private ChangeThemeCommand prepareCommand(Theme theme) {
        ChangeThemeCommand changeThemeCommand = new ChangeThemeCommand(theme);
        changeThemeCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return changeThemeCommand;
    }
}
