package guitests.guihandles;

import javafx.scene.Node;

//@@author qiu-siqi
/**
 * Provides a handle for the {@code BookInLibraryPanel} of the UI.
 */
public class BookInLibraryPanelHandle extends NodeHandle<Node> {
    public static final String BOOK_IN_LIBRARY_PANEL_ID = "#bookInLibraryPanel";

    public BookInLibraryPanelHandle(Node bookInLibraryPanelNode) {
        super(bookInLibraryPanelNode);
    }

    public boolean isVisible() {
        return getRootNode().isVisible();
    }

}
