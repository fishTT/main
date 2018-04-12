package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.BookListSelectionChangedEvent;
import seedu.address.commons.events.ui.JumpToBookListIndexRequestEvent;
import seedu.address.model.book.Book;

/**
 * Panel containing the list of books.
 */
public class BookListPanel extends UiPart<Region> {
    private static final String FXML = "BookListPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(BookListPanel.class);

    @FXML
    private ListView<Book> bookListView;

    public BookListPanel(ObservableList<Book> bookList) {
        super(FXML);
        setConnections(bookList);
        registerAsAnEventHandler(this);
    }

    /**
     * Set the currently displayed book list to {@code bookList}.
     */
    protected void setBookList(ObservableList<Book> bookList) {
        bookListView.setItems(bookList);
        bookListView.setCellFactory(listView -> new BookListViewCell());
    }

    private void setConnections(ObservableList<Book> bookList) {
        setBookList(bookList);
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        bookListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in book list panel changed to : '" + newValue + "'");
                        raise(new BookListSelectionChangedEvent(newValue));
                    }
                });
    }

    protected void clearSelection() {
        bookListView.getSelectionModel().clearSelection();
    }

    protected void scrollToTop() {
        bookListView.scrollTo(0);
    }

    /**
     * Scrolls to the {@code Book} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        bookListView.scrollTo(index);
        bookListView.getSelectionModel().clearAndSelect(index);
    }

    @Subscribe
    private void handleJumpToBookListRequestEvent(JumpToBookListIndexRequestEvent event) {
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
