package seedu.address.model.alias;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class AliasTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Alias(null, "test", "test"));
        Assert.assertThrows(NullPointerException.class, () -> new Alias("test", null, "test"));
        Assert.assertThrows(NullPointerException.class, () -> new Alias("test", "test", null));
    }

    @Test
    public void equals() {
        Alias alias = new Alias("name", "prefix", "args");

        // same object -> return true
        assertTrue(alias.equals(alias));

        // same values -> return true
        Alias aliasCopy = new Alias("name", "prefix", "args");
        assertTrue(alias.equals(aliasCopy));

        // different types -> returns false
        assertFalse(alias.equals("string"));

        // null -> returns false
        assertFalse(alias.equals(null));

        // different name -> return false
        Alias differentAlias = new Alias("newname", "prefix", "args");
        assertFalse(alias.equals(differentAlias));

        // different prefix -> return false
        differentAlias = new Alias("name", "newprefix", "args");
        assertFalse(alias.equals(differentAlias));

        // different arguments -> return false
        differentAlias = new Alias("name", "prefix", "newargs");
        assertFalse(alias.equals(differentAlias));
    }

    @Test
    public void hashCode_sameData_equals() {
        Alias alias = new Alias("name", "prefix", "args");
        Alias aliasCopy = new Alias("name", "prefix", "args");

        assertEquals(alias.hashCode(), alias.hashCode());
        assertEquals(alias.hashCode(), aliasCopy.hashCode());
    }
}
