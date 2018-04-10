package guitests.guihandles;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.book.Author;
import seedu.address.model.book.Book;
import seedu.address.model.book.Category;
import seedu.address.ui.BookCard;

/**
 * Provides a handle to a person card in the person list panel.
 */
public class BookCardHandle extends NodeHandle<Node> {
    private static final String ID_FIELD_ID = "#id";
    private static final String TITLE_FIELD_ID = "#title";
    private static final String AUTHORS_FIELD_ID = "#authors";
    private static final String CATEGORIES_FIELD_ID = "#categories";
    private static final String STATUS_FIELD_ID = "#status";
    private static final String PRIORITY_FIELD_ID = "#priority";
    private static final String RATING_FIELD_ID = "#rating";

    private final Label idLabel;
    private final Label titleLabel;
    private final List<Label> authorsLabel;
    private final List<Label> categoriesLabel;
    private final Label statusLabel;
    private final Label priorityLabel;
    private final Label ratingLabel;

    public BookCardHandle(Node cardNode) {
        super(cardNode);

        this.idLabel = getChildNode(ID_FIELD_ID);
        this.titleLabel = getChildNode(TITLE_FIELD_ID);

        Region authorsContainer = getChildNode(AUTHORS_FIELD_ID);
        this.authorsLabel = authorsContainer
                .getChildrenUnmodifiable()
                .stream()
                .map(Label.class::cast)
                .collect(Collectors.toList());

        Region categoriesContainer = getChildNode(CATEGORIES_FIELD_ID);
        this.categoriesLabel = categoriesContainer
                .getChildrenUnmodifiable()
                .stream()
                .map(Label.class::cast)
                .collect(Collectors.toList());

        this.statusLabel = getChildNode(STATUS_FIELD_ID);
        this.priorityLabel = getChildNode(PRIORITY_FIELD_ID);
        this.ratingLabel = getChildNode(RATING_FIELD_ID);
    }

    public String getId() {
        return idLabel.getText();
    }

    private String getTitle() {
        return titleLabel.getText();
    }

    private List<String> getAuthors() {
        return authorsLabel
                .stream()
                .map(Label::getText)
                .collect(Collectors.toList());
    }

    private List<String> getCategories() {
        return categoriesLabel
                .stream()
                .map(Label::getText)
                .collect(Collectors.toList());
    }

    private String getStatus() {
        return statusLabel.getText();
    }

    private String getPriority() {
        return priorityLabel.getText();
    }

    private String getRating() {
        return ratingLabel.getText();
    }

    /**
     * Returns true if all the fields of this handle are the same as {@code otherHandle}.
     */
    public boolean equals(BookCardHandle otherHandle) {
        return getId().equals(otherHandle.getId())
                && getTitle().equals(otherHandle.getTitle())
                && getAuthors().equals(otherHandle.getAuthors())
                && getCategories().equals(otherHandle.getCategories())
                && getStatus().equals(otherHandle.getStatus())
                && getPriority().equals(otherHandle.getPriority())
                && getRating().equals(otherHandle.getRating());
    }

    /**
     * Returns true if this handle contains {@code book}.
     */
    public boolean equals(Book book) {
        return getTitle().equals(book.getTitle().title)
                && new HashSet<>(getAuthors())
                    .equals(book.getAuthors().stream().map(Author::getDisplayText).collect(Collectors.toSet()))
                && new HashSet<>(getCategories())
                    .equals(book.getCategories().stream().limit(BookCard.DISPLAYED_CATEGORY_LIMIT)
                            .map(Category::getDisplayText).collect(Collectors.toSet()))
                && getStatus().equals(book.getStatus().getDisplayText())
                && getPriority().equals(book.getPriority().getDisplayText())
                && getRating().equals(book.getRating().getDisplayText());
    }
}
