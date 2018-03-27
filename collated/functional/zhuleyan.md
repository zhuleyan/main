# zhuleyan
###### /java/seedu/address/logic/parser/RemarkCommandParser.java
``` java
/**
 * Parses input arguments and creates a new RemarkCommand object
 */
public class RemarkCommandParser implements Parser<RemarkCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the RemarkCommand
     * and returns a RemarkCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RemarkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_REMARK);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));
        }


        String remark = argMultimap.getValue(PREFIX_REMARK).orElse("");

        return new RemarkCommand(index, new Remark(remark));
    }
}
```
###### /java/seedu/address/logic/commands/RemarkCommand.java
``` java
/**
 * Edits the remark of an existing person in the address book.
 */
public class RemarkCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "remark";
    public static final String COMMAND_ALIAS = "rem";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the remark of the person identified "
            + "by the index number used in the last person listing. "
            + "Existing remark will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_REMARK + "[REMARK]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_REMARK + "Likes to drink coffee.";

    public static final String MESSAGE_ADD_REMARK_SUCCESS = "Added remark to Person: %1$s";
    public static final String MESSAGE_DELETE_REMARK_SUCCESS = "Removed remark from Person: %1$s";

    private final Index index;
    private final Remark remark;

    private Person personToEdit;
    private Person editedPerson;

    /**
     * @param index of the person in the filtered person list to edit
     * @param remark the remark to be updated to the person
     */
    public RemarkCommand(Index index, Remark remark) {
        requireNonNull(index);
        requireNonNull(remark);

        this.index = index;
        this.remark = remark;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(personToEdit);
        requireNonNull(editedPerson);

        try {
            model.updatePerson(personToEdit, editedPerson);
        } catch (DuplicatePersonException dpe) {
            throw new AssertionError("The changing of the remark should not result in a duplicate");
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        personToEdit = lastShownList.get(index.getZeroBased());
        editedPerson = new Person(personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), remark, personToEdit.getTags());
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RemarkCommand)) {
            return false;
        }

        // state check
        RemarkCommand r = (RemarkCommand) other;

        return index.equals(r.index) && remark.equals(r.remark);
    }

    /**
     * Generate a success message according to whether the remark is added or removed
     */
    private String generateSuccessMessage(Person personToEdit) {
        String message;
        if (remark.value.isEmpty()) {
            message = MESSAGE_DELETE_REMARK_SUCCESS;
        } else {
            message = MESSAGE_ADD_REMARK_SUCCESS;
        }
        return String.format(message, personToEdit);
    }
}
```
###### /java/seedu/address/model/person/Remark.java
``` java
/**
 * Represents a Person's remark in the address book.
 * Guarantees: immutable; is valid
 */
public class Remark {

    public static final String MESSAGE_REMARK_CONSTRAINTS =
            "Person remarks can take any values, can even be blank";
    public final String value;

    public Remark(String remark) {
        //requireNonNull(remark);
        this.value = remark;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Remark // instanceof handles nulls
                && this.value.equals(((Remark) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
```
