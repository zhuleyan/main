package seedu.address.commons.core;

import java.awt.Point;
import java.io.Serializable;
import java.util.Objects;

import seedu.address.model.Theme;

/**
 * A Serializable class that contains the GUI settings.
 */
public class GuiSettings implements Serializable {

    private static final double DEFAULT_HEIGHT = 600;
    private static final double DEFAULT_WIDTH = 740;

    private Double windowWidth;
    private Double windowHeight;
    private Point windowCoordinates;
    private String themeFilePath;

    public GuiSettings() {
        this.windowWidth = DEFAULT_WIDTH;
        this.windowHeight = DEFAULT_HEIGHT;
        this.windowCoordinates = null; // null represent no coordinates
        this.themeFilePath = Theme.DEFAULT_THEME_FILE_PATH;
    }

    public GuiSettings(Double windowWidth, Double windowHeight, int xPosition, int yPosition) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.windowCoordinates = new Point(xPosition, yPosition);
        this.themeFilePath = Theme.DEFAULT_THEME_FILE_PATH;
    }

    public GuiSettings(Double windowWidth, Double windowHeight, int xPosition, int yPosition, String themeFilePath) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.windowCoordinates = new Point(xPosition, yPosition);
        this.themeFilePath = themeFilePath;
    }

    public Double getWindowWidth() {
        return windowWidth;
    }

    public Double getWindowHeight() {
        return windowHeight;
    }

    public Point getWindowCoordinates() {
        return windowCoordinates;
    }

    public String getThemeFilePath() {
        return themeFilePath;
    }

    public void setThemeFilePath(String theme) {
        this.themeFilePath = theme;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuiSettings)) { //this handles null as well.
            return false;
        }

        GuiSettings o = (GuiSettings) other;

        return Objects.equals(windowWidth, o.windowWidth)
                && Objects.equals(windowHeight, o.windowHeight)
                && Objects.equals(windowCoordinates.x, o.windowCoordinates.x)
                && Objects.equals(windowCoordinates.y, o.windowCoordinates.y)
                && themeFilePath.equals(o.themeFilePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(windowWidth, windowHeight, windowCoordinates, themeFilePath);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Width : " + windowWidth + "\n");
        sb.append("Height : " + windowHeight + "\n");
        sb.append("Position : " + windowCoordinates);
        sb.append("ThemeFilePath : " + themeFilePath);
        return sb.toString();
    }
}
