package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.model.alias.Alias;

/**
 * Panel containing the list of aliases.
 */
public class AliasListPanel extends UiPart<Region> {
    private static final String FXML = "AliasListPanel.fxml";

    @FXML
    private ListView<Alias> aliasListView;

    public AliasListPanel(ObservableList<Alias> aliasList) {
        super(FXML);
        getRoot().setVisible(false);
        setConnections(aliasList);
    }

    private void setConnections(ObservableList<Alias> aliasList) {
        aliasListView.setItems(aliasList);
        aliasListView.setCellFactory(listView -> new AliasListViewCell());
    }

    protected void hide() {
        getRoot().setVisible(false);
    }

    protected void show() {
        getRoot().setVisible(true);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of an {@code Alias}.
     */
    private static class AliasListViewCell extends ListCell<Alias> {

        @Override
        protected void updateItem(Alias alias, boolean empty) {
            super.updateItem(alias, empty);

            if (empty || alias == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new AliasCard(alias, getIndex() + 1).getRoot());
            }
        }
    }
}
