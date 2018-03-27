# Liu
###### /resources/view/DarkTheme.css
``` css
#tags .blue {
    -fx-text-fill: white;
    -fx-background-color: blue;
}

#tags .cyan {
    -fx-text-fill: black;
    -fx-background-color: cyan;
}

#tags .green {
    -fx-text-fill: black;
    -fx-background-color: green;
}

#tags .magenta {
    -fx-text-fill: black;
    -fx-background-color: magenta;
}

#tags .orange {
    -fx-text-fill: white;
    -fx-background-color: orange;
}

#tags .blue {
    -fx-text-fill: white;
    -fx-background-color: blue;
}

#tags .pink {
    -fx-text-fill: black;
    -fx-background-color: pink;
}

#tags .red {
    -fx-text-fill: white;
    -fx-background-color: red;
}

#tags .yellow {
    -fx-text-fill: black;
    -fx-background-color: yellow;
}

#tags .teal {
    -fx-text-fill: white;
    -fx-background-color: teal;
}

#tags .brown {
    -fx-text-fill: white;
    -fx-background-color: brown;
}
```
###### /java/seedu/address/ui/PersonCard.java
``` java
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

    //@author
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
```
