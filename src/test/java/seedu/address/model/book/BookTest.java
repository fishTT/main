package seedu.address.model.book;

import java.util.Collections;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class BookTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Book(null, null,
                null, null, null, null, null, null, null, null, null));

        Assert.assertThrows(NullPointerException.class, () -> new Book(null, new Isbn(""),
                Collections.emptyList(), new Title(""), Collections.emptyList(),
                new Description(""), new Publisher(""), new PublicationDate("")));

        Assert.assertThrows(NullPointerException.class, () -> new Book(new Gid(""), null,
                Collections.emptyList(), new Title(""), Collections.emptyList(),
                new Description(""), new Publisher(""), new PublicationDate("")));

        Assert.assertThrows(NullPointerException.class, () -> new Book(new Gid(""), new Isbn(""),
                null, new Title(""), Collections.emptyList(),
                new Description(""), new Publisher(""), new PublicationDate("")));

        Assert.assertThrows(NullPointerException.class, () -> new Book(new Gid(""), new Isbn(""),
                Collections.emptyList(), null, Collections.emptyList(),
                new Description(""), new Publisher(""), new PublicationDate("")));

        Assert.assertThrows(NullPointerException.class, () -> new Book(new Gid(""), new Isbn(""),
                Collections.emptyList(), new Title(""), null,
                new Description(""), new Publisher(""), new PublicationDate("")));

        Assert.assertThrows(NullPointerException.class, () -> new Book(new Gid(""), new Isbn(""),
                Collections.emptyList(), new Title(""), Collections.emptyList(),
                null, new Publisher(""), new PublicationDate("")));

        Assert.assertThrows(NullPointerException.class, () -> new Book(new Gid(""), new Isbn(""),
                Collections.emptyList(), new Title(""), Collections.emptyList(), new Description(""),
                null, Priority.LOW, new Rating(-1), new Publisher(""), new PublicationDate("")));

        Assert.assertThrows(NullPointerException.class, () -> new Book(new Gid(""), new Isbn(""),
                Collections.emptyList(), new Title(""), Collections.emptyList(), new Description(""),
                Status.READ, null, new Rating(-1), new Publisher(""), new PublicationDate("")));

        Assert.assertThrows(NullPointerException.class, () -> new Book(new Gid(""), new Isbn(""),
                Collections.emptyList(), new Title(""), Collections.emptyList(), new Description(""),
                Status.READ, Priority.LOW, null, new Publisher(""), new PublicationDate("")));

        Assert.assertThrows(NullPointerException.class, () -> new Book(new Gid(""), new Isbn(""),
                Collections.emptyList(), new Title(""), Collections.emptyList(),
                new Description(""), null, new PublicationDate("")));

        Assert.assertThrows(NullPointerException.class, () -> new Book(new Gid(""), new Isbn(""),
                Collections.emptyList(), new Title(""), Collections.emptyList(),
                new Description(""), new Publisher(""), null));
    }

}
