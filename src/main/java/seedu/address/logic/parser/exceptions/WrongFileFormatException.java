package seedu.address.logic.parser.exceptions;

/**
 * Represents a wrong file format error encountered by a parser.
 */
public class WrongFileFormatException extends Exception {
    public WrongFileFormatException(String message) {
        super(message);
    }
}
