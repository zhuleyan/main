package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Lead;
import seedu.address.model.person.exceptions.DuplicatePersonException;

//@@author zhuleyan
/**
 * Import many persons to the address book at one time.
 */
public class ImportCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "import";
    public static final String COMMAND_ALIAS = "i";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Imports a CSV file to the CRM Book. "
            + "Parameters: PATH\n"
            + "Example: " + COMMAND_WORD + " ./sample.csv";

    public static final String MESSAGE_SUCCESS = "CSV file imported";

    private final List<Lead> toAdd;

    /**
     * Creates an ImportCommand to add the specified {@code Person}
     */
    public ImportCommand(List<Lead> person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            for (Lead lead:toAdd) {
                model.addPerson(lead);
            }
        } catch (DuplicatePersonException e) {
            //do nothing
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ImportCommand // instanceof handles nulls
                && toAdd.equals(((ImportCommand) other).toAdd));
    }
}
