package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.alias.Alias;

/**
 * A UI component that displays information of an {@code Alias}.
 */
public class AliasCard extends UiPart<Region> {

    private static final String FXML = "AliasListCard.fxml";

    private final Alias alias;

    @FXML
    private Label id;
    @FXML
    private Label name;
    @FXML
    private Label prefix;
    @FXML
    private Label namedArgs;

    public AliasCard(Alias alias, int displayedIndex) {
        super(FXML);
        this.alias = alias;
        id.setText(displayedIndex + ".");
        name.setText(alias.getName());
        prefix.setText(alias.getPrefix());
        namedArgs.setText(alias.getNamedArgs());
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AliasCard)) {
            return false;
        }

        // state check
        AliasCard card = (AliasCard) other;
        return id.getText().equals(card.id.getText())
                && alias.equals(card.alias);
    }
}
