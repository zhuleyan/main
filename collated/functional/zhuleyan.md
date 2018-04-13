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
            Reader reader = Files.newBufferedReader(Paths.get(args));
            if (!args.substring(args.length() - 4, args.length()).equalsIgnoreCase(".csv")) {
                throw new ParseException("not a csv file");
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
        }
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
