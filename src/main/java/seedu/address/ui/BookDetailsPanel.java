package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.BookListSelectionChangedEvent;
import seedu.address.commons.events.ui.RecentBooksSelectionChangedEvent;
import seedu.address.commons.events.ui.SearchResultsSelectionChangedEvent;
import seedu.address.model.book.Book;

/**
 * The Book Details Panel of the App.
 */
public class BookDetailsPanel extends UiPart<Region> {

    private static final String FXML = "BookDetailsPanel.fxml";
    private static final String DEFAULT_LABEL_STYLE_CLASS = "label";
    private static final String URL_BOOK_FRONT_COVER =
            "https://books.google.com/books/content?id=%s&printsec=frontcover&img=1&zoom=1";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    // Independent Ui parts residing in this Ui container
    private BookDescriptionView bookDescriptionView;

    @FXML
    private ImageView frontCover;
    @FXML
    private Label title;
    @FXML
    private Label isbn;
    @FXML
    private FlowPane authors;
    @FXML
    private FlowPane categories;
    @FXML
    private Label publisher;
    @FXML
    private Label publicationDate;
    @FXML
    private Label status;
    @FXML
    private Label priority;
    @FXML
    private Label rating;
    @FXML
    private StackPane descriptionPlaceholder;
    @FXML
    private ScrollPane scrollPane;

    public BookDetailsPanel() {
        super(FXML);
        registerAsAnEventHandler(this);
        getRoot().setVisible(false);

        bookDescriptionView = new BookDescriptionView();
        descriptionPlaceholder.getChildren().add(bookDescriptionView.getRoot());
    }

    private void scrollToTop() {
        scrollPane.setVvalue(0);
    }

    /** Update this panel to show details about the specified book. */
    private void showBook(Book book) {
        frontCover.setImage(new Image(String.format(URL_BOOK_FRONT_COVER, book.getGid().gid), true));

        title.setText(book.getTitle().toString());
        isbn.setText(book.getIsbn().toString());
        publisher.setText(book.getPublisher().toString());
        publicationDate.setText(book.getPublicationDate().toString());
        bookDescriptionView.loadContent(book);

        status.setText(book.getStatus().getDisplayText());
        status.getStyleClass().setAll(DEFAULT_LABEL_STYLE_CLASS, book.getStatus().getStyleClass());
        priority.setText(book.getPriority().getDisplayText());
        priority.getStyleClass().setAll(DEFAULT_LABEL_STYLE_CLASS, book.getPriority().getStyleClass());
        rating.setText(book.getRating().getDisplayText());
        rating.getStyleClass().setAll(DEFAULT_LABEL_STYLE_CLASS, book.getRating().getStyleClass());

        authors.getChildren().clear();
        categories.getChildren().clear();
        book.getAuthors().forEach(author -> authors.getChildren()
                .add(new Label(author.getDisplayText())));
        book.getCategories().forEach(category -> categories.getChildren()
                .add(new Label(category.getDisplayText())));

        scrollToTop();
        getRoot().setVisible(true);
    }

    protected void setStyleSheet(String styleSheet) {
        bookDescriptionView.setStyleSheet(styleSheet);
    }

    protected void hide() {
        getRoot().setVisible(false);
    }

    @Subscribe
    private void handleBookListSelectionChangedEvent(BookListSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        showBook(event.getNewSelection());
    }

    @Subscribe
    private void handleSearchResultsSelectionChangedEvent(SearchResultsSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        showBook(event.getNewSelection());
    }

    @Subscribe
    private void handleRecentBooksSelectionChangedEvent(RecentBooksSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        showBook(event.getNewSelection());
    }
}
