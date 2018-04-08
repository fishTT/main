package seedu.address.model.book;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import seedu.address.model.UniqueList;

/**
 * Contains data about a single book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Book {

    private final UniqueList<Author> authors;
    private final Title title;
    private final UniqueList<Category> categories;
    private final Description description;
    private final Status status;
    private final Priority priority;
    private final Rating rating;
    private final Gid gid;
    private final Isbn isbn;
    private final PublicationDate publicationDate;
    private final Publisher publisher;

    /**
     * Creates a {@code Book} with the default status, priority, and rating.
     * Every field must be present and not null.
     * Duplicate authors and categories will be silently ignored.
     */
    public Book(Gid gid, Isbn isbn, Collection<Author> authors, Title title, Collection<Category> categories,
                Description description, Publisher publisher, PublicationDate publicationDate) {
        this(gid, isbn, authors, title, categories, description, Status.DEFAULT_STATUS,
                Priority.DEFAULT_PRIORITY, new Rating(), publisher, publicationDate);
    }

    /**
     * Creates a {@code Book}.
     * Every field must be present and not null.
     * Duplicate authors and categories will be silently ignored.
     */
    public Book(Gid gid, Isbn isbn, Collection<Author> authors, Title title, Collection<Category> categories,
                Description description, Status status, Priority priority, Rating rating,
                Publisher publisher, PublicationDate publicationDate) {
        requireAllNonNull(gid, isbn, authors, title, categories, description,
                status, priority, rating, publisher, publicationDate);
        this.gid = gid;
        this.isbn = isbn;
        this.authors = new UniqueList<>();
        this.authors.addAllIgnoresDuplicates(authors);
        this.title = title;
        this.categories = new UniqueList<>();
        this.categories.addAllIgnoresDuplicates(categories);
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.rating = rating;
        this.publicationDate = publicationDate;
        this.publisher = publisher;
    }

    /**
     * Returns an immutable authors list, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<Author> getAuthors() {
        return Collections.unmodifiableList(authors.toList());
    }

    public Title getTitle() {
        return title;
    }

    /**
     * Returns an immutable categories list, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<Category> getCategories() {
        return Collections.unmodifiableList(categories.toList());
    }

    public Description getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public Priority getPriority() {
        return priority;
    }

    public Rating getRating() {
        return rating;
    }

    public Gid getGid() {
        return gid;
    }

    public Isbn getIsbn() {
        return isbn;
    }

    public PublicationDate getPublicationDate() {
        return publicationDate;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Book)) {
            return false;
        }

        Book otherBook = (Book) other;
        return otherBook.getIsbn().equals(this.getIsbn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getTitle()).append(" - Authors: ");
        getAuthors().forEach(author -> builder.append("[").append(author).append("]"));
        return builder.toString();
    }

}
