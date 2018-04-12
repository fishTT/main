package seedu.address.model.book;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class PublisherTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Publisher(null));
    }

    @Test
    public void equals() {
        Publisher publisher = new Publisher("1");

        // same object -> return true
        assertTrue(publisher.equals(publisher));

        // same values -> return true
        Publisher publisherCopy = new Publisher("1");
        assertTrue(publisher.equals(publisherCopy));

        // different types -> returns false
        assertFalse(publisher.equals("string"));

        // null -> returns false
        assertFalse(publisher.equals(null));

        // different values -> return false
        Publisher differentPublisher = new Publisher("x");
        assertFalse(publisher.equals(differentPublisher));
    }

    @Test
    public void hashCode_sameContent_returnsSameValue() {
        assertEquals(new Publisher("Publisher 1").hashCode(), new Publisher("Publisher 1").hashCode());
        assertEquals(new Publisher("").hashCode(), new Publisher("").hashCode());
    }
}
