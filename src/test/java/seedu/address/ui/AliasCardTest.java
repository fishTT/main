package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.testutil.GuiTestAssert.assertAliasCardDisplaysAlias;

import org.junit.Test;

import guitests.guihandles.AliasCardHandle;
import seedu.address.model.alias.Alias;

public class AliasCardTest extends GuiUnitTest {

    @Test
    public void display() {
        Alias alias = new Alias("urd", "list", "s/unread");
        AliasCard aliasCard = new AliasCard(alias, 1);
        uiPartRule.setUiPart(aliasCard);
        assertCardDisplay(aliasCard, alias, 1);
    }

    @Test
    public void equals() {
        Alias alias = new Alias("1", "1", "1");
        AliasCard aliasCard = new AliasCard(alias, 0);

        // same alias, same index -> returns true
        AliasCard copy = new AliasCard(alias, 0);
        assertTrue(aliasCard.equals(copy));

        // same object -> returns true
        assertTrue(aliasCard.equals(aliasCard));

        // null -> returns false
        assertFalse(aliasCard.equals(null));

        // different types -> returns false
        assertFalse(aliasCard.equals("123"));

        // different alias, same index -> returns false
        Alias differentAlias = new Alias("1", "1", "123");
        assertFalse(aliasCard.equals(new AliasCard(differentAlias, 0)));

        // same alias, different index -> returns false
        assertFalse(aliasCard.equals(new AliasCard(alias, 1)));
    }

    /**
     * Asserts that {@code aliasCard} displays the details of {@code expectedAlias} correctly and matches
     * {@code expectedId}.
     */
    private void assertCardDisplay(AliasCard aliasCard, Alias expectedAlias, int expectedId) {
        guiRobot.pauseForHuman();

        AliasCardHandle aliasCardHandle = new AliasCardHandle(aliasCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ".", aliasCardHandle.getId());

        // verify alias details are displayed correctly
        assertAliasCardDisplaysAlias(expectedAlias, aliasCardHandle);
    }
}
