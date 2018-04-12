package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Lead;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.tag.Tag;

//@@author zhuleyan
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
