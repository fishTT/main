package seedu.address.ui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.MainApp;

/**
 * The Welcome Panel of the App.
 */
public class WelcomePanel extends UiPart<Region> {

    private static final String FXML = "WelcomePanel.fxml";
    private static final URL QUOTES_FILE = MainApp.class.getResource("/text/quotes.txt");

    @FXML
    private Label qotd;

    public WelcomePanel() {
        super(FXML);

        try {
            qotd.setText(getRandomQuote());
        } catch (IOException e) {
            throw new AssertionError("Failed to load random quote.");
        }
    }

    //@@author fishTT
    private String getRandomQuote() throws IOException {
        List<String> lines = Resources.readLines(QUOTES_FILE, Charsets.UTF_8);

        // choose a random one from the list
        Random r = new Random();
        String randomQuote = lines.get(r.nextInt(lines.size()));
        return "\"" + randomQuote + "\"";
    }

    //@@author
    protected void hide() {
        getRoot().setVisible(false);
    }

    protected void show() {
        getRoot().setVisible(true);
    }

}
