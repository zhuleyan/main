package seedu.address.logic.commands;

import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

//@@author zhuleyan
/**
 * Sorts persons in CRM by name.
 */
public class SortCommand extends Command {
    public static final String COMMAND_WORD = "sort";
    //@@author Sheikh-Umar
    public static final String COMMAND_ALIAS = "st";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts and lists all Leads and Contacts by name in alphabetical order.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Sorted all Leads or Contacts by name";
    //@@author

    @Override
    public CommandResult execute() {
        model.sortAllPersons();
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
