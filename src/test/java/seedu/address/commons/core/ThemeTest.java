package seedu.address.commons.core;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.InvalidThemeException;

public class ThemeTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getThemeByName_validName_returnsTheme() throws Exception {
        assertEquals(Theme.WHITE, Theme.getThemeByName("white"));
        assertEquals(Theme.LIGHT, Theme.getThemeByName("light"));
        assertEquals(Theme.DARK, Theme.getThemeByName("dark"));

        // theme names should be case insensitive
        assertEquals(Theme.WHITE, Theme.getThemeByName("whITE"));
        assertEquals(Theme.LIGHT, Theme.getThemeByName("LIGht"));
        assertEquals(Theme.DARK, Theme.getThemeByName("Dark"));
    }

    @Test
    public void getThemeByName_null_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        Theme.getThemeByName(null);
    }

    @Test
    public void getThemeByName_invalidName_throwsInvalidThemeException() throws Exception {
        thrown.expect(InvalidThemeException.class);
        Theme.getThemeByName("12345");
    }
}
