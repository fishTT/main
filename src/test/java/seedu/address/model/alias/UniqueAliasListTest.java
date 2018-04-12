package seedu.address.model.alias;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class UniqueAliasListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new UniqueAliasList(null);
    }

    @Test
    public void constructor_emptyList_newEmptyList() {
        UniqueAliasList aliasList = new UniqueAliasList();
        UniqueAliasList aliasListCopy = new UniqueAliasList(aliasList);
        assertEquals(0, aliasListCopy.size());

        // check that the copied list is independent of the existing list
        aliasList.add(new Alias("3", "3", "3"));
        assertEquals(0, aliasListCopy.size());
    }

    @Test
    public void constructor_nonEmptyList_aliasesCopied() {
        UniqueAliasList aliasList = new UniqueAliasList();
        aliasList.add(new Alias("1", "1", "1"));
        aliasList.add(new Alias("2", "2", "2"));
        UniqueAliasList aliasListCopy = new UniqueAliasList(aliasList);
        assertEquals(2, aliasListCopy.size());
        assertTrue(aliasList.getAliasByName("1").isPresent());
        assertTrue(aliasList.getAliasByName("2").isPresent());

        // check that the copied list is independent of the existing list
        aliasList.add(new Alias("3", "3", "3"));
        assertEquals(2, aliasListCopy.size());
    }

    @Test
    public void add_existingAlias_aliasReplaced() {
        UniqueAliasList aliasList = new UniqueAliasList();
        aliasList.add(new Alias("1", "1", "1"));
        aliasList.add(new Alias("1", "updated", "updated"));
        assertEquals(1, aliasList.size());
        Alias updatedAlias = aliasList.getAliasByName("1").get();
        assertEquals("updated", updatedAlias.getPrefix());
        assertEquals("updated", updatedAlias.getNamedArgs());
    }

    @Test
    public void remove_validAlias_success() {
        UniqueAliasList aliasList = new UniqueAliasList();
        aliasList.add(new Alias("11", "11", "11"));
        aliasList.add(new Alias("22", "22", "22"));
        aliasList.remove("11");
        assertEquals(1, aliasList.size());
        assertFalse(aliasList.getAliasByName("11").isPresent());
        assertTrue(aliasList.getAliasByName("22").isPresent());
    }

    @Test
    public void remove_invalidAlias_doesNothing() {
        UniqueAliasList aliasList = new UniqueAliasList();
        aliasList.add(new Alias("x", "x", "x"));
        aliasList.remove("y");
        assertEquals(1, aliasList.size());
    }
}
