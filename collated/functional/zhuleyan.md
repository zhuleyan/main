# zhuleyan
###### /java/seedu/address/logic/parser/ImportCommandParser.java
``` java
/**
 * Parses input arguments and creates a new ImportCommand object
 */
public class ImportCommandParser {
    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ImportCommand parse(String args) throws ParseException {
        args = args.trim();
        if (args.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        }
        List<Lead> list = new ArrayList<>();
        int index = 1;
        try {
            Reader reader = new FileReader(args);
            if (!args.substring(args.length() - 4, args.length()).equalsIgnoreCase(".csv")) {
                throw new WrongFileFormatException("not a csv file");
            }
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            for (CSVRecord csvRecord : csvParser) {
                Name name = ParserUtil.parseName(csvRecord.get(0));
                Phone phone = ParserUtil.parsePhone(csvRecord.get(1));
                Email email = ParserUtil.parseEmail(csvRecord.get(2));
                Address address = ParserUtil.parseAddress(csvRecord.get(3));
                Remark remark = new Remark("");
                Set<Tag> tagList = Collections.emptySet();

                Lead person = new Lead(name, phone, email, address, remark, tagList);
                list.add(person);
                index++;
            }
            return new ImportCommand(list);
        } catch (IOException e) {
            throw new ParseException("invalid file path");
        } catch (IllegalValueException ive) {
            String errorMessage = ive.getMessage();
            String indexMessage = "Error at the person of index " + index + ": ";
            String result = indexMessage.concat(errorMessage);
            throw new ParseException(result, ive);
        } catch (WrongFileFormatException wffe) {
            throw new ParseException("not a csv file");
        }
    }
}
```
###### /java/seedu/address/logic/parser/RemarkCommandParser.java
``` java
//reused
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

        if (!arePrefixPresent(argMultimap, PREFIX_REMARK)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));
        }

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));
        }


        String remark = argMultimap.getValue(PREFIX_REMARK).orElse("");

        return new RemarkCommand(index, new Remark(remark));
    }

    /**
     * Returns true if the remark prefix doesn't contain empty {@code Optional} value in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixPresent(ArgumentMultimap argumentMultimap, Prefix prefix) {
        return argumentMultimap.getValue(prefix).isPresent();
    }
}
```
###### /java/seedu/address/logic/parser/AddressBookParser.java
``` java
        case RemarkCommand.COMMAND_WORD:
        case RemarkCommand.COMMAND_ALIAS:
            return new RemarkCommandParser().parse(arguments);
```
###### /java/seedu/address/logic/parser/AddressBookParser.java
``` java
        case SortCommand.COMMAND_WORD:
        case SortCommand.COMMAND_ALIAS:
            return new SortCommand();

        case ImportCommand.COMMAND_WORD:
        case ImportCommand.COMMAND_ALIAS:
            return new ImportCommandParser().parse(arguments);
```
###### /java/seedu/address/logic/commands/SortCommand.java
``` java
/**
 * Sorts persons in CRM by name.
 */
public class SortCommand extends Command {
    public static final String COMMAND_WORD = "sort";
```
###### /java/seedu/address/logic/commands/SortCommand.java
``` java
    @Override
    public CommandResult execute() {
        model.sortAllPersons();
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
```
###### /java/seedu/address/logic/commands/RemarkCommand.java
``` java
//reused
/**
 * Edits the remark of an existing person in the address book.
 */
public class RemarkCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "remark";
```
###### /java/seedu/address/logic/commands/ImportCommand.java
``` java
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
```
###### /java/seedu/address/model/person/UniquePersonList.java
``` java
    /**
     * Sorts all persons by name in alphabetical order.
     */
    public void sort() {
        internalList.sort(
            Comparator.comparing((
                Person p) -> p.getName().toString(), (
                s1, s2) -> (s1.compareToIgnoreCase(s2) == 0) ? s1.compareTo(s2) : s1.compareToIgnoreCase(s2))
        );
    }
```
###### /java/seedu/address/model/person/Remark.java
``` java
//reused
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
