package seedu.address.ui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import org.controlsfx.control.Notifications;

import javafx.geometry.Pos;

import javafx.scene.layout.Region;
import javafx.util.Duration;

//@@author fishTT
/**
 * Panel containing the list of books.
 */
public class PopupBox extends UiPart<Region> {
    private static final String FXML = "PopupBox.fxml";
    private static String MESSAGE ="";


    public String getQuote() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("/text/quote.txt"));
        List<String> lines = new ArrayList<String>();

        String line = reader.readLine();

        while( line != null ) {
            lines.add(line);
            line = reader.readLine();
        }

// Choose a random one from the list
        Random r = new Random();
        String randomString = lines.get(r.nextInt(lines.size()));
        return randomString;
    }



    public PopupBox() throws IOException {
        super(FXML);
        MESSAGE = getQuote();
        Notifications pop = Notifications.create()
                .title("Quote of the day")
                .text(MESSAGE)
                .graphic(null)
                .hideAfter(Duration.seconds(10))
                .position(Pos.BOTTOM_RIGHT);
        pop.show();
    }


}
