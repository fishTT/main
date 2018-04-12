package seedu.address.model.book;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class PublicationDateTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new PublicationDate(null));
    }

    @Test
    public void equals() {
        PublicationDate publicationDate = new PublicationDate("2000-01-01");

        // same object -> return true
        assertTrue(publicationDate.equals(publicationDate));

        // same values -> return true
        PublicationDate publicationDateCopy = new PublicationDate("2000-01-01");
        assertTrue(publicationDate.equals(publicationDateCopy));

        // different types -> returns false
        assertFalse(publicationDate.equals("string"));

        // null -> returns false
        assertFalse(publicationDate.equals(null));

        // different values -> return false
        PublicationDate differentPublicationDate = new PublicationDate("2018-01-01");
        assertFalse(publicationDate.equals(differentPublicationDate));
    }

    @Test
    public void hashCode_sameContent_returnsSameValue() {
        assertEquals(new PublicationDate("2000-01-01").hashCode(), new PublicationDate("2000-01-01").hashCode());
        assertEquals(new PublicationDate("").hashCode(), new PublicationDate("").hashCode());
    }
}
