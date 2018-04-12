package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Contact;
import seedu.address.model.person.Lead;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final String[] TAG_COLORS =
        { "blue", "cyan", "green", "magenta", "orange", "pink", "red", "yellow", "teal", "brown" };

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label type;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label remark;
    @FXML
    private FlowPane tags;

    //@@author WoodyLau
    @FXML
    private Label company;
    @FXML
    private Label industry;
    @FXML
    private Label rating;
    @FXML
    private Label website;
    @FXML
    private Label department;
    //@@author

    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        if (person instanceof Lead && ((Lead) person).getTitle() != null) {
            name.setText(((Lead) person).getTitle() + " " + person.getName().fullName);
        } else if (person instanceof Contact && ((Contact) person).getTitle() != null) {
            String text = ((Contact) person).getTitle() + " " + person.getName().fullName;
            name.setText(text);
        } else {
            name.setText(person.getName().fullName);
        }
        type.setText(person.getType().value);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        remark.setText(person.getRemark().value);
        initTags(person);
        if (person instanceof Lead) {
            setLead((Lead) person);
        } else {
            setContact((Contact) person);
        }
    }

    //@@author WoodyLau
    private void setLead(Lead person) {
        department.setVisible(false);
        department.setManaged(false);

        if (person.getCompany() == null) {
            company.setText("Company: Not Given");
        } else {
            company.setText("Company: " + person.getCompany());
        }
        if (person.getIndustry() == null) {
            industry.setVisible(false);
            industry.setManaged(false);
        } else {
            industry.setText("Industry: " + person.getIndustry());
        }
        if (person.getRating() == 0) {
            rating.setText("Rating: Not Given");
        } else {
            rating.setText("Rating: " + person.getRating() + "/5");
        }
        if (person.getWebsite() == null) {
            website.setVisible(false);
            website.setManaged(false);
        } else {
            website.setText("Website: " + person.getWebsite());
        }
    }

    private void setContact(Contact person) {
        industry.setVisible(false);
        industry.setManaged(false);
        rating.setVisible(false);
        rating.setManaged(false);
        website.setVisible(false);
        website.setManaged(false);

        if (person.getCompany() == null) {
            company.setText("Company: Not Given");
        } else {
            company.setText("Company: " + person.getCompany());
        }
        if (person.getDepartment() == null) {
            department.setText("Department: Not Given");
        } else {
            department.setText("Department: " + person.getDepartment());
        }
    }

    //@@author A0155428B
    /**
     *Returns the color for {@code tagName}'s label
     */
    private String getTagColorFor(String tagName) {
        //Uses the hash code of the tag name to generate a color, such that each run of the program
        //produce the same color for that tag name
        return TAG_COLORS[Math.abs(tagName.hashCode()) % TAG_COLORS.length];
    }

    /**
     *Creates tag labels for {@code person}.
     */
    private void initTags(Person person) {
        person.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(getTagColorFor(tag.tagName));
            tags.getChildren().add(tagLabel);
        });
    }

    //@@author
    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        PersonCard card = (PersonCard) other;
        return id.getText().equals(card.id.getText())
                && person.equals(card.person);
    }
}
