package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.JumpToSearchResultsIndexRequestEvent;
import seedu.address.commons.events.ui.SearchResultsSelectionChangedEvent;
import seedu.address.model.book.Book;

/**
 * Panel containing the list of search results.
 */
public class SearchResultsPanel extends UiPart<Region> {
    private static final String FXML = "SearchResultsPanel.fxml";
    private static final String INFO_TEXT = "Showing %s search results.";
    private final Logger logger = LogsCenter.getLogger(SearchResultsPanel.class);

    @FXML
    private Label infoLabel;
    @FXML
    private ListView<Book> searchResultsListView;

    public SearchResultsPanel(ObservableList<Book> searchResults) {
        super(FXML);
        setConnections(searchResults);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<Book> searchResults) {
        searchResultsListView.setItems(searchResults);
        searchResultsListView.setCellFactory(listView -> new BookListViewCell());
        setEventHandlerForSelectionChangeEvent();
        setEventHandlerForListChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        searchResultsListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null && this.getRoot().isVisible()) {
                        logger.fine("Selection in search results panel changed to : '" + newValue + "'");
                        raise(new SearchResultsSelectionChangedEvent(newValue));
                    }
                });
    }

    private void setEventHandlerForListChangeEvent() {
        searchResultsListView.getItems().addListener((ListChangeListener<Book>) c -> {
            long newSize = c.getList().size();
            infoLabel.setText(String.format(INFO_TEXT, newSize));
        });
    }

    protected void clearSelection() {
        searchResultsListView.getSelectionModel().clearSelection();
    }

    /** Scrolls to the top of the search results list. */
    protected void scrollToTop() {
        searchResultsListView.scrollTo(0);
    }

    /**
     * Scrolls to the {@code Book} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        searchResultsListView.scrollTo(index);
        searchResultsListView.getSelectionModel().clearAndSelect(index);
    }

    @Subscribe
    private void handleJumpToResultsListRequestEvent(JumpToSearchResultsIndexRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Book}.
     */
    class BookListViewCell extends ListCell<Book> {

        @Override
        protected void updateItem(Book book, boolean empty) {
            super.updateItem(book, empty);

            if (empty || book == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new BookCard(book, getIndex() + 1).getRoot());
            }
        }
    }

}
