package seedu.address.ui;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.Theme;

//@@author A0155428B
public class GuiThemeSettingTest {
    private GuiSettings guiSettings;

    @Test
    public void display() {
        guiSettings = new GuiSettings(600.0, 740.0, 0,
                0, Theme.DEFAULT_THEME_FILE_PATH);
        assertEquals(guiSettings.getThemeFilePath(), Theme.BLUE_THEME_FILE_PATH);
    }
}
