package guitests.guihandles;

import javafx.scene.Node;

/**
 * Provides a handle for {@code WelcomePanel}.
 */
public class WelcomePanelHandle extends NodeHandle<Node> {

    public static final String WELCOME_PANEL_ID = "#welcomePanel";

    public WelcomePanelHandle(Node welcomePanelNode) {
        super(welcomePanelNode);
    }

    public boolean isVisible() {
        return getRootNode().isVisible();
    }

}
