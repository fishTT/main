package guitests.guihandles;

import java.util.Optional;
import java.util.Set;

import javafx.scene.Node;
import javafx.scene.control.ListView;
import seedu.address.model.alias.Alias;

/**
 * Provides a handle for {@code AliasListPanel} containing the list of aliases.
 */
public class AliasListPanelHandle extends NodeHandle<Node> {

    public static final String ALIAS_LIST_PANEL_ID = "#aliasListPanel";
    private static final String LIST_VIEW_FIELD_ID = "#aliasListView";
    private static final String CARD_PANE_ID = "#aliasCardPane";

    private final ListView<Alias> aliasListView;

    public AliasListPanelHandle(Node aliasListPanelNode) {
        super(aliasListPanelNode);
        aliasListView = getChildNode(LIST_VIEW_FIELD_ID);
    }

    /**
     * Navigates the listview to display and select {@code alias}.
     */
    public void navigateToCard(Alias alias) {
        if (!aliasListView.getItems().contains(alias)) {
            throw new IllegalArgumentException("Alias does not exist.");
        }

        guiRobot.interact(() -> aliasListView.scrollTo(alias));
        guiRobot.pauseForHuman();
    }

    /**
     * Navigates the listview to {@code index}.
     */
    public void navigateToCard(int index) {
        if (index < 0 || index >= aliasListView.getItems().size()) {
            throw new IllegalArgumentException("Index is out of bounds.");
        }

        guiRobot.interact(() -> aliasListView.scrollTo(index));
        guiRobot.pauseForHuman();
    }

    /**
     * Returns the alias card handle of an alias associated with the {@code index} in the list.
     */
    public Optional<AliasCardHandle> getAliasCardHandle(int index) {
        return getAllCardNodes().stream()
                .map(AliasCardHandle::new)
                .filter(handle -> handle.equals(getAlias(index)))
                .findFirst();
    }

    private Alias getAlias(int index) {
        return aliasListView.getItems().get(index);
    }

    /**
     * Returns all card nodes in the scene graph.
     * Card nodes that are visible in the listview are definitely in the scene graph, while some nodes that are not
     * visible in the listview may also be in the scene graph.
     */
    private Set<Node> getAllCardNodes() {
        return guiRobot.lookup(CARD_PANE_ID).queryAll();
    }

    public boolean isVisible() {
        return getRootNode().isVisible();
    }

}
