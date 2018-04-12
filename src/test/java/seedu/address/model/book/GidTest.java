package seedu.address.model.book;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class GidTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Gid(null));
    }

    @Test
    public void equals() {
        Gid gid = new Gid("1");

        // same object -> return true
        assertTrue(gid.equals(gid));

        // same values -> return true
        Gid gidCopy = new Gid("1");
        assertTrue(gid.equals(gidCopy));

        // different types -> returns false
        assertFalse(gid.equals("string"));

        // null -> returns false
        assertFalse(gid.equals(null));

        // different values -> return false
        Gid differentGid = new Gid("x");
        assertFalse(gid.equals(differentGid));
    }

    @Test
    public void hashCode_sameContent_returnsSameValue() {
        assertEquals(new Gid("1234567").hashCode(), new Gid("1234567").hashCode());
        assertEquals(new Gid("").hashCode(), new Gid("").hashCode());
    }
}
