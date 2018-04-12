//@@author WoodyLau
package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMPANY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPARTMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDUSTRY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEBSITE;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.EditDetailsCommand;
import seedu.address.logic.commands.EditDetailsCommand.EditContactDescriptor;
import seedu.address.logic.commands.EditDetailsCommand.EditLeadDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditDetailsCommandParser implements Parser<EditDetailsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditDetailsCommand
     * and returns an EditDetailsCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditDetailsCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argLeadMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_COMPANY, PREFIX_INDUSTRY, PREFIX_RATING, PREFIX_TITLE, PREFIX_WEBSITE);
        ArgumentMultimap argContactMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_COMPANY, PREFIX_DEPARTMENT, PREFIX_TITLE);

        Index index;

        try {
            index = ParserUtil.parseIndex(argLeadMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            try {
                index = ParserUtil.parseIndex(argContactMultimap.getPreamble());
            } catch (IllegalValueException ive2) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        EditDetailsCommand.MESSAGE_USAGE));
            }
        }

        EditLeadDescriptor editLeadDescriptor = new EditLeadDescriptor();
        EditContactDescriptor editContactDescriptor = new EditContactDescriptor();
        try {
            ParserUtil.parseCompany(argLeadMultimap.getValue(PREFIX_COMPANY)).ifPresent(editLeadDescriptor::setCompany);
            ParserUtil.parseIndustry(argLeadMultimap.getValue(PREFIX_INDUSTRY))
                    .ifPresent(editLeadDescriptor::setIndustry);
            ParserUtil.parseRating(argLeadMultimap.getValue(PREFIX_RATING)).ifPresent(editLeadDescriptor::setRating);
            ParserUtil.parseTitle(argLeadMultimap.getValue(PREFIX_TITLE)).ifPresent(editLeadDescriptor::setTitle);
            ParserUtil.parseWebsite(argLeadMultimap.getValue(PREFIX_WEBSITE)).ifPresent(editLeadDescriptor::setWebsite);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
        try {
            ParserUtil.parseCompany(argContactMultimap.getValue(PREFIX_COMPANY))
                    .ifPresent(editContactDescriptor::setCompany);
            ParserUtil.parseDepartment(argContactMultimap.getValue(PREFIX_DEPARTMENT))
                    .ifPresent(editContactDescriptor::setDepartment);
            ParserUtil.parseTitle(argContactMultimap.getValue(PREFIX_TITLE)).ifPresent(editContactDescriptor::setTitle);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (!editLeadDescriptor.isAnyFieldEdited() && !editContactDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditDetailsCommand.MESSAGE_NOT_EDITED);
        }

        return new EditDetailsCommand(index, editLeadDescriptor, editContactDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws IllegalValueException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
