package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.address.testutil.TypicalAliases.getTypicalAliases;
import static seedu.address.ui.testutil.GuiTestAssert.assertAliasCardDisplaysAlias;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.AliasCardHandle;
import guitests.guihandles.AliasListPanelHandle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.alias.Alias;

public class AliasListPanelTest extends GuiUnitTest {
    private static final ObservableList<Alias> TYPICAL_ALIASES = FXCollections.observableList(getTypicalAliases());

    private AliasListPanel aliasListPanel;
    private AliasListPanelHandle aliasListPanelHandle;

    @Before
    public void setUp() {
        aliasListPanel = new AliasListPanel(TYPICAL_ALIASES);
        uiPartRule.setUiPart(aliasListPanel);

        aliasListPanelHandle = new AliasListPanelHandle(aliasListPanel.getRoot());
    }

    @Test
    public void display() {
        // hidden by default
        assertFalse(aliasListPanelHandle.isVisible());

        aliasListPanel.show();

        for (int i = 0; i < TYPICAL_ALIASES.size(); i++) {
            aliasListPanelHandle.navigateToCard(TYPICAL_ALIASES.get(i));
            Alias expectedAlias = TYPICAL_ALIASES.get(i);
            AliasCardHandle actualCard = aliasListPanelHandle.getAliasCardHandle(i).get();

            assertAliasCardDisplaysAlias(expectedAlias, actualCard);
            assertEquals(Integer.toString(i + 1) + ".", actualCard.getId());
        }
    }
}
