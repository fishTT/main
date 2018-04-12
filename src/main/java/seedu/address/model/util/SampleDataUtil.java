package seedu.address.model.util;

import java.util.Arrays;
import java.util.Collections;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.BookShelf;
import seedu.address.model.ReadOnlyBookShelf;
import seedu.address.model.book.Author;
import seedu.address.model.book.Book;
import seedu.address.model.book.Category;
import seedu.address.model.book.Description;
import seedu.address.model.book.Gid;
import seedu.address.model.book.Isbn;
import seedu.address.model.book.Priority;
import seedu.address.model.book.PublicationDate;
import seedu.address.model.book.Publisher;
import seedu.address.model.book.Rating;
import seedu.address.model.book.Status;
import seedu.address.model.book.Title;
import seedu.address.model.book.exceptions.DuplicateBookException;

/**
 * Contains utility methods for populating {@code BookShelf} with sample data.
 */
public final class SampleDataUtil {

    private static final Book ARTEMIS = new Book(new Gid("ry3GjwEACAAJ"), new Isbn("9780525572664"),
            Collections.singleton(new Author("Andy Weir")), new Title("Artemis"),
            CollectionUtil.toList(new Category("Fiction"), new Category("Science Fiction")),
            new Description("This is Artemis."), Status.READ, Priority.LOW, new Rating(5),
            new Publisher(""), new PublicationDate("2017-11-14"));
    private static final Book BABYLON_ASHES = new Book(new Gid("3jsYCwAAQBAJ"), new Isbn("9780316217637"),
            Collections.singleton(new Author("James S. A. Corey")), new Title("Babylon's Ashes"),
            CollectionUtil.toList(new Category("Fiction"), new Category("Science Fiction"),
                    new Category("Space Opera")),
            new Description("This is Babylon's Ashes."), Status.READING, Priority.HIGH, new Rating(4),
            new Publisher("Orbit"), new PublicationDate("2016-12-06"));
    private static final Book COLLAPSING_EMPIRE = new Book(new Gid("2SoaDAAAQBAJ"), new Isbn("9780765388896"),
            Collections.singleton(new Author("John Scalzi")), new Title("The Collapsing Empire"),
            CollectionUtil.toList(new Category("Fiction"), new Category("Science Fiction"),
                    new Category("Space Opera")),
            new Description("This is Collapsing Empire."), Status.READING, Priority.LOW, new Rating(-1),
            new Publisher("Tor Books"), new PublicationDate("2017-03-21"));
    private static final Book CONSIDER_PHLEBAS = new Book(new Gid("3_bJKlAOecEC"), new Isbn("9780316095839"),
            Collections.singleton(new Author("Iain M. Banks")), new Title("Consider Phlebas"),
            CollectionUtil.toList(new Category("Fiction"), new Category("Science Fiction"),
                    new Category("Space Opera")),
            new Description("This is Consider Phlebas."), Status.UNREAD, Priority.NONE, new Rating(-1),
                new Publisher("Orbit"), new PublicationDate("2009-12-01"));
    private static final Book WAKING_GODS = new Book(new Gid("CIj1DAAAQBAJ"), new Isbn("9781405921909"),
            Collections.singleton(new Author("Sylvain Neuvel")), new Title("Waking Gods"),
            CollectionUtil.toList(new Category("Fiction"), new Category("Science Fiction")),
            new Description("This is Waking Gods."), Status.UNREAD, Priority.NONE, new Rating(-1),
            new Publisher("Penguin UK"), new PublicationDate("2017-04-06"));

    private SampleDataUtil() {} // prevents instantiation

    public static Book[] getSampleBooks() {
        return new Book[]{ ARTEMIS, BABYLON_ASHES, COLLAPSING_EMPIRE, CONSIDER_PHLEBAS, WAKING_GODS };
    }

    public static Book[] getSampleBooksDefaultOrdering() {
        return new Book[]{ BABYLON_ASHES, COLLAPSING_EMPIRE, CONSIDER_PHLEBAS, WAKING_GODS, ARTEMIS };
    }

    public static ReadOnlyBookShelf getSampleBookShelf() {
        BookShelf sampleBookShelf = new BookShelf();
        try {
            sampleBookShelf.setBooks(Arrays.asList(getSampleBooks()));
        } catch (DuplicateBookException e) {
            throw new AssertionError("Unexpected DuplicateBookException.");
        }
        return sampleBookShelf;
    }

}
