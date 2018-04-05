package seedu.address.ui;


import org.controlsfx.control.Notifications;

import javafx.geometry.Pos;

import javafx.scene.layout.Region;
import javafx.util.Duration;


/**
 * Panel containing the list of books.
 */
public class PopupBox extends UiPart<Region> {
    private static final String FXML = "PopupBox.fxml";
    private static final String MESSAGE = "There is no end to education. It is not that you read a book,\n"
            + " pass an examination, and finish with education.\n"
            + "The whole of life, from the moment you are born to\n "
            + "moment you die, is a process of learning.\n";
    public PopupBox() {
        super(FXML);
        Notifications pop = Notifications.create()
                .title("Quote of the day")
                .text(MESSAGE)
                .graphic(null)
                .hideAfter(Duration.seconds(10))
                .position(Pos.BOTTOM_RIGHT);
        pop.show();
    }


}
