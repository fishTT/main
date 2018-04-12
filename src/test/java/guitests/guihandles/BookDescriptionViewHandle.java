package guitests.guihandles;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javafx.scene.Node;
import javafx.scene.web.WebView;
import seedu.address.commons.core.LogsCenter;

//@@author qiu-siqi
/**
 * Provides a handle for {@code BookDescriptionView}.
 */
public class BookDescriptionViewHandle extends NodeHandle<Node> {
    public static final String BOOK_DESCRIPTION_VIEW_ID = "#bookDescriptionView";

    private static final String DESCRIPTION_FIELD_ID = "#description";

    private final WebView descriptionView;

    public BookDescriptionViewHandle(Node bookDescriptionNode) {
        super(bookDescriptionNode);

        this.descriptionView = getChildNode(DESCRIPTION_FIELD_ID);
    }

    public String getContent() {
        final FutureTask<String> query = new FutureTask<>(() -> (String) descriptionView.getEngine()
                .executeScript("document.getElementById('description').innerHTML"));
        guiRobot.interact(query);
        try {
            return query.get();
        } catch (InterruptedException | ExecutionException e) {
            LogsCenter.getLogger(this.getClass()).warning("Failed to fetch book description.");
            return "";
        }
    }
}
