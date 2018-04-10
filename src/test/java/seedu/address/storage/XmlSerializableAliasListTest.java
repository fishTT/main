package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.alias.Alias;
import seedu.address.model.alias.ReadOnlyAliasList;
import seedu.address.model.alias.UniqueAliasList;
import seedu.address.testutil.TypicalAliases;

public class XmlSerializableAliasListTest {

    private static final String TEST_DATA_FOLDER = FileUtil.getPath("src/test/data/XmlSerializableAliasListTest/");
    private static final File VALID_ALIASES_FILE = new File(TEST_DATA_FOLDER + "typicalAliasesAliasList.xml");
    private static final File INVALID_ALIASES_FILE = new File(TEST_DATA_FOLDER + "invalidAliasList.xml");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toModelType_typicalAliasesAliasListFile_success() throws Exception {
        XmlSerializableAliasList dataFromFile = XmlUtil.getDataFromFile(VALID_ALIASES_FILE,
                XmlSerializableAliasList.class);
        ReadOnlyAliasList aliasListFromFile = dataFromFile.toModelType();
        UniqueAliasList typicalAliasList = TypicalAliases.getTypicalAliasList();
        assertEquals(aliasListFromFile, typicalAliasList);
    }

    @Test
    public void toModelType_invalidAliasListFile_throwsIllegalValueException() throws Exception {
        XmlSerializableAliasList dataFromFile = XmlUtil.getDataFromFile(INVALID_ALIASES_FILE,
                XmlSerializableAliasList.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

    @Test
    public void equals() {
        UniqueAliasList aliasList = new UniqueAliasList();
        aliasList.add(new Alias("1", "1", "1"));
        XmlSerializableAliasList xmlAliasList = new XmlSerializableAliasList(aliasList);

        // same object -> return true
        assertTrue(xmlAliasList.equals(xmlAliasList));

        // same aliases in list -> return true
        UniqueAliasList aliasListCopy = new UniqueAliasList();
        aliasListCopy.add(new Alias("1", "1", "1"));
        XmlSerializableAliasList xmlAliasListCopy = new XmlSerializableAliasList(aliasListCopy);
        assertTrue(xmlAliasList.equals(xmlAliasListCopy));

        // different types -> returns false
        assertFalse(xmlAliasList.equals("string"));

        // null -> returns false
        assertFalse(xmlAliasList.equals(null));

        // different aliases in list -> return false
        aliasListCopy.add(new Alias("2", "2", "2"));
        XmlSerializableAliasList differentXmlAliasList = new XmlSerializableAliasList(aliasListCopy);
        assertFalse(xmlAliasList.equals(differentXmlAliasList));
    }
}
