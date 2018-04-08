package guitests.guihandles;

import java.net.URL;

import javafx.scene.Node;

//@@author qiu-siqi
/**
 * Provides a handle for the {@code BookReviewsPanel} of the UI.
 */
public class BookReviewsPanelHandle extends NodeHandle<Node> {
    public static final String BOOK_REVIEWS_PANE_ID = "#bookReviewsPane";

    private static final String BOOK_REVIEWS_BROWSER_ID = "#browser";

    public BookReviewsPanelHandle(Node bookReviewsPanelNode) {
        super(bookReviewsPanelNode);
    }

    public boolean isVisible() {
        return getRootNode().isVisible();
    }

    /**
     * Returns the {@code URL} of the currently loaded page.
     */
    public URL getLoadedUrl() {
        return WebViewUtil.getLoadedUrl(getChildNode(BOOK_REVIEWS_BROWSER_ID));
    }
}
