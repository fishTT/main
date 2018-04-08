package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.storage.XmlAdaptedAlias.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.storage.XmlAdaptedAlias.NAMED_ARGS_FIELD;
import static seedu.address.storage.XmlAdaptedAlias.NAME_FIELD;
import static seedu.address.storage.XmlAdaptedAlias.PREFIX_FIELD;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.alias.Alias;
import seedu.address.testutil.Assert;

public class XmlAdaptedAliasTest {

    private static final String VALID_NAME = "urd";
    private static final String VALID_PREFIX = "list";
    private static final String VALID_NAMED_ARGS = " s/unread";
    private static final Alias VALID_ALIAS = new Alias(VALID_NAME, VALID_PREFIX, VALID_NAMED_ARGS);

    @Test
    public void toModelType_validAliasDetails_returnsAlias() throws Exception {
        XmlAdaptedAlias alias = new XmlAdaptedAlias(VALID_ALIAS);
        assertEquals(VALID_ALIAS, alias.toModelType());
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        XmlAdaptedAlias alias = new XmlAdaptedAlias(null, VALID_PREFIX, VALID_NAMED_ARGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, NAME_FIELD);
        Assert.assertThrows(IllegalValueException.class, expectedMessage, alias::toModelType);
    }

    @Test
    public void toModelType_nullPrefix_throwsIllegalValueException() {
        XmlAdaptedAlias alias = new XmlAdaptedAlias(VALID_NAME, null, VALID_NAMED_ARGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, PREFIX_FIELD);
        Assert.assertThrows(IllegalValueException.class, expectedMessage, alias::toModelType);
    }

    @Test
    public void toModelType_nullArguments_throwsIllegalValueException() {
        XmlAdaptedAlias alias = new XmlAdaptedAlias(VALID_NAME, VALID_PREFIX, null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, NAMED_ARGS_FIELD);
        Assert.assertThrows(IllegalValueException.class, expectedMessage, alias::toModelType);
    }

    @Test
    public void equals() {
        XmlAdaptedAlias xmlAdaptedAlias = new XmlAdaptedAlias(VALID_NAME, VALID_PREFIX, VALID_NAMED_ARGS);

        // same object -> return true
        assertTrue(xmlAdaptedAlias.equals(xmlAdaptedAlias));

        // same values -> return true
        XmlAdaptedAlias xmlAdaptedAliasCopy = new XmlAdaptedAlias(VALID_NAME, VALID_PREFIX, VALID_NAMED_ARGS);
        assertTrue(xmlAdaptedAlias.equals(xmlAdaptedAliasCopy));

        // different types -> returns false
        assertFalse(xmlAdaptedAlias.equals("string"));

        // null -> returns false
        assertFalse(xmlAdaptedAlias.equals(null));

        // different name -> return false
        XmlAdaptedAlias differentAlias = new XmlAdaptedAlias("x", VALID_PREFIX, VALID_NAMED_ARGS);
        assertFalse(xmlAdaptedAlias.equals(differentAlias));

        // different prefix -> return false
        differentAlias = new XmlAdaptedAlias(VALID_NAME, "x", VALID_NAMED_ARGS);
        assertFalse(xmlAdaptedAlias.equals(differentAlias));

        // different arguments -> return false
        differentAlias = new XmlAdaptedAlias(VALID_NAME, VALID_PREFIX, "x");
        assertFalse(xmlAdaptedAlias.equals(differentAlias));
    }
}
